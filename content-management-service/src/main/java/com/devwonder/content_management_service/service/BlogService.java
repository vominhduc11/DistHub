package com.devwonder.content_management_service.service;

import com.devwonder.content_management_service.model.Blog;
import com.devwonder.content_management_service.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    
    private final BlogRepository blogRepository;
    
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }
    
    public List<Blog> getBlogsByCategoryId(Long categoryId) {
        return blogRepository.findByCategoryBlogId(categoryId);
    }
    
    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }
    
    public Blog updateBlog(Long id, Blog blog) {
        blog.setId(id);
        return blogRepository.save(blog);
    }
    
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}