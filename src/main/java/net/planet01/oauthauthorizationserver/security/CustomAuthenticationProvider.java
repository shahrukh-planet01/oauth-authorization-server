package net.planet01.oauthauthorizationserver.security;

import net.planet01.oauthauthorizationserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private CustomLdapAuth customLdapAuth;
    @Autowired
    private UserService userService;
    @Autowired
    @Lazy
    RedisTokenStore redisTokenStore;

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
            this.removeOldAccessToken(user.getUsername());

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

    private void removeOldAccessToken(String username){
        redisTokenStore.findTokensByClientIdAndUserName("client",username).stream().forEach(x -> redisTokenStore.removeAccessToken(x.getValue()));
    }
}
