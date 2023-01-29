package com.duelivotni.carrie.auth.services;

public interface PasswordService {

    boolean matches(String encodedPassword, String password);
    String encodePassword(String password);
}
