package com.duelivotni.carrie.auth.services.impl;

import com.duelivotni.carrie.auth.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl
        implements PasswordService {

    private final BCryptPasswordEncoder encoder;

    @Override
    public boolean matches(
            String encodedPassword,
            String password) {
        return encoder.matches(password, encodedPassword);
    }

    @Override
    public String encodePassword(String password) {
        return encoder.encode(password);
    }
}
