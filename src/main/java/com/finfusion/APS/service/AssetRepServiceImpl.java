package com.finfusion.APS.service;

import com.finfusion.APS.dto.Action;
import com.finfusion.APS.dto.AssetDto;
import com.finfusion.APS.dto.AssetUpdateDto;
import com.finfusion.APS.entity.AssetEntity;
import com.finfusion.APS.entity.UserEntity;
import com.finfusion.APS.mapper.AssetMapper;
import com.finfusion.APS.repository.AssetRepository;
import com.finfusion.APS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AssetRepServiceImpl implements AssetRepService {
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssetMapper assetMapper;

    public AssetDto getAsset(UUID id) throws Exception {
        AssetEntity assetEntity = assetRepository.findById(id).orElseThrow(() -> new Exception("Entity not found"));
        return assetMapper.convertEntityToDto(assetEntity);
    }

    public AssetDto saveAsset(AssetDto assetDto) {
        UserEntity user = userRepository.findByEmail(assetDto.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AssetEntity assetEntity = assetMapper.convertDtoToEntity(assetDto);
        assetEntity.setUser(user);
        return assetMapper.convertEntityToDto(assetRepository.save(assetEntity));
    }

    @Override
    public AssetDto deleteAsset(UUID id) throws Exception {
        AssetEntity assetEntity = assetRepository.findById(id).orElseThrow(() -> new Exception("Entity not found"));
        assetRepository.delete(assetEntity);
        return assetMapper.convertEntityToDto(assetEntity);
    }

    @Override
    public AssetDto updateAsset(UUID id, AssetUpdateDto assetUpdateDto) throws Exception {
        final AssetEntity assetEntity = assetRepository.findById(id).orElseThrow(() -> new Exception("Entity not found"));
        final BigDecimal updateAmount = assetUpdateDto.getAmount();
        BigDecimal amount = assetEntity.getAmount();
        if (assetUpdateDto.getAction() == Action.ADD) {
            amount = amount.add(updateAmount);
        } else if (assetUpdateDto.getAction() == Action.SUBTRACT && updateAmount.compareTo(amount) < 0) {
            amount = amount.subtract(updateAmount);
        }
        assetEntity.setAmount(amount);
        AssetEntity updatedAsset = assetRepository.save(assetEntity);
        return assetMapper.convertEntityToDto(updatedAsset);
    }
}
