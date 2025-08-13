package com.devwonder.content_management_service.service;

import com.devwonder.content_management_service.model.CategoryBlog;
import com.devwonder.content_management_service.repository.CategoryBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryBlogService {
    
    private final CategoryBlogRepository categoryBlogRepository;
    
    public List<CategoryBlog> getAllCategories() {
        return categoryBlogRepository.findAll();
    }
    
    public Optional<CategoryBlog> getCategoryById(Long id) {
        return categoryBlogRepository.findById(id);
    }
    
    public CategoryBlog createCategory(CategoryBlog categoryBlog) {
        return categoryBlogRepository.save(categoryBlog);
    }
    
    public CategoryBlog updateCategory(Long id, CategoryBlog categoryBlog) {
        categoryBlog.setId(id);
        return categoryBlogRepository.save(categoryBlog);
    }
    
    public void deleteCategory(Long id) {
        categoryBlogRepository.deleteById(id);
    }
}