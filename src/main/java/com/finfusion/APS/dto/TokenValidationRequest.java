package com.finfusion.APS.dto;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String email;
    private String token;
}