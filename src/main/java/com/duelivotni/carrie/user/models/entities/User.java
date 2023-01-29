package com.duelivotni.carrie.user.models.entities;

import com.duelivotni.carrie.data.entity.AuditModel;
import com.duelivotni.carrie.user.models.enums.Role;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = User.TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "update " + User.TABLE_NAME + " set deleted_at = now() where id = ?")
@SequenceGenerator(
        name = "seq",
        sequenceName = "s_" + User.TABLE_NAME,
        initialValue = 1,
        allocationSize = 1
)
public class User
        extends AuditModel implements UserDetails {
    public static final String TABLE_NAME = "users";

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String location;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNull(getDeletedAt());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
