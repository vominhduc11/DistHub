package com.devwonder.product_service.dto;

import lombok.Data;

@Data
public class ProductImageDTO {
    private Long id;
    private String imageId;
    private String url;
    private String altText;
    private String type;
    private Integer displayOrder;
}