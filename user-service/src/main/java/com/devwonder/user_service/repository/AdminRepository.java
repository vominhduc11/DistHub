package com.devwonder.user_service.repository;

import com.devwonder.user_service.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    // Basic queries
    Optional<Admin> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    // Date range queries
    @Query("SELECT a FROM Admin a WHERE a.createdAt >= :since")
    List<Admin> findRecentlyCreated(@Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM Admin a WHERE a.createdAt BETWEEN :startDate AND :endDate")
    List<Admin> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Admin a WHERE a.updatedAt >= :since")
    List<Admin> findRecentlyUpdated(@Param("since") LocalDateTime since);
    
    // Statistics queries
    @Query("SELECT COUNT(a) FROM Admin a")
    Long countAllAdmins();
}