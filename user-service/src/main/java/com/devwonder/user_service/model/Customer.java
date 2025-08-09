package com.devwonder.user_service.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @EqualsAndHashCode.Include
    private Long accountId; // Foreign key reference to auth-service Account.id

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(length = 100)
    private String country;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_level", length = 20)
    private LoyaltyLevel loyaltyLevel = LoyaltyLevel.BRONZE;

    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    @Column(name = "total_spent", precision = 15, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage = "en";

    @Column(name = "marketing_consent")
    private Boolean marketingConsent = false;

    @Column(name = "email_notifications")
    private Boolean emailNotifications = true;

    @Column(name = "sms_notifications")
    private Boolean smsNotifications = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Customer(Long accountId, String firstName, String lastName) {
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

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
        updateLoyaltyLevel();
    }

    private void updateLoyaltyLevel() {
        if (loyaltyPoints >= 10000) {
            loyaltyLevel = LoyaltyLevel.PLATINUM;
        } else if (loyaltyPoints >= 5000) {
            loyaltyLevel = LoyaltyLevel.GOLD;
        } else if (loyaltyPoints >= 1000) {
            loyaltyLevel = LoyaltyLevel.SILVER;
        } else {
            loyaltyLevel = LoyaltyLevel.BRONZE;
        }
    }

    public void incrementTotalOrders() {
        this.totalOrders++;
    }

    public void addToTotalSpent(BigDecimal amount) {
        this.totalSpent = this.totalSpent.add(amount);
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum LoyaltyLevel {
        BRONZE, SILVER, GOLD, PLATINUM
    }
}