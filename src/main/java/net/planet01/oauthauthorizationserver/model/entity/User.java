package net.planet01.oauthauthorizationserver.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "oauth_user")
@Table(uniqueConstraints =
        { @UniqueConstraint(name = "employeeCodeUsernameAndEmail", columnNames = { "employee_code", "username","email" })})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_enabled",columnDefinition = "bit default 1")
    private boolean enabled;

    @Column(name = "is_account_locked",columnDefinition = "bit default 1")
    private boolean accountLocked;

    @Column(name = "failed_attempt",columnDefinition = "integer default 0")
    private int failedAttempt;

    @Column(name = "is_temporary_user",columnDefinition = "bit default 0")
    private boolean temporaryUser;

    @Column(name = "temporary_access_expire_date",nullable = true)
    private LocalDateTime temporaryAccessExpireDate;

    @Column(name = "lock_time",nullable = true)
    private LocalDateTime lockTime;

    @Column(name = "last_login_date",nullable = true)
    private LocalDate lastLoginDate;

    @Column(name = "is_admin",columnDefinition = "bit default 0")
    private boolean isAdmin;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    Set<UserHasRole> roles;

    public User() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountNonLocked) {
        this.accountLocked = accountNonLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public boolean isTemporaryUser() {
        return temporaryUser;
    }

    public void setTemporaryUser(boolean temporaryUser) {
        this.temporaryUser = temporaryUser;
    }

    public LocalDateTime getTemporaryAccessExpireDate() {
        return temporaryAccessExpireDate;
    }

    public void setTemporaryAccessExpireDate(LocalDateTime temporaryAccessExpireDate) {
        this.temporaryAccessExpireDate = temporaryAccessExpireDate;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
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

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<UserHasRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserHasRole> roles) {
        this.roles = roles;
    }


}
