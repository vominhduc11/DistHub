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
        Long resellerId = resellerService.createReseller(request);
        
        log.info("Reseller created with ID: {} for account: {}", resellerId, request.getAccountId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Reseller created successfully",
            "resellerId", resellerId,
            "accountId", request.getAccountId()
        ));
    }
    
    @GetMapping("/resellers")
    public ResponseEntity<List<ResellerResponse>> getAllResellers() {
        List<ResellerResponse> resellers = resellerService.getAllResellers();
        log.info("Retrieved {} resellers", resellers.size());
        return ResponseEntity.ok(resellers);
    }
}