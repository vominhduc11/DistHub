package com.devwonder.user_service.config;

import com.devwonder.user_service.model.Admin;
import com.devwonder.user_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdminRecord();
    }

    private void createDefaultAdminRecord() {
        // Assuming the default admin account will have ID = 1 in auth-service
        // This should match the account created in auth-service
        Long defaultAdminAccountId = 1L;

        // Check if admin record already exists
        if (adminRepository.findByAccountId(defaultAdminAccountId).isPresent()) {
            log.info("Default admin record already exists for accountId: {}", defaultAdminAccountId);
            return;
        }

        // Create admin record
        Admin adminRecord = new Admin();
        adminRecord.setAccountId(defaultAdminAccountId);
        adminRecord.setCreatedAt(LocalDateTime.now());
        adminRecord.setUpdatedAt(LocalDateTime.now());

        adminRepository.save(adminRecord);
        log.info("Default admin record created successfully for accountId: {}", defaultAdminAccountId);
    }
}