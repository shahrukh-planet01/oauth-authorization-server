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

import java.time.LocalDateTime;

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
        //check user is enabled
        if(!user.getUser().isEnabled()) {
            throw new LockedException(
                    String.format("Your account has been disabled. Please contact to respective department.")
            );
        }
        //check user is temporary and also check user access expiry
        if(user.getUser().isTemporaryUser()){
            checkTemporaryUserAccessExpired(user.getUser().getTemporaryAccessExpireDate());
        }
        //check user is freezed
        if(userService.isAccountFreezed(user.getUser())){
            throw new LockedException(
                    String.format("Your account has been freezed. Please contact to respective department.")
            );
        }
        //check is user account lock because of many failed attempts
        if(userService.isAccountLocked(user.getUser())) {
            throw new LockedException(
                    String.format("Your account has been locked. It will be unlocked after %d minutes.",userService.remainingLockPeriod(user.getUser().getLockTime()))
            );
        }
        //authenticating user from LDAP server
        if (customLdapAuth.authenticateUser(username,password)) {
            userService.resetFailedAttempts(user.getUser());
            userService.updateLastLoginDate(user.getUser());
            this.removeOldAccessToken(user.getUsername());

            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        }
        //increase failed attempts
        int failedAttempts = userService.increaseFailedAttempts(user.getUser());
        //lock user account if applicable
        if(userService.lockIfApplicable(user.getUser(),failedAttempts)) {
            throw new LockedException(
                    String.format("Your account has been locked. It will be unlocked after %d minutes.",userService.remainingLockPeriod(user.getUser().getLockTime()))
            );
        }
        //throw exception
        throw new UsernameNotFoundException("Invalid username or password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }

    private void removeOldAccessToken(String username){
        redisTokenStore.findTokensByClientIdAndUserName("client",username).stream().forEach(x -> redisTokenStore.removeAccessToken(x.getValue()));
    }

    private void checkTemporaryUserAccessExpired(LocalDateTime expireDate) throws LockedException
    {
        if(userService.isTemporaryUserAccessExpired(expireDate)){
            throw new LockedException("Your temporary account access expired.");
        }
    }
}
