package com.finfusion.APS.service.security.auth;

import com.finfusion.APS.dto.AuthenticationRequest;
import com.finfusion.APS.dto.AuthenticationResponse;
import com.finfusion.APS.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
