package com.devwonder.auth_service.controller;

import com.devwonder.auth_service.dto.LoginRequest;
import com.devwonder.auth_service.dto.LoginResponse;
import com.devwonder.auth_service.dto.ResellerRegistrationRequest;
import com.devwonder.auth_service.model.Account;
import com.devwonder.auth_service.service.AccountService;
import com.devwonder.auth_service.util.JwtUtil;
import com.devwonder.auth_service.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String clientIp = RequestUtil.getClientIpAddress(request);
        log.info("Login attempt for user: {} from IP: {}", loginRequest.getUsername(), clientIp);
        
        Optional<Account> accountOpt = accountService.authenticate(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getRole());
        
        if (accountOpt.isEmpty()) {
            log.warn("Login failed for account: {} from IP: {}", loginRequest.getUsername(), clientIp);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials", "timestamp", System.currentTimeMillis()));
        }
        
        Account account = accountOpt.get();
        List<String> roles = account.getRoles().stream()
            .map(role -> role.getName())
            .toList();
        if (roles.isEmpty()) {
            roles = List.of("CUSTOMER");
        }
        String token = jwtUtil.generateToken(account.getUsername(), roles);
        
        // Create comprehensive response with account type information
        String primaryRole = roles.get(0); // First role as primary
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(account.getUsername());
        response.setRole(primaryRole);
        response.setAccountType(primaryRole); // Use primary role as account type
        response.setAccountId(account.getId());
        
        log.info("Login successful for account: {} (roles: {}) from IP: {}", 
            account.getUsername(), roles, clientIp);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token);
        
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "username", username,
                "roles", roles,
                "timestamp", System.currentTimeMillis()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "error", "Invalid or expired token"));
        }
    }

    @PostMapping("/register-reseller")
    public ResponseEntity<?> registerReseller(@Valid @RequestBody ResellerRegistrationRequest request, 
                                            HttpServletRequest httpRequest) {
        String clientIp = RequestUtil.getClientIpAddress(httpRequest);
        log.info("Reseller registration attempt from IP: {}", clientIp);
        
        Map<String, Object> result = accountService.createResellerAccount(request);
        
        log.info("Reseller registration successful for: {} from IP: {}", request.getEmail(), clientIp);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Reseller registration successful",
            "accountId", result.get("accountId"),
            "resellerId", result.get("resellerId"),
            "timestamp", System.currentTimeMillis()
        ));
    }
}
