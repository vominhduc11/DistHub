package com.devwonder.auth_service.service;

import com.devwonder.auth_service.client.UserServiceClient;
import com.devwonder.auth_service.dto.ResellerCreateRequest;
import com.devwonder.auth_service.dto.ResellerRegistrationRequest;
import com.devwonder.auth_service.model.Account;
import com.devwonder.auth_service.model.Role;
import com.devwonder.auth_service.repository.AccountRepository;
import com.devwonder.auth_service.repository.RoleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public Optional<Account> authenticate(String username, String password) {
        log.debug("Attempting authentication for account: {}", username);
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            log.warn("Authentication failed: empty username or password");
            return Optional.empty();
        }

        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) {
            log.warn("Authentication failed: account not found: {}", username);
            return Optional.empty();
        }

        Account account = accountOpt.get();

        if (!passwordEncoder.matches(password, account.getPassword())) {
            log.warn("Authentication failed: invalid password for account: {}", username);
            return Optional.empty();
        }

        log.info("Authentication successful for account: {}", username);
        return Optional.of(account);
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public void createAccount(Account account) {
        if (existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException("Account already exists: " + account.getUsername());
        }
        
        // Encode password before saving
        if (account.getPassword() != null && !account.getPassword().startsWith("$2a$")) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        
        accountRepository.save(account);
        log.info("Account created successfully: {}", account.getUsername());
    }

    public void assignRoleToAccount(String username, String roleName) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        
        if (accountOpt.isPresent() && roleOpt.isPresent()) {
            Account account = accountOpt.get();
            Role role = roleOpt.get();
            account.addRole(role);
            accountRepository.save(account);
            log.info("Role {} assigned to account: {}", roleName, username);
        } else {
            log.warn("Could not assign role {} to account {}: account or role not found", roleName, username);
        }
    }

    @Transactional
    public Map<String, Object> createResellerAccount(ResellerRegistrationRequest registrationRequest) {
        log.info("Creating reseller account for username: {}", registrationRequest.getUsername());
        
        // Check for duplicate username
        if (accountRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + registrationRequest.getUsername());
        }
        
        try {
            // Create Account with username and password only
            Account account = new Account();
            account.setUsername(registrationRequest.getUsername());
            account.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            
            // Add DEALER role (resellers are a type of dealer)
            Optional<Role> dealerRole = roleRepository.findByName("DEALER");
            if (dealerRole.isPresent()) {
                account.addRole(dealerRole.get());
            } else {
                log.warn("DEALER role not found, creating account without role");
            }
            
            Account savedAccount = accountRepository.save(account);
            log.info("Account created with ID: {} for username: {}", savedAccount.getId(), registrationRequest.getUsername());
            
            // Create Reseller profile via User Service (without username/password)
            ResellerCreateRequest resellerRequest = new ResellerCreateRequest(
                savedAccount.getId(),
                registrationRequest.getName(),
                registrationRequest.getAddress(),
                registrationRequest.getDistrict(),
                registrationRequest.getCity(),
                registrationRequest.getPhone(),
                registrationRequest.getEmail()
            );
            
            ResponseEntity<Map<String, Object>> resellerResponse = userServiceClient.createReseller(resellerRequest);
            
            if (resellerResponse.getStatusCode().is2xxSuccessful() && resellerResponse.getBody() != null) {
                Map<String, Object> body = resellerResponse.getBody();
                if (body == null || !body.containsKey("resellerId")) {
                    throw new RuntimeException("Failed to create reseller profile in user service: missing resellerId");
                }
                Object resellerIdObj = body.get("resellerId");
                Long resellerId = resellerIdObj instanceof Integer ? ((Integer) resellerIdObj).longValue() : (Long) resellerIdObj;
                log.info("Reseller profile created with ID: {} for account: {}", resellerId, savedAccount.getId());
                
                // Send welcome email notification asynchronously
                try {
                    notificationService.sendResellerWelcomeEmail(
                        registrationRequest.getEmail(),
                        registrationRequest.getName(),
                        registrationRequest.getUsername()
                    );
                } catch (Exception emailError) {
                    log.warn("Failed to send welcome email for reseller: {}, but registration succeeded", 
                             registrationRequest.getUsername(), emailError);
                }
                
                Map<String, Object> result = new HashMap<>();
                result.put("accountId", savedAccount.getId());
                result.put("resellerId", resellerId);
                return result;
            } else {
                throw new RuntimeException("Failed to create reseller profile in user service");
            }
            
        } catch (FeignException e) {
            // Extract error message from UserService response
            String errorMessage = "Registration failed";
            if (e.contentUTF8() != null && e.contentUTF8().contains("message")) {
                try {
                    String content = e.contentUTF8();
                    if (content.contains("Phone number already exists")) {
                        errorMessage = "Phone number already exists";
                    } else if (content.contains("Email already exists")) {
                        errorMessage = "Email already exists";  
                    } else if (content.contains("already exists")) {
                        errorMessage = content.substring(content.indexOf("message\":\"") + 10, content.indexOf("\"", content.indexOf("message\":\"") + 10));
                    }
                } catch (Exception parseException) {
                    log.warn("Failed to parse error message from UserService", parseException);
                }
            }
            
            log.error("UserService error creating reseller for username: {}, error: {}", registrationRequest.getUsername(), errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } catch (Exception e) {
            log.error("Error creating reseller account for username: {}", registrationRequest.getUsername(), e);
            throw new RuntimeException("Failed to create reseller account: " + e.getMessage());
        }
    }
}