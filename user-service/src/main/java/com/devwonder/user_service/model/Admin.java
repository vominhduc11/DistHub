package com.devwonder.user_service.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "admins")
public class Admin {
    
    @Id
    @EqualsAndHashCode.Include
    private Long accountId; // Foreign key reference to auth-service Account.id

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "employee_id", unique = true, length = 50)
    private String employeeId;

    @Column(length = 100)
    private String department;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_level", length = 20)
    private AdminLevel adminLevel = AdminLevel.OPERATOR;

    @Column(name = "direct_phone", length = 20)
    private String directPhone;

    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "office_location", length = 200)
    private String officeLocation;

    @Column(name = "manager_account_id")
    private Long managerAccountId; // Reference to another admin's accountId

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relation", length = 50)
    private String emergencyContactRelation;

    @Column(name = "access_level")
    private Integer accessLevel = 1;

    @Column(name = "can_approve_orders")
    private Boolean canApproveOrders = false;

    @Column(name = "can_manage_users")
    private Boolean canManageUsers = false;

    @Column(name = "can_manage_products")
    private Boolean canManageProducts = false;

    @Column(name = "can_manage_inventory")
    private Boolean canManageInventory = false;

    @Column(name = "can_view_reports")
    private Boolean canViewReports = false;

    @Column(name = "can_manage_system")
    private Boolean canManageSystem = false;

    @Column(name = "max_approval_amount", precision = 15, scale = 2)
    private java.math.BigDecimal maxApprovalAmount = java.math.BigDecimal.ZERO;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Admin(Long accountId, String firstName, String lastName) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void recordLogin(String ipAddress) {
        this.lastLoginAt = LocalDateTime.now();
        this.lastLoginIp = ipAddress;
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.accountLockedUntil = LocalDateTime.now().plusHours(24);
        }
    }

    public boolean isAccountLocked() {
        return accountLockedUntil != null && LocalDateTime.now().isBefore(accountLockedUntil);
    }

    public void unlockAccount() {
        this.accountLockedUntil = null;
        this.failedLoginAttempts = 0;
    }

    public void updatePasswordChanged() {
        this.passwordChangedAt = LocalDateTime.now();
    }

    public boolean hasPermission(String permission) {
        return switch (permission.toUpperCase()) {
            case "APPROVE_ORDERS" -> canApproveOrders;
            case "MANAGE_USERS" -> canManageUsers;
            case "MANAGE_PRODUCTS" -> canManageProducts;
            case "MANAGE_INVENTORY" -> canManageInventory;
            case "VIEW_REPORTS" -> canViewReports;
            case "MANAGE_SYSTEM" -> canManageSystem;
            default -> false;
        };
    }

    public boolean canApproveAmount(java.math.BigDecimal amount) {
        return canApproveOrders && 
               maxApprovalAmount != null && 
               amount.compareTo(maxApprovalAmount) <= 0;
    }

    public enum AdminLevel {
        OPERATOR, SUPERVISOR, MANAGER, DIRECTOR, SUPER_ADMIN
    }
}