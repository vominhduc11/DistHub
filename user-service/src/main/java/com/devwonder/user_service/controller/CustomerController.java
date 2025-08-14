package com.devwonder.user_service.controller;

import com.devwonder.user_service.dto.CustomerCreateRequest;
import com.devwonder.user_service.dto.CustomerResponse;
import com.devwonder.user_service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        Long customerId = customerService.createCustomer(request);
        
        log.info("Customer created with ID: {} for account: {}", customerId, request.getAccountId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Customer created successfully",
            "customerId", customerId,
            "accountId", request.getAccountId()
        ));
    }
    
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        log.info("Retrieved {} customers", customers.size());
        return ResponseEntity.ok(customers);
    }
}