package com.finfusion.APS.service;

import com.finfusion.APS.dto.UserDto;
import com.finfusion.APS.entity.UserEntity;
import com.finfusion.APS.mapper.UserMapper;
import com.finfusion.APS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRepServiceImpl implements UserRepService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDto getUser(UUID id) throws Exception {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new Exception("Entity not found"));
        return userMapper.convertEntityToDto(userEntity);
    }

    public UserDto saveUser(UserDto entity) {
        UserEntity userEntity = userMapper.convertDtoToEntity(entity);
        return userMapper.convertEntityToDto(userRepository.save(userEntity));
    }

}
