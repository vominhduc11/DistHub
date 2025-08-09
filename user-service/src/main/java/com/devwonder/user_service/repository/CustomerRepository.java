package com.devwonder.user_service.repository;

import com.devwonder.user_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Basic queries
    Optional<Customer> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    // Search queries
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> findByFullNameContaining(@Param("name") String name);
    
    List<Customer> findByPhoneContaining(String phone);
    
    // Location-based queries
    List<Customer> findByCity(String city);
    
    List<Customer> findByState(String state);
    
    List<Customer> findByCountry(String country);
    
    List<Customer> findByCityAndState(String city, String state);
    
    // Loyalty queries
    List<Customer> findByLoyaltyLevel(Customer.LoyaltyLevel loyaltyLevel);
    
    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :minPoints")
    List<Customer> findByLoyaltyPointsGreaterThanEqual(@Param("minPoints") Integer minPoints);
    
    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints BETWEEN :minPoints AND :maxPoints")
    List<Customer> findByLoyaltyPointsBetween(@Param("minPoints") Integer minPoints, 
                                              @Param("maxPoints") Integer maxPoints);
    
    // Purchase behavior queries
    @Query("SELECT c FROM Customer c WHERE c.totalOrders >= :minOrders")
    List<Customer> findByTotalOrdersGreaterThanEqual(@Param("minOrders") Integer minOrders);
    
    @Query("SELECT c FROM Customer c WHERE c.totalSpent >= :minAmount")
    List<Customer> findByTotalSpentGreaterThanEqual(@Param("minAmount") BigDecimal minAmount);
    
    @Query("SELECT c FROM Customer c WHERE c.totalSpent BETWEEN :minAmount AND :maxAmount")
    List<Customer> findByTotalSpentBetween(@Param("minAmount") BigDecimal minAmount, 
                                           @Param("maxAmount") BigDecimal maxAmount);
    
    // Marketing queries
    List<Customer> findByMarketingConsentTrue();
    
    List<Customer> findByEmailNotificationsTrue();
    
    List<Customer> findBySmsNotificationsTrue();
    
    List<Customer> findByPreferredLanguage(String language);
    
    // Top customers
    @Query("SELECT c FROM Customer c ORDER BY c.totalSpent DESC")
    List<Customer> findTopCustomersByTotalSpent();
    
    @Query("SELECT c FROM Customer c ORDER BY c.loyaltyPoints DESC")
    List<Customer> findTopCustomersByLoyaltyPoints();
    
    @Query("SELECT c FROM Customer c ORDER BY c.totalOrders DESC")
    List<Customer> findTopCustomersByTotalOrders();
    
    // Statistics
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.loyaltyLevel = :level")
    Long countByLoyaltyLevel(@Param("level") Customer.LoyaltyLevel level);
    
    @Query("SELECT AVG(c.totalSpent) FROM Customer c")
    BigDecimal getAverageTotalSpent();
    
    @Query("SELECT AVG(c.loyaltyPoints) FROM Customer c")
    Double getAverageLoyaltyPoints();
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.city = :city")
    Long countByCity(@Param("city") String city);
}