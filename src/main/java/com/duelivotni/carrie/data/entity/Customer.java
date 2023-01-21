package com.duelivotni.carrie.data.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Customer.CUSTOMER_TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "update " + Order.ORDER_TABLE_NAME + " set deleted_at = now() where id = ?")
public class Customer extends AuditModel {
    public static final String CUSTOMER_TABLE_NAME = "customer";

    private String firstName;
    private String lastName;
    private Date birthDate;
    private String passportNumber;

    @OneToMany
    List<Order> orders;

}
