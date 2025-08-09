package com.devwonder.language_service.repository;

import com.devwonder.language_service.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    
    // Basic queries
    Optional<Language> findByCode(String code);
    
    boolean existsByCode(String code);
    
    // Status queries
    List<Language> findByEnabledTrue();
    
    List<Language> findByEnabledTrueOrderBySortOrder();
    
    List<Language> findByEnabledFalse();
    
    // Default language
    Optional<Language> findByIsDefaultTrueAndEnabledTrue();
    
    // Search queries
    @Query("SELECT l FROM Language l WHERE l.enabled = true AND " +
           "(LOWER(l.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(l.nativeName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(l.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Language> findByEnabledTrueAndSearchTerm(@Param("search") String searchTerm);
    
    // Direction queries
    List<Language> findByDirectionAndEnabledTrueOrderBySortOrder(String direction);
    
    @Query("SELECT l FROM Language l WHERE l.direction = 'rtl' AND l.enabled = true ORDER BY l.sortOrder")
    List<Language> findRightToLeftLanguages();
    
    @Query("SELECT l FROM Language l WHERE l.direction = 'ltr' AND l.enabled = true ORDER BY l.sortOrder")
    List<Language> findLeftToRightLanguages();
    
    // Statistics
    @Query("SELECT COUNT(l) FROM Language l WHERE l.enabled = true")
    Long countEnabledLanguages();
    
    @Query("SELECT COUNT(l) FROM Language l WHERE l.enabled = false")
    Long countDisabledLanguages();
    
    // Custom ordering
    @Query("SELECT l FROM Language l WHERE l.enabled = true ORDER BY l.sortOrder ASC, l.name ASC")
    List<Language> findAllEnabledOrderedByPriority();
    
    // Find by name
    List<Language> findByNameContainingIgnoreCaseAndEnabledTrue(String name);
    
    List<Language> findByNativeNameContainingIgnoreCaseAndEnabledTrue(String nativeName);
}