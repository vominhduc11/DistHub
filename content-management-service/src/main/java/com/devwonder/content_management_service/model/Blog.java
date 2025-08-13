package com.devwonder.content_management_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "cover_image_url")
    private String coverImageUrl;
    
    @Column(name = "publish_date")
    private LocalDate publishDate;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "read_time_minutes")
    private Integer readTimeMinutes;
    
    @Column(columnDefinition = "TEXT")
    private String detail;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_blog_id", nullable = false)
    private CategoryBlog categoryBlog;
}