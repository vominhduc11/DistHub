package com.devwonder.auth_service.repository;

import com.devwonder.auth_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Permission> findByResource(String resource);
    
    List<Permission> findByAction(String action);
    
    List<Permission> findByResourceAndAction(String resource, String action);
    
    @Query("SELECT p FROM Permission p JOIN FETCH p.roles WHERE p.name = :name")
    Optional<Permission> findByNameWithRoles(@Param("name") String name);
    
    @Query("SELECT p FROM Permission p JOIN FETCH p.roles WHERE p.id IN :ids")
    Set<Permission> findByIdInWithRoles(@Param("ids") Set<Long> ids);
    
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r WHERE r.name IN :roleNames")
    Set<Permission> findByRoleNames(@Param("roleNames") Set<String> roleNames);
}