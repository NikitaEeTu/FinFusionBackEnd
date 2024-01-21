package com.finfusion.APS.mapper;

import com.finfusion.APS.dto.AssetDto;
import com.finfusion.APS.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "userEmail", ignore = true)
    AssetDto convertEntityToDto(AssetEntity assetEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    AssetEntity convertDtoToEntity(AssetDto assetDto);
}
