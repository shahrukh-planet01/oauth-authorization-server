package net.planet01.oauthauthorizationserver.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserRoleKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "role_id")
    Long roleId;

    public UserRoleKey(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRoleKey() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
