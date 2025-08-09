package com.devwonder.auth_service.client;

import com.devwonder.auth_service.dto.ResellerCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "user-service", url = "http://user-service:8083")
public interface UserServiceClient {
    
    @PostMapping("/user/resellers")
    ResponseEntity<Map<String, Object>> createReseller(@RequestBody ResellerCreateRequest request);
}