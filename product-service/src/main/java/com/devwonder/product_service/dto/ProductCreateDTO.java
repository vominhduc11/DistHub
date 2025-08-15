package com.devwonder.product_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductCreateDTO {
    
    private String id;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;
    
    @Size(max = 255, message = "Subtitle must not exceed 255 characters")
    private String subtitle;
    
    private String description;
    
    private String longDescription;
    
    private String categoryId;
    
    private Map<String, Object> specifications;
    
    private Map<String, Object> features;
    
    private Map<String, Object> availability;
    
    private Map<String, Object> warranty;
    
    private List<String> highlights;
    
    private List<String> targetAudience;
    
    private List<String> useCases;
    
    private List<String> tags;
    
    private List<String> relatedProductIds;
    
    private List<String> accessories;
    
    @Min(value = 0, message = "Popularity must be non-negative")
    private Integer popularity;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be between 0.0 and 5.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be between 0.0 and 5.0")
    @Digits(integer = 1, fraction = 1, message = "Rating must have at most 1 decimal place")
    private BigDecimal rating;
    
    @Min(value = 0, message = "Review count must be non-negative")
    private Integer reviewCount;
    
    @Size(max = 255, message = "SKU must not exceed 255 characters")
    private String sku;
    
    @Size(max = 255, message = "SEO title must not exceed 255 characters")
    private String seoTitle;
    
    private String seoDescription;
    
    @Valid
    private List<ProductImageCreateDTO> images;
    
    @Valid
    private List<ProductVideoCreateDTO> videos;
}