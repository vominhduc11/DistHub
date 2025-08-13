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

    public Optional<Account> authenticate(String username, String password, String requiredRole) {
        log.debug("Attempting authentication for account: {} with required role: {}", username, requiredRole);
        
        if (username == null || password == null || requiredRole == null || 
            username.trim().isEmpty() || password.trim().isEmpty() || requiredRole.trim().isEmpty()) {
            log.warn("Authentication failed: empty username, password, or role");
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

        // Check if account has the required role
        boolean hasRequiredRole = account.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase(requiredRole));
            
        if (!hasRequiredRole) {
            log.warn("Authentication failed: account {} does not have required role: {}", username, requiredRole);
            return Optional.empty();
        }

        log.info("Authentication successful for account: {} with role: {}", username, requiredRole);
        return Optional.of(account);
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Transactional
    public Map<String, Object> createResellerAccount(ResellerRegistrationRequest request) {
        log.info("Creating reseller account for username: {}", request.getUsername());
        
        validateUniqueUsername(request.getUsername());
        
        try {
            Account account = createAccount(request);
            Long resellerId = createResellerProfile(account.getId(), request);
            sendWelcomeEmail(request);
            sendRegistrationNotification(account.getId(), request.getUsername());
            
            return buildRegistrationResult(account.getId(), resellerId);
        } catch (FeignException e) {
            throw new IllegalArgumentException(extractUserServiceError(e));
        } catch (Exception e) {
            log.error("Error creating reseller account for username: {}", request.getUsername(), e);
            throw new RuntimeException("Failed to create reseller account: " + e.getMessage());
        }
    }
    
    private void validateUniqueUsername(String username) {
        if (accountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
    }
    
    private Account createAccount(ResellerRegistrationRequest request) {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        
        assignDealerRole(account);
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created with ID: {} for username: {}", savedAccount.getId(), request.getUsername());
        return savedAccount;
    }
    
    private void assignDealerRole(Account account) {
        roleRepository.findByName("DEALER")
            .ifPresentOrElse(
                account::addRole,
                () -> log.warn("DEALER role not found, creating account without role")
            );
    }
    
    private Long createResellerProfile(Long accountId, ResellerRegistrationRequest request) {
        ResellerCreateRequest resellerRequest = new ResellerCreateRequest(
            accountId,
            request.getName(),
            request.getAddress(),
            request.getDistrict(),
            request.getCity(),
            request.getPhone(),
            request.getEmail()
        );
        
        ResponseEntity<Map<String, Object>> response = userServiceClient.createReseller(resellerRequest);
        
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to create reseller profile in user service");
        }
        
        return extractResellerId(response.getBody(), accountId);
    }
    
    private Long extractResellerId(Map<String, Object> responseBody, Long accountId) {
        if (!responseBody.containsKey("resellerId")) {
            throw new RuntimeException("Failed to create reseller profile in user service: missing resellerId");
        }
        
        Object resellerIdObj = responseBody.get("resellerId");
        Long resellerId = resellerIdObj instanceof Integer ? 
            ((Integer) resellerIdObj).longValue() : (Long) resellerIdObj;
            
        log.info("Reseller profile created with ID: {} for account: {}", resellerId, accountId);
        return resellerId;
    }
    
    private void sendWelcomeEmail(ResellerRegistrationRequest request) {
        try {
            notificationService.sendResellerWelcomeEmail(
                request.getEmail(),
                request.getName(),
                request.getUsername()
            );
        } catch (Exception e) {
            log.warn("Failed to send welcome email for reseller: {}, but registration succeeded", 
                     request.getUsername(), e);
        }
    }
    
    private void sendRegistrationNotification(Long accountId, String username) {
        try {
            notificationService.sendResellerRegistrationNotification(
                username,  // Send username instead of accountId for principal matching
                username
            );
            log.info("Registration notification sent for account: {} (username: {})", accountId, username);
        } catch (Exception e) {
            log.warn("Failed to send registration notification for reseller: {}, but registration succeeded", 
                     username, e);
        }
    }
    
    private Map<String, Object> buildRegistrationResult(Long accountId, Long resellerId) {
        Map<String, Object> result = new HashMap<>();
        result.put("accountId", accountId);
        result.put("resellerId", resellerId);
        return result;
    }
    
    private String extractUserServiceError(FeignException e) {
        String errorMessage = "Registration failed";
        if (e.contentUTF8() == null || !e.contentUTF8().contains("message")) {
            return errorMessage;
        }
        
        try {
            String content = e.contentUTF8();
            if (content.contains("Phone number already exists")) {
                return "Phone number already exists";
            }
            if (content.contains("Email already exists")) {
                return "Email already exists";
            }
            if (content.contains("already exists")) {
                int start = content.indexOf("message\":\"") + 10;
                int end = content.indexOf("\"", start);
                if (start > 9 && end > start) {
                    return content.substring(start, end);
                }
            }
        } catch (Exception parseException) {
            log.warn("Failed to parse error message from UserService", parseException);
        }
        
        return errorMessage;
    }
}