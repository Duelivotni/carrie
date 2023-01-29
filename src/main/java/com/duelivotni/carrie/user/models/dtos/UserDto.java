package com.duelivotni.carrie.user.models.dtos;

import com.duelivotni.carrie.user.models.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1190536278266811217L;

    private Long id;
    private String email;
    private String location;
    private Role role;
}
