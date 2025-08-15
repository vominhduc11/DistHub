package com.devwonder.user_service.config;

import com.devwonder.user_service.model.Admin;
import com.devwonder.user_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3)
public class DefaultDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final JdbcTemplate jdbcTemplate;
    
    @Value("${spring.datasource.auth-url:jdbc:postgresql://dishub_postgres:5432/auth_db}")
    private String authDbUrl;

    @Override
    public void run(String... args) throws Exception {
        // Wait a bit to ensure auth-service has created admin account
        Thread.sleep(2000);
        createDefaultAdminRecord();
    }

    private void createDefaultAdminRecord() {
        try {
            // Get the actual admin account ID by querying auth database directly
            Long actualAdminAccountId = getAdminAccountIdFromDatabase();
            
            if (actualAdminAccountId == null) {
                log.warn("Could not find admin account in auth database. Skipping admin record creation.");
                return;
            }

            // Check if admin record already exists
            if (adminRepository.findByAccountId(actualAdminAccountId).isPresent()) {
                log.info("Default admin record already exists for accountId: {}", actualAdminAccountId);
                return;
            }

            // Create admin record
            Admin adminRecord = new Admin();
            adminRecord.setAccountId(actualAdminAccountId);
            adminRecord.setCreatedAt(LocalDateTime.now());
            adminRecord.setUpdatedAt(LocalDateTime.now());

            adminRepository.save(adminRecord);
            log.info("Default admin record created successfully for accountId: {}", actualAdminAccountId);
            
        } catch (Exception e) {
            log.error("Failed to create default admin record: {}", e.getMessage());
        }
    }
    
    private Long getAdminAccountIdFromDatabase() {
        try {
            // Create a separate JDBC template for auth database
            org.springframework.jdbc.datasource.DriverManagerDataSource authDataSource = 
                new org.springframework.jdbc.datasource.DriverManagerDataSource();
            authDataSource.setUrl("jdbc:postgresql://dishub_postgres:5432/auth_db");
            authDataSource.setUsername("voduc");
            authDataSource.setPassword("voduc123");
            authDataSource.setDriverClassName("org.postgresql.Driver");
            
            JdbcTemplate authJdbcTemplate = new JdbcTemplate(authDataSource);
            
            // First, find the admin account
            String accountSql = "SELECT id FROM accounts WHERE username = 'admin' ORDER BY created_at DESC LIMIT 1";
            List<Map<String, Object>> accountResults = authJdbcTemplate.queryForList(accountSql);
            
            if (!accountResults.isEmpty()) {
                Object idObj = accountResults.get(0).get("id");
                if (idObj instanceof Number) {
                    Long accountId = ((Number) idObj).longValue();
                    log.info("Found admin account with ID: {}", accountId);
                    return accountId;
                }
            } else {
                log.warn("No admin account found in auth database");
            }
            
        } catch (Exception e) {
            log.error("Error fetching admin account ID from auth database: {}", e.getMessage(), e);
        }
        
        log.warn("Falling back to default account ID = 1");
        return 1L; // Fallback to prevent errors
    }
}