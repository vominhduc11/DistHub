package com.devwonder.content_management_service.repository;

import com.devwonder.content_management_service.model.CategoryBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryBlogRepository extends JpaRepository<CategoryBlog, Long> {
}