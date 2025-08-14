package com.devwonder.auth_service.config;

import com.devwonder.auth_service.model.Account;
import com.devwonder.auth_service.model.Role;
import com.devwonder.auth_service.repository.AccountRepository;
import com.devwonder.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class DefaultDataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdminAccount();
    }

    private void createDefaultAdminAccount() {
        // Check if admin account already exists
        if (accountRepository.findByUsername("admin").isPresent()) {
            log.info("Default admin account already exists");
            return;
        }

        // Create default admin account first (without roles)
        Account adminAccount = new Account();
        adminAccount.setUsername("admin");
        adminAccount.setPassword(passwordEncoder.encode("admin123456"));
        adminAccount.setEnabled(true);
        adminAccount.setCreatedAt(LocalDateTime.now());
        adminAccount.setUpdatedAt(LocalDateTime.now());

        // Save account first
        Account savedAccount = accountRepository.save(adminAccount);
        
        // Then add role relationship using saved account
        Role adminRole = roleRepository.findByName("ADMIN")
            .orElseThrow(() -> new RuntimeException("ADMIN role not found. Make sure roles are initialized first."));
        
        savedAccount.getRoles().add(adminRole);
        accountRepository.save(savedAccount);
        
        log.info("Default admin account created successfully - Username: admin, Password: admin123456");
    }
}