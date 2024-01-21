package com.finfusion.APS.controller;

import com.finfusion.APS.dto.AssetDto;
import com.finfusion.APS.dto.AssetUpdateDto;
import com.finfusion.APS.service.AssetRepService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/assets")
@Data
public class AssetController {
    private final AssetRepService assetRepService;

    @GetMapping("/{id}")
    private AssetDto getAsset(@PathVariable UUID id) throws Exception {
        return assetRepService.getAsset(id);
    }

    @PostMapping("/")
    private AssetDto saveAsset(@RequestBody AssetDto assetDto) {
        return assetRepService.saveAsset(assetDto);
    }

    @DeleteMapping("/{id}")
    private AssetDto deleteAsset(@PathVariable UUID id) throws Exception {
        return assetRepService.deleteAsset(id);
    }

    @PutMapping("/{id}")
    private AssetDto updateAsset(@PathVariable UUID id, @RequestBody AssetUpdateDto assetUpdateDto) throws Exception {
        return assetRepService.updateAsset(id, assetUpdateDto);
    }

}
