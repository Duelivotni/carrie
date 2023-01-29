package com.duelivotni.carrie.user.services.impl;

import com.duelivotni.carrie.auth.services.PasswordService;
import com.duelivotni.carrie.exception.Exceptional;
import com.duelivotni.carrie.user.models.entities.User;
import com.duelivotni.carrie.user.models.enums.Role;
import com.duelivotni.carrie.user.repositories.UserRepository;
import com.duelivotni.carrie.user.services.UserService;
import com.duelivotni.carrie.user.models.requests.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.duelivotni.carrie.constants.Messages.ITEM_NOT_FOUND;
import static com.duelivotni.carrie.constants.Messages.UNAVAILABLE_EMAIL;


@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService, UserDetailsService, Exceptional {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    public User registerAdmin(
            String email,
            String password,
            String location) {
        return register(email.toLowerCase(Locale.ROOT), password, location, Role.ROLE_ADMIN);
    }

    @Override
    public User registerManager(
            String email,
            String password,
            String location) {
        return register(email, password, location, Role.ROLE_MANAGER);
    }

    @Override
    public User registerUser(
            String email,
            String password,
            String location,
            Role role) {
        return register(email, password, location, role);
    }

    private User register(
            String email,
            String password,
            String location,
            Role role) {
        if (isEmailAlreadyTaken(email)) {
            throw bad(UNAVAILABLE_EMAIL);
        }
        String encodePassword = passwordService.encodePassword(password);
        return save(buildUser(email, encodePassword, location, role));
    }

    private User buildUser(
            String email,
            String password,
            String location,
            Role role) {

        return User.builder().password(password).email(email).location(location).role(role).build();
    }

    @Override
    public boolean isEmailAlreadyTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username).orElse(null);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase(Locale.ROOT));
    }

    @Override
    public void deleteUserByEmail(String email) {
        findByEmail(email).ifPresent(userRepository::delete);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(
            Long id,
            UserRequest request) {
        var item = getById(id);

        if (request.getEmail() != null && !item.getEmail().equals(request.getEmail())) {
            item.setEmail(request.getEmail());
        }
        if (request.getRole() != null && !item.getRole().equals(request.getRole())) {
            item.setRole(request.getRole());
        }
        if (request.getLocation() != null) {
            item.setLocation(request.getLocation());
        }

        userRepository.save(item);
        return item;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> exception(HttpStatus.NOT_FOUND, ITEM_NOT_FOUND));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, ITEM_NOT_FOUND));
    }

    public void deleteUserById(Long id) {
        var item = getById(id);
        userRepository.delete(item);
    }

    @Override
    public void resetPassword(
            Long id,
            UserPasswordRequest request) {

        var item = getById(id);
        item.setPassword(passwordService.encodePassword(request.getPassword()));
        userRepository.save(item);
    }
}
