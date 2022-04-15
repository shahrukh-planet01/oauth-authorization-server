package net.planet01.oauthauthorizationserver.security;

import net.planet01.oauthauthorizationserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private CustomLdapAuth customLdapAuth;
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication
                .getCredentials()
                .toString();

        CustomUserDetails user = customUserDetailService.loadUserByUsername(username);

        if(userService.isAccountLocked(user.getUser())) {
            System.out.println("lock condition");
            throw new LockedException(
                    String.format("Your account has been locked. It will be unlocked after %d minutes.",userService.remainingLockPeriod(user.getUser().getLockTime()))
            );
        }

        if (customLdapAuth.authenticateUser(username,password)) {
            userService.resetFailedAttempts(user.getUser());
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        }
        int failedAttempts = userService.increaseFailedAttempts(user.getUser());

        if(userService.lockIfApplicable(user.getUser(),failedAttempts)) {
            throw new LockedException(
                    String.format("Your account has been locked. It will be unlocked after %d minutes.",userService.remainingLockPeriod(user.getUser().getLockTime()))
            );
        }

        throw new UsernameNotFoundException("Invalid Username or password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
