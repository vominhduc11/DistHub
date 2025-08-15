package com.devwonder.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    private String subtitle;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "long_description", columnDefinition = "TEXT")
    private String longDescription;
    
    @Column(name = "category_id")
    private String categoryId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonBackReference
    private ProductCategory category;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> specifications;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> features;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> availability;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> warranty;
    
    @ElementCollection
    @CollectionTable(name = "product_highlights", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "highlight")
    private List<String> highlights;
    
    @ElementCollection
    @CollectionTable(name = "product_target_audience", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "audience")
    private List<String> targetAudience;
    
    @ElementCollection
    @CollectionTable(name = "product_use_cases", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "use_case")
    private List<String> useCases;
    
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    @ElementCollection
    @CollectionTable(name = "product_related_products", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "related_product_id")
    private List<String> relatedProductIds;
    
    @ElementCollection
    @CollectionTable(name = "product_accessories", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "accessory")
    private List<String> accessories;
    
    private Integer popularity;
    
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;
    
    @Column(name = "review_count")
    private Integer reviewCount;
    
    @Column(unique = true)
    private String sku;
    
    @Column(name = "seo_title")
    private String seoTitle;
    
    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductImage> images;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductVideo> videos;
}