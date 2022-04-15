package net.planet01.oauthauthorizationserver.config;

import net.planet01.oauthauthorizationserver.security.CustomTokenEnhancer;
import net.planet01.oauthauthorizationserver.security.EnhancedAuthenticationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    CustomTokenEnhancer customTokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(redisTokenStore()) // registering redisTokenStore bean
                .tokenEnhancer(customTokenEnhancer);
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("clientsecret")
                .authorizedGrantTypes("password")
                .scopes("read")
                .and()
                .withClient("resourceserver")
                .secret("resourceserversecret");
    }
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess
                ("isAuthenticated()");
    }
    @Bean
    public RedisTokenStore redisTokenStore() {
        var redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.findTokensByClientIdAndUserName("client","shah").stream().forEach(x -> redisTokenStore.removeAccessToken(x.getValue()));
        redisTokenStore.setAuthenticationKeyGenerator(new EnhancedAuthenticationKeyGenerator());
        return redisTokenStore;
    }

}
