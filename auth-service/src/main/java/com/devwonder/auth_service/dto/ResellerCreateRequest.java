package com.devwonder.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResellerCreateRequest {
    private Long accountId;
    private String name;
    private String address;
    private String district;
    private String city;
    private String phone;
    private String email;
}