package com.duelivotni.carrie.user.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_MANAGER,

    ROLE_CRAWLER,
    ;


    @Override
    public String getAuthority() {
        return this.name();
    }
}
