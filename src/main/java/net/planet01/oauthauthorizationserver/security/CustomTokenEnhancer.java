package net.planet01.oauthauthorizationserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    @Lazy
    @Autowired
    RedisTokenStore redisTokenStore;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        redisTokenStore.findTokensByClientIdAndUserName("client",authentication.getName()).stream().forEach(x -> redisTokenStore.removeAccessToken(x.getValue()));
        additionalInfo.put("username", authentication.getName());
        additionalInfo.put("authorities", authentication.getAuthorities().stream().map(x->x.getAuthority()).toArray());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
