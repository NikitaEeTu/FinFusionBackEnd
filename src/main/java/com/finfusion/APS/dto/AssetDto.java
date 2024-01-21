package com.finfusion.APS.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetDto {
    private String name;
    private BigDecimal amount;
    private Type type;
    private CryptoActivityType cryptoActivityType;
    private String userEmail;
}
