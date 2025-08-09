package com.devwonder.user_service.controller;

import com.devwonder.user_service.dto.ResellerCreateRequest;
import com.devwonder.user_service.dto.ResellerResponse;
import com.devwonder.user_service.service.ResellerService;
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
public class ResellerController {
    
    private final ResellerService resellerService;

    @PostMapping("/resellers")
    public ResponseEntity<?> createReseller(@Valid @RequestBody ResellerCreateRequest request) {
        log.info("Creating reseller for account ID: {}", request.getAccountId());
        
        Long resellerId = resellerService.createReseller(request);
        
        log.info("Reseller created successfully with ID: {} for account: {}", resellerId, request.getAccountId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Reseller created successfully",
            "resellerId", resellerId,
            "accountId", request.getAccountId()
        ));
    }
    
    @GetMapping("/resellers")
    public ResponseEntity<List<ResellerResponse>> getAllResellers() {
        log.info("Fetching all resellers");
        
        List<ResellerResponse> resellers = resellerService.getAllResellers();
        
        log.info("Found {} resellers", resellers.size());
        return ResponseEntity.ok(resellers);
    }
}