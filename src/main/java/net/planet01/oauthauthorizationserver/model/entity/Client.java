package net.planet01.oauthauthorizationserver.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity(name = "oauth_clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name",unique=true,nullable = false)
    private String clientName;

    @Column(name = "clientSecret",nullable = false)
    private String clientSecret;

    @Column(name = "scope",nullable = false)
    private String scope;

    @Column(name = "grant_type",nullable = false)
    private String grantType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
