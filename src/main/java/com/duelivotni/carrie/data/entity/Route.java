package com.duelivotni.carrie.data.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Route.ROUTE_TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "update " + Route.ROUTE_TABLE_NAME + " set deleted_at = now() where id = ?")
public class Route extends AuditModel {
    public static final String ROUTE_TABLE_NAME = "route";

    private String startAddress;
    private String endAddress;

    @ManyToOne
    private Order order;
}
