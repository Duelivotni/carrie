package com.duelivotni.carrie.data.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Order.ORDER_TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "update " + Order.ORDER_TABLE_NAME + " set deleted_at = now() where id = ?")
public class Order extends AuditModel{
    public static final String ORDER_TABLE_NAME = "_order";

    private String pickupCountry;
    private String pickupCity;
    private String pickupAddress;
    private LocalDateTime pickupTime;

    private String deliveryCountry;
    private String deliveryCity;
    private String deliveryAddress;
    private LocalDateTime deliveryTime;

    private Integer lengthMm;
    private Integer widthMm;
    private Integer heightMm;

    private String name;
    private String description;
    private Double value;
    private Double price;

    private String status;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Carrier carrier;

    @OneToMany
    private List<Route> routes;
}