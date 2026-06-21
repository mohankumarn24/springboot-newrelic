package net.projectsync.springboot_newrelic.controller;

import net.projectsync.springboot_newrelic.model.Orders;
import net.projectsync.springboot_newrelic.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Orders createOrder(@RequestBody Orders orders) {
        return orderService.createOrder(orders);
    }

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}")
    public Orders updateOrderById(@PathVariable Long id, @RequestBody Orders orders) {
        return orderService.updateOrderById(id, orders);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
    }
}