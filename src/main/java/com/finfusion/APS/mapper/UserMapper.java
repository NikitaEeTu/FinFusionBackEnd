package com.finfusion.APS.mapper;

import com.finfusion.APS.dto.UserDto;
import com.finfusion.APS.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto convertEntityToDto(UserEntity userEntity);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity convertDtoToEntity(UserDto userDto);
}
