package com.duelivotni.carrie.auth.services;

import com.duelivotni.carrie.auth.models.dtos.SecurityUser;
import com.duelivotni.carrie.auth.models.enums.TokenType;
import io.jsonwebtoken.Claims;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface TokenService {

    String generateAccessToken(UUID id, String email, String role, @Nullable String location);

    String generateRefreshToken(String email);

    String getEmailFromRefreshToken(String refreshToken);

    Claims parse(String token);

    String email(String token);

    Long id(String token);

    String role(String token);

    TokenType tokenType(String token);

    Optional<String> location(String token);

    String email(Claims claims);

    Long id(Claims claims);

    String role(Claims claims);

    TokenType tokenType(Claims claims);

    Optional<String> location(Claims claims);

    SecurityUser securityUser(String token);

}
