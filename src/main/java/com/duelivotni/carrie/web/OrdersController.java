package com.duelivotni.carrie.web;

import com.duelivotni.carrie.data.entity.Order;
import com.duelivotni.carrie.data.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/orders")
public class OrdersController {
    private final OrderRepository orderRepository;

    @Autowired
    public OrdersController(OrderRepository ordersRepository) {
        this.orderRepository = ordersRepository;
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<>((List<Order>) orderRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        Order newOrder = orderRepository.save(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PutMapping(path = "/change", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Order> changeOrderById(@RequestBody Order order, @RequestParam UUID id)
    {
        Order currentOrder = orderRepository.findById(order.getId()).orElseThrow();
//        currentOrder.setOrderLocation(order.getOrderLocation());
//        currentOrder.setCustomerName(order.getCustomerName());
        orderRepository.save(currentOrder);
        return new ResponseEntity<>(currentOrder, HttpStatus.OK);
    }
}
