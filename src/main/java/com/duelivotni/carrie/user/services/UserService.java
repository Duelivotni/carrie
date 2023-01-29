package com.duelivotni.carrie.user.services;

import com.duelivotni.carrie.user.models.entities.User;
import com.duelivotni.carrie.user.models.enums.Role;
import com.duelivotni.carrie.user.models.requests.UserPasswordRequest;
import com.duelivotni.carrie.user.models.requests.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerAdmin(
            String email,
            String password,
            String location);

    User registerManager(
            String email,
            String password,
            String location);

    User registerUser(
            String email,
            String password,
            String location,
            Role role);

    boolean isEmailAlreadyTaken(String email);
    User save(User user);
    Optional<User> findByEmail(String email);
    void deleteUserByEmail(String email);
    List<User> findAll();
    User updateUser(Long id, UserRequest request);

    User getById(Long id);
    User getByEmail(String email);
    void deleteUserById(Long id);
    void resetPassword(Long id, UserPasswordRequest request);

}
