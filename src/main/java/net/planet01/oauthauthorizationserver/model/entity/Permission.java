package net.planet01.oauthauthorizationserver.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "permission",fetch = FetchType.EAGER)
    Set<RoleHasPermission> permissions;

    public Permission() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoleHasPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<RoleHasPermission> roleHasPermissions) {
        this.permissions = roleHasPermissions;
    }
}
