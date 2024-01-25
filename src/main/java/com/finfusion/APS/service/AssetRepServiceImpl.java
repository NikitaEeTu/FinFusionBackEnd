package com.finfusion.APS.service;

import com.finfusion.APS.dto.Action;
import com.finfusion.APS.dto.AssetDto;
import com.finfusion.APS.dto.AssetUpdateDto;
import com.finfusion.APS.entity.AssetEntity;
import com.finfusion.APS.entity.UserEntity;
import com.finfusion.APS.mapper.AssetMapper;
import com.finfusion.APS.repository.AssetRepository;
import com.finfusion.APS.repository.UserRepository;
import com.finfusion.APS.service.exception.AssetUpdateException;
import com.finfusion.APS.service.exception.EntityNotFoundException;
import com.finfusion.APS.service.exception.EntitySaveException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetRepServiceImpl implements AssetRepService {
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssetMapper assetMapper;

    private static BigDecimal updateAmount(AssetUpdateDto assetUpdateDto, AssetEntity assetEntity) {
        final BigDecimal updateAmount = assetUpdateDto.getAmount();
        BigDecimal amount = assetEntity.getAmount();
        if (assetUpdateDto.getAction() == Action.ADD) {
            amount = amount.add(updateAmount);
        } else if (assetUpdateDto.getAction() == Action.SUBTRACT) {
            if (updateAmount.compareTo(amount) > 0) {
                throw new AssetUpdateException("The updated amount is greater than asset amount");
            }
            amount = amount.subtract(updateAmount);
        }
        return amount;
    }

    public AssetDto getAsset(UUID id) {
        AssetEntity assetEntity = assetRepository.findById(id).orElseThrow(() -> {
            logAssetNotFoundMsg(id);
            return new EntityNotFoundException("Entity Asset not found");
        });
        return assetMapper.convertEntityToDto(assetEntity);
    }

    public AssetDto saveAsset(AssetDto assetDto) {
        UserEntity user = userRepository.findByEmail(assetDto.getUserEmail())
                .orElseThrow(() -> {
                    log.error("User with email: {} not found", assetDto.getUserEmail());
                    return new EntityNotFoundException("Entity User not found");
                });
        AssetEntity assetEntity = assetMapper.convertDtoToEntity(assetDto);
        assetEntity.setUser(user);
        try {
            AssetEntity savedAsset = assetRepository.save(assetEntity);
            return assetMapper.convertEntityToDto(savedAsset);
        } catch (DataIntegrityViolationException ex) {
            log.error("Error saving asset: {}", ex.getMessage());
            throw new EntitySaveException("Failed to save asset");
        }
    }

    @Override
    public AssetDto deleteAsset(UUID id) {
        AssetEntity assetEntity = assetRepository.findById(id).orElseThrow(() -> {
            logAssetNotFoundMsg(id);
            return new EntityNotFoundException("Entity Asset not found");
        });
        assetRepository.delete(assetEntity);
        return assetMapper.convertEntityToDto(assetEntity);
    }

    @Override
    public AssetDto updateAsset(UUID id, AssetUpdateDto assetUpdateDto) {
        final AssetEntity assetEntity = assetRepository.findById(id).orElseThrow(() -> {
            logAssetNotFoundMsg(id);
            return new EntityNotFoundException("Entity Asset not found");
        });
        BigDecimal amount = updateAmount(assetUpdateDto, assetEntity);
        assetEntity.setAmount(amount);
        AssetEntity updatedAsset = assetRepository.save(assetEntity);
        return assetMapper.convertEntityToDto(updatedAsset);
    }

    private void logAssetNotFoundMsg(UUID id) {
        log.warn("Asset with id: {} not found", id);
    }
}
