package com.devwonder.product_service.dto;

import lombok.Data;

@Data
public class ProductCategoryDTO {
    private String id;
    private String name;
    private String description;
    private String slug;
}