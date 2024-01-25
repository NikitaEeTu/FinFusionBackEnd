package com.finfusion.APS.service;

import com.finfusion.APS.dto.UserDto;
import com.finfusion.APS.entity.UserEntity;
import com.finfusion.APS.mapper.UserMapper;
import com.finfusion.APS.repository.UserRepository;
import com.finfusion.APS.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRepServiceImpl implements UserRepService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUser(UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return userMapper.convertEntityToDto(userEntity);
    }

    public UserDto saveUser(UserDto entity) {
        UserEntity userEntity = userMapper.convertDtoToEntity(entity);
        return userMapper.convertEntityToDto(userRepository.save(userEntity));
    }

}
