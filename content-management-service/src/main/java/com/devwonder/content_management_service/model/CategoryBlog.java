package com.devwonder.content_management_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category_blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "categoryBlog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Blog> blogs;
}