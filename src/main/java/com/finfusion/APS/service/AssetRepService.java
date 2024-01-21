package com.finfusion.APS.service;

import com.finfusion.APS.dto.AssetDto;
import com.finfusion.APS.dto.AssetUpdateDto;

import java.util.UUID;

public interface AssetRepService {
    AssetDto getAsset(UUID id) throws Exception;

    AssetDto saveAsset(AssetDto assetDto);

    AssetDto deleteAsset(UUID id) throws Exception;

    AssetDto updateAsset(UUID id, AssetUpdateDto assetUpdateDto) throws Exception;
}
