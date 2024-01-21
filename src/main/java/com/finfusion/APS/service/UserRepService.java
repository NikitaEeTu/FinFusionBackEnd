package com.finfusion.APS.service;

import com.finfusion.APS.dto.UserDto;

import java.util.UUID;

public interface UserRepService {
    UserDto getUser(UUID id) throws Exception;

    UserDto saveUser(UserDto userDto);
}
