package com.devwonder.user_service.service;

import com.devwonder.user_service.dto.ResellerCreateRequest;
import com.devwonder.user_service.dto.ResellerResponse;
import com.devwonder.user_service.model.Reseller;
import com.devwonder.user_service.repository.ResellerRepository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResellerService {
    
    private final ResellerRepository resellerRepository;

    @Transactional
    public Long createReseller(ResellerCreateRequest request) {
        if (resellerRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Reseller already exists for account ID: " + request.getAccountId());
        }
        
        if (resellerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        if (resellerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists: " + request.getPhone());
        }
        
        Reseller reseller = new Reseller();
        reseller.setAccountId(request.getAccountId());
        reseller.setName(request.getName());
        reseller.setAddress(request.getAddress());
        reseller.setDistrict(request.getDistrict());
        reseller.setCity(request.getCity());
        reseller.setPhone(request.getPhone());
        reseller.setEmail(request.getEmail());
        
        Reseller savedReseller = resellerRepository.save(reseller);
        return savedReseller.getAccountId();
    }
    
    public List<ResellerResponse> getAllResellers() {
        List<Reseller> resellers = resellerRepository.findAll();
        return resellers.stream()
            .map(this::mapToResellerResponse)
            .collect(Collectors.toList());
    }
    
    private ResellerResponse mapToResellerResponse(Reseller reseller) {
        return new ResellerResponse(
            reseller.getAccountId(),
            reseller.getName(),
            reseller.getAddress(),
            reseller.getCity(),
            reseller.getDistrict(),
            reseller.getPhone(),
            reseller.getEmail()
        );
    }
}