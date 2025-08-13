package com.devwonder.content_management_service.repository;

import com.devwonder.content_management_service.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByCategoryBlogId(Long categoryBlogId);
}