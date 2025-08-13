package com.devwonder.content_management_service.controller;

import com.devwonder.content_management_service.model.CategoryBlog;
import com.devwonder.content_management_service.service.CategoryBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/categories")
@RequiredArgsConstructor
public class CategoryBlogController {
    
    private final CategoryBlogService categoryBlogService;
    
    @GetMapping
    public ResponseEntity<List<CategoryBlog>> getAllCategories() {
        return ResponseEntity.ok(categoryBlogService.getAllCategories());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryBlog> getCategoryById(@PathVariable Long id) {
        return categoryBlogService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CategoryBlog> createCategory(@RequestBody CategoryBlog categoryBlog) {
        return ResponseEntity.ok(categoryBlogService.createCategory(categoryBlog));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoryBlog> updateCategory(@PathVariable Long id, @RequestBody CategoryBlog categoryBlog) {
        return ResponseEntity.ok(categoryBlogService.updateCategory(id, categoryBlog));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryBlogService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}