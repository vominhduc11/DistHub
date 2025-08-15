package com.devwonder.product_service.dto;

import lombok.Data;

@Data
public class ProductVideoDTO {
    private Long id;
    private String videoId;
    private String title;
    private String description;
    private String url;
    private String thumbnail;
    private String duration;
    private String type;
}