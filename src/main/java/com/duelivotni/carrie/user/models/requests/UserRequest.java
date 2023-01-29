package com.duelivotni.carrie.user.models.requests;

import com.duelivotni.carrie.user.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String location;

    @NotNull
    private Role role;


}
