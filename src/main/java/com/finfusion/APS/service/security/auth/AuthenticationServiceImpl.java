package com.finfusion.APS.service.security.auth;

import com.finfusion.APS.dto.AuthenticationRequest;
import com.finfusion.APS.dto.AuthenticationResponse;
import com.finfusion.APS.dto.RegisterRequest;
import com.finfusion.APS.entity.UserEntity;
import com.finfusion.APS.repository.UserRepository;
import com.finfusion.APS.service.exception.EmailAlreadyInUseException;
import com.finfusion.APS.service.exception.EntityNotFoundException;
import com.finfusion.APS.service.exception.UserAuthenticationException;
import com.finfusion.APS.service.security.Role;
import com.finfusion.APS.service.security.jwt.JwtService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MeterRegistry meterRegistry;
    private Counter loginCounter;
    private Counter registrationCounter;

    @PostConstruct
    private void counterInit() {
        loginCounter = meterRegistry.counter("authentication.login");
        registrationCounter = meterRegistry.counter("authentication.registration");
    }


    public AuthenticationResponse register(RegisterRequest registerRequest) {
        final String userEmail = registerRequest.getEmail();
        if (userRepository.existsByEmail(userEmail)) {
            log.error("User with email: {} already registered", userEmail);
            throw new EmailAlreadyInUseException("User with this email already registered");
        }
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setName(registerRequest.getFirstName());
        userEntity.setSurname(registerRequest.getLastName());
        userEntity.setAssets(Set.of());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setRole(Role.USER);
        userRepository.save(userEntity);
        registrationCounter.increment();
        return generateAuthenticationResponse(userEntity);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            log.warn("Failed authentication attempt for email: {}", authenticationRequest.getEmail());
            throw new UserAuthenticationException("Your password or email is incorrect. Please try again");
        }
        UserEntity user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Entity User not found"));
        loginCounter.increment();
        return generateAuthenticationResponse(user);
    }

    private AuthenticationResponse generateAuthenticationResponse(UserEntity userEntity) {
        final String token = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
