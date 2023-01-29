package com.duelivotni.carrie.auth.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@JsonIgnoreProperties(value = {"authorities", "details", "principal", "credentials", "id", "name", "authenticated"})
public class SecurityUser
        extends UsernamePasswordAuthenticationToken {

    private Long id;
    private String role;
    private String email;
    private String location;


    public SecurityUser(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities,
            Long id,
            GrantedAuthority role,
            String email, String location) {
        super(principal, credentials, authorities);
        this.id = id;
        this.role = role.getAuthority();
        this.email = email;
        this.location = location;
    }

}
