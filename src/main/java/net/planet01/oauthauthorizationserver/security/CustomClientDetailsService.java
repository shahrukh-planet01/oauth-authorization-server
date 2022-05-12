package net.planet01.oauthauthorizationserver.security;

import net.planet01.oauthauthorizationserver.model.entity.Client;
import net.planet01.oauthauthorizationserver.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;
    @Value("${oauth.access.token.validity.seconds}")
    private Integer accessTokenValiditySeconds;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        Supplier<ClientRegistrationException> supplier =
                () -> new ClientRegistrationException("Invalid client username or password");

        Client client = clientRepository
                .findClientByClientName(s)
                .orElseThrow(supplier);

        var cd = new BaseClientDetails();
        cd.setClientId(client.getClientName());
        cd.setClientSecret(client.getClientSecret());
        cd.setScope(List.of(client.getScope()));
        cd.setAuthorizedGrantTypes(List.of(client.getGrantType()));
        cd.setAccessTokenValiditySeconds(accessTokenValiditySeconds); // 24 hours

        return cd;

    }
}
