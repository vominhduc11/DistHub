package com.devwonder.product_service.repository;

import com.devwonder.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    Optional<Product> findBySku(String sku);
    
    List<Product> findByCategoryId(String categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.name ILIKE %:keyword% OR p.description ILIKE %:keyword%")
    List<Product> findByNameOrDescriptionContainingIgnoreCase(@Param("keyword") String keyword);
    
    List<Product> findByPopularityGreaterThanEqual(Integer popularity);
    
    @Query("SELECT p FROM Product p WHERE p.publishedAt IS NOT NULL ORDER BY p.publishedAt DESC")
    List<Product> findPublishedProducts();
    
    @Query("SELECT p FROM Product p WHERE p.rating >= :minRating ORDER BY p.rating DESC")
    List<Product> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);
}