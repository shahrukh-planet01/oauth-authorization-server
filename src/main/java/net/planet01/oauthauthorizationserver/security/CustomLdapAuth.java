package net.planet01.oauthauthorizationserver.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomLdapAuth {


    private LdapContextSource contextSource;

    private LdapTemplate ldapTemplate;

    @Value("${ldap.server.url}")
    private String ldapUrl;

    @Value("${ldap.user.domain}")
    private String ldapUserDn;

    private void initContext()
    {
        contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        contextSource.setAnonymousReadOnly(true);
        contextSource.setUserDn(ldapUserDn);
        contextSource.afterPropertiesSet();
        ldapTemplate = new LdapTemplate(contextSource);

    }

    public Boolean authenticateUser(String username,String password){
        initContext();
        Filter filter = new EqualsFilter("uid", username);
        Boolean authenticate = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.encode(), password);
        return authenticate;
    }
}
