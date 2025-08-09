package com.devwonder.auth_service.repository;

import com.devwonder.auth_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.name = :name")
    Optional<Role> findByNameWithPermissions(@Param("name") String name);
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.id IN :ids")
    Set<Role> findByIdInWithPermissions(@Param("ids") Set<Long> ids);
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions")
    Set<Role> findAllWithPermissions();
}