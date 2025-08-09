package com.devwonder.user_service.repository;

import com.devwonder.user_service.model.Reseller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResellerRepository extends JpaRepository<Reseller, Long> {
    
    // Basic queries
    Optional<Reseller> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    Optional<Reseller> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<Reseller> findByPhone(String phone);
    
    boolean existsByPhone(String phone);
    
    // Search queries
    List<Reseller> findByNameContainingIgnoreCase(String name);
    
    List<Reseller> findByEmailContainingIgnoreCase(String email);
    
    @Query("SELECT r FROM Reseller r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Reseller> findByNameOrEmailContaining(@Param("search") String search);
    
    // Location-based queries
    List<Reseller> findByCity(String city);
    
    List<Reseller> findByDistrict(String district);
    
    List<Reseller> findByCityAndDistrict(String city, String district);
    
    List<Reseller> findByAddressContainingIgnoreCase(String address);
}