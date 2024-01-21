package com.finfusion.APS.controller;

import com.finfusion.APS.dto.UserDto;
import com.finfusion.APS.service.UserRepService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@Data
public class UserController {
    private final UserRepService userRepService;

    @GetMapping("/users/{id}")
    private UserDto getUser(@PathVariable UUID id) throws Exception {
        return userRepService.getUser(id);
    }

    @PostMapping("/users")
    private UserDto saveUser(@RequestBody UserDto userDto) {
        return userRepService.saveUser(userDto);
    }
}
