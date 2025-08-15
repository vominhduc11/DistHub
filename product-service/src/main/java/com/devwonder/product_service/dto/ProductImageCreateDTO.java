package com.devwonder.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductImageCreateDTO {
    private String imageId;
    
    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "URL must not exceed 500 characters")
    private String url;
    
    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;
    
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type;
    
    private Integer displayOrder;
}