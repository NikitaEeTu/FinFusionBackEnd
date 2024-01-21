package com.finfusion.APS.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetUpdateDto {
    private Action action;
    private BigDecimal amount;
}
