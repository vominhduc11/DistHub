package com.devwonder.user_service.repository;

import com.devwonder.user_service.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    // Basic queries
    Optional<Admin> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    Optional<Admin> findByEmployeeId(String employeeId);
    
    boolean existsByEmployeeId(String employeeId);
    
    // Search queries
    List<Admin> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<Admin> findByLastNameContainingIgnoreCase(String lastName);
    
    @Query("SELECT a FROM Admin a WHERE " +
           "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Admin> findByFullNameContaining(@Param("name") String name);
    
    // Department and role queries
    List<Admin> findByDepartment(String department);
    
    List<Admin> findByJobTitle(String jobTitle);
    
    List<Admin> findByAdminLevel(Admin.AdminLevel adminLevel);
    
    List<Admin> findByDepartmentAndJobTitle(String department, String jobTitle);
    
    // Hierarchy queries
    List<Admin> findByManagerAccountId(Long managerAccountId);
    
    @Query("SELECT a FROM Admin a WHERE a.managerAccountId IS NULL")
    List<Admin> findTopLevelAdmins();
    
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.managerAccountId = :managerAccountId")
    Long countDirectReports(@Param("managerAccountId") Long managerAccountId);
    
    // Location queries
    List<Admin> findByOfficeLocation(String officeLocation);
    
    // Access level queries
    @Query("SELECT a FROM Admin a WHERE a.accessLevel >= :minLevel")
    List<Admin> findByAccessLevelGreaterThanEqual(@Param("minLevel") Integer minLevel);
    
    @Query("SELECT a FROM Admin a WHERE a.accessLevel BETWEEN :minLevel AND :maxLevel")
    List<Admin> findByAccessLevelBetween(@Param("minLevel") Integer minLevel, 
                                         @Param("maxLevel") Integer maxLevel);
    
    // Permission queries
    List<Admin> findByCanApproveOrdersTrue();
    
    List<Admin> findByCanManageUsersTrue();
    
    List<Admin> findByCanManageProductsTrue();
    
    List<Admin> findByCanManageInventoryTrue();
    
    List<Admin> findByCanViewReportsTrue();
    
    List<Admin> findByCanManageSystemTrue();
    
    @Query("SELECT a FROM Admin a WHERE a.maxApprovalAmount >= :amount")
    List<Admin> findByMaxApprovalAmountGreaterThanEqual(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Admin a WHERE a.canApproveOrders = true AND a.maxApprovalAmount >= :amount")
    List<Admin> findWhoCanApproveAmount(@Param("amount") BigDecimal amount);
    
    // Status queries
    List<Admin> findByIsActiveTrue();
    
    List<Admin> findByIsActiveFalse();
    
    @Query("SELECT a FROM Admin a WHERE a.accountLockedUntil IS NOT NULL AND a.accountLockedUntil > :now")
    List<Admin> findCurrentlyLockedAccounts(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Admin a WHERE a.failedLoginAttempts >= :attempts")
    List<Admin> findByFailedLoginAttemptsGreaterThanEqual(@Param("attempts") Integer attempts);
    
    // Activity queries
    @Query("SELECT a FROM Admin a WHERE a.lastLoginAt >= :since")
    List<Admin> findByLastLoginSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM Admin a WHERE a.lastLoginAt < :before OR a.lastLoginAt IS NULL")
    List<Admin> findInactiveAdminsSince(@Param("before") LocalDateTime before);
    
    @Query("SELECT a FROM Admin a WHERE a.passwordChangedAt < :before OR a.passwordChangedAt IS NULL")
    List<Admin> findAdminsNeedingPasswordChange(@Param("before") LocalDateTime before);
    
    // Date range queries
    @Query("SELECT a FROM Admin a WHERE a.hireDate BETWEEN :startDate AND :endDate")
    List<Admin> findByHireDateBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Admin a WHERE a.createdAt >= :since")
    List<Admin> findRecentlyCreated(@Param("since") LocalDateTime since);
    
    // Statistics queries
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.department = :department")
    Long countByDepartment(@Param("department") String department);
    
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.adminLevel = :level")
    Long countByAdminLevel(@Param("level") Admin.AdminLevel level);
    
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.isActive = true")
    Long countActiveAdmins();
    
    @Query("SELECT AVG(a.accessLevel) FROM Admin a WHERE a.isActive = true")
    Double getAverageAccessLevel();
    
    // Complex queries
    @Query("SELECT a FROM Admin a WHERE a.isActive = true " +
           "AND a.adminLevel IN ('MANAGER', 'DIRECTOR', 'SUPER_ADMIN') " +
           "ORDER BY a.adminLevel DESC, a.accessLevel DESC")
    List<Admin> findSeniorActiveAdmins();
    
    @Query("SELECT a FROM Admin a WHERE a.canManageUsers = true " +
           "AND a.isActive = true AND a.department = :department")
    List<Admin> findUserManagersInDepartment(@Param("department") String department);
    
    @Query("SELECT DISTINCT a.department FROM Admin a WHERE a.department IS NOT NULL ORDER BY a.department")
    List<String> findAllDepartments();
    
    @Query("SELECT DISTINCT a.jobTitle FROM Admin a WHERE a.jobTitle IS NOT NULL ORDER BY a.jobTitle")
    List<String> findAllJobTitles();
}