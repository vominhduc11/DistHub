package com.devwonder.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductVideoCreateDTO {
    private String videoId;
    
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Video URL is required")
    @Size(max = 500, message = "URL must not exceed 500 characters")
    private String url;
    
    @Size(max = 500, message = "Thumbnail must not exceed 500 characters")
    private String thumbnail;
    
    @Size(max = 50, message = "Duration must not exceed 50 characters")
    private String duration;
    
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type;
}