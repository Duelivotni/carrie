package com.duelivotni.carrie.auth.models.responses;

import com.art.oko.modules.user.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String refreshToken;
    private String email;
    private String role;

    public AuthResponse(User user, String token, String refreshToken) {
        setEmail(user.getEmail());
        setRole(user.getRole().toString());
        setRefreshToken(refreshToken);
        setToken(token);
    }
}
