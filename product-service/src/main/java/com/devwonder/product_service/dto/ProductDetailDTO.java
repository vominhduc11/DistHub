package com.devwonder.product_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ProductDetailDTO {
    private String id;
    private String name;
    private String subtitle;
    private String description;
    private String longDescription;
    private ProductCategoryDTO category;
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
    private Integer popularity;
    private BigDecimal rating;
    private Integer reviewCount;
    private String sku;
    private String seoTitle;
    private String seoDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private List<ProductImageDTO> images;
    private List<ProductVideoDTO> videos;
}