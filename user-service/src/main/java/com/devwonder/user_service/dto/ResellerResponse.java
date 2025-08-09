package com.devwonder.user_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResellerResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String district;
    private String phone;
    private String email;
}