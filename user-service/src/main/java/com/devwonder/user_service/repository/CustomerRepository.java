package com.devwonder.user_service.repository;

import com.devwonder.user_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Basic queries
    Optional<Customer> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    // Date range queries
    @Query("SELECT c FROM Customer c WHERE c.createdAt >= :since")
    List<Customer> findRecentlyCreated(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM Customer c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Customer> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT c FROM Customer c WHERE c.updatedAt >= :since")
    List<Customer> findRecentlyUpdated(@Param("since") LocalDateTime since);
    
    // Statistics queries
    @Query("SELECT COUNT(c) FROM Customer c")
    Long countAllCustomers();
}