package com.duelivotni.carrie.user.controllers;

import com.duelivotni.carrie.exception.ServiceException;
import com.duelivotni.carrie.user.mappers.UserMapper;
import com.duelivotni.carrie.user.models.dtos.UserDto;
import com.duelivotni.carrie.user.models.enums.Role;
import com.duelivotni.carrie.user.models.requests.UserPasswordRequest;
import com.duelivotni.carrie.user.models.requests.UserRequest;
import com.duelivotni.carrie.user.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@SecurityRequirements(@SecurityRequirement(name = "bearerAuth"))
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public List<UserDto> findByAll() {
        return service.findAll().stream().map(mapper::map).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return mapper.map(service.getById(id));
    }

    @GetMapping("email/{email}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public UserDto getUserByEmail(@PathVariable("email") String email) {
        return mapper.map(service.getByEmail(email));
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto saveUser(@RequestBody @Valid UserRequest request) throws ServiceException {

        if (request.getRole() == Role.ROLE_ADMIN) {
            return mapper.map(service.registerAdmin(request.getEmail(), request.getPassword(), request.getLocation()));
        }
        if (request.getRole() == Role.ROLE_MANAGER) {
            return mapper.map(service.registerManager(request.getEmail(), request.getPassword(), request.getLocation()));
        } else {
            String[] message = {"Invalid Argument"};
            throw new ServiceException(message, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto updateUser(@PathVariable @Valid Long id, @RequestBody @Valid UserRequest request) {
        return mapper.map(service.updateUser(id, request));
    }

    @PatchMapping("/reset-password/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void resetPassword(@PathVariable @Valid Long id, @RequestBody @Valid UserPasswordRequest request) {
        service.resetPassword(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
    }

}
