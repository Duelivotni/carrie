package com.duelivotni.carrie.data.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Carrier.CARRIER_TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "update " + Carrier.CARRIER_TABLE_NAME + " set deleted_at = now() where id = ?")
public class Carrier extends AuditModel {
    public static final String CARRIER_TABLE_NAME = "carrier";

    private String firstName;
    private String lastName;
    private Date birthDate;
    private String passportNumber;

    @OneToMany
    List<Order> orders;
}
