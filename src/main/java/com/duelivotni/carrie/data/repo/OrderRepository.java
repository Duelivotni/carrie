package com.duelivotni.carrie.data.repo;

import com.duelivotni.carrie.data.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
}