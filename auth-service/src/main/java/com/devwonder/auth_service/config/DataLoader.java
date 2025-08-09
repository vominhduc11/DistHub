package com.devwonder.auth_service.config;

import com.devwonder.auth_service.model.Role;
import com.devwonder.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
    }

    private void loadRoles() {
        log.info("Loading roles...");

        if (!roleRepository.existsByName("CUSTOMER")) {
            Role customerRole = new Role("CUSTOMER");
            roleRepository.save(customerRole);
            log.info("Created CUSTOMER role");
        }

        if (!roleRepository.existsByName("DEALER")) {
            Role dealerRole = new Role("DEALER");
            roleRepository.save(dealerRole);
            log.info("Created DEALER role");
        }

        if (!roleRepository.existsByName("ADMIN")) {
            Role adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
            log.info("Created ADMIN role");
        }

        log.info("Roles loading completed.");
    }
}