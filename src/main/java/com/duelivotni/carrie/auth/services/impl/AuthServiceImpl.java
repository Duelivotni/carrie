package com.duelivotni.carrie.auth.services.impl;

//import com.art.oko.core.constants.Messages;
//import com.art.oko.core.services.Exceptional;
//import com.art.oko.modules.auth.models.dtos.SecurityUser;
//import com.art.oko.modules.auth.models.enums.TokenType;
//import com.art.oko.modules.auth.models.responses.AuthResponse;
//import com.art.oko.modules.auth.services.AuthService;
//import com.art.oko.modules.auth.services.PasswordService;
//import com.art.oko.modules.auth.services.TokenService;
//import com.art.oko.modules.user.models.entities.User;
//import com.art.oko.modules.user.services.UserService;
import com.duelivotni.carrie.auth.models.dtos.SecurityUser;
import com.duelivotni.carrie.auth.models.enums.TokenType;
import com.duelivotni.carrie.auth.models.responses.AuthResponse;
import com.duelivotni.carrie.auth.services.AuthService;
import com.duelivotni.carrie.auth.services.PasswordService;
import com.duelivotni.carrie.auth.services.TokenService;
import com.duelivotni.carrie.constants.Messages;
import com.duelivotni.carrie.exception.Exceptional;
import com.duelivotni.carrie.user.models.entities.User;
import com.duelivotni.carrie.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.duelivotni.carrie.constants.Messages.TOKEN_INVALID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl
        implements AuthService, Exceptional {

    private final UserService userService;
    private final PasswordService passwordService;

    private final TokenService tokenService;

    @Override
    public AuthResponse login(
            String email,
            String password) {

        var user = userService.findByEmail(email)
                .filter(e -> passwordService.matches(e.getPassword(), password))
                .orElseThrow(() -> bad(Messages.INVALID_CREDENTIALS));
        return authResponse(user);
    }

    @Override
    public SecurityUser currentUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User user() {
        return userService.getById(currentUser().getId());
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String email = tokenService.getEmailFromRefreshToken(refreshToken);

        var type = tokenService.tokenType(refreshToken);

        if (!Objects.equals(type, TokenType.REFRESH)) {
            throw bad(TOKEN_INVALID);
        }

        var user = userService.findByEmail(email).orElseThrow(() -> bad(TOKEN_INVALID));

        return authResponse(user);
    }

    private AuthResponse authResponse(User user) {
        String token = tokenService.generateAccessToken(user.getId(), user.getEmail(), user.getRole().toString(), null);
        String refreshToken = tokenService.generateRefreshToken(user.getEmail());

        return new AuthResponse(user, token, refreshToken);
    }
}
