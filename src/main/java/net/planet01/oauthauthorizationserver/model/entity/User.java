package net.planet01.oauthauthorizationserver.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code",unique=true)
    private String employeeCode;

    @Column(name = "name")
    private String name;

    @Column(name = "username",unique=true)
    private String username;

    @Column(name = "email",unique=true)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_enabled",columnDefinition = "boolean default 1")
    private boolean enabled;

    @Column(name = "account_non_locked",columnDefinition = "boolean default 1")
    private boolean accountNonLocked;

    @Column(name = "failed_attempt",columnDefinition = "integer default 0")
    private int failedAttempt;

    @Column(name = "lock_time",nullable = true)
    private LocalDateTime lockTime;

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isAccountLocked() {
        return !accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    Set<UserHasRole> userHasRole;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<UserHasRole> getUserHasRole() {
        return userHasRole;
    }

    public void setUserHasRole(Set<UserHasRole> userHasRole) {
        this.userHasRole = userHasRole;
    }

}
