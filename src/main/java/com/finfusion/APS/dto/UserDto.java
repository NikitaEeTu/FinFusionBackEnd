package com.finfusion.APS.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String surname;
    private List<AssetDto> assets;
}
