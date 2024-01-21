package com.finfusion.APS.service.security.auth;

import com.finfusion.APS.dto.AuthenticationRequest;
import com.finfusion.APS.dto.AuthenticationResponse;
import com.finfusion.APS.dto.RegisterRequest;
import com.finfusion.APS.entity.UserEntity;
import com.finfusion.APS.repository.UserRepository;
import com.finfusion.APS.service.security.Role;
import com.finfusion.APS.service.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setName(registerRequest.getFirstName());
        userEntity.setSurname(registerRequest.getLastName());
        userEntity.setAssets(Set.of());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setRole(Role.USER);
        userRepository.save(userEntity);

        return generateAuthenticationResponse(userEntity);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserEntity user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User isn't authenticated"));
        return generateAuthenticationResponse(user);
    }

    private AuthenticationResponse generateAuthenticationResponse(UserEntity userEntity) {
        final String token = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
