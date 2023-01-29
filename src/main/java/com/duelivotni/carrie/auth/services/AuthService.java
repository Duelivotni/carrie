package com.duelivotni.carrie.auth.services;

import com.duelivotni.carrie.auth.models.dtos.SecurityUser;
import com.duelivotni.carrie.auth.models.responses.AuthResponse;
import com.duelivotni.carrie.user.models.entities.User;

public interface AuthService {

    AuthResponse login(String email, String password);

    SecurityUser currentUser();

    User user();

    AuthResponse refreshToken(String refreshToken);
}
