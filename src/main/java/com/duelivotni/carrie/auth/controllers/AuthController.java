package com.duelivotni.carrie.auth.controllers;

import com.duelivotni.carrie.auth.models.dtos.SecurityUser;
import com.duelivotni.carrie.auth.models.requests.LoginRequest;
import com.duelivotni.carrie.auth.models.requests.RefreshTokenRequest;
import com.duelivotni.carrie.auth.models.responses.AuthResponse;
import com.duelivotni.carrie.auth.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody @Valid
                                     RefreshTokenRequest request) {
            return authService.refreshToken(request.getRefreshToken());
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", security = @SecurityRequirement(name = "bearerAuth"))
    public SecurityUser user() {
        return authService.currentUser();
    }



}
