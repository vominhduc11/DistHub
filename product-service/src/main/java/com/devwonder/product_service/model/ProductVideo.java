package com.devwonder.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "product_videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVideo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "video_id", unique = true)
    private String videoId;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column(length = 500)
    private String thumbnail;
    
    @Column(length = 50)
    private String duration;
    
    @Column(length = 50)
    private String type;
    
    @Column(name = "product_id")
    private String productId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonBackReference
    private Product product;
}