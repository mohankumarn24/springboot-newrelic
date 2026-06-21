package net.projectsync.springboot_newrelic.service;

import net.projectsync.springboot_newrelic.exception.OrderNotFoundException;
import net.projectsync.springboot_newrelic.model.Orders;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final Map<Long, Orders> orderDb = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    // pre-loaded data
    public OrderService() {

        long id1 = idGenerator.incrementAndGet();
        orderDb.put(id1,
                Orders.builder()
                        .id(id1)
                        .customerName("Mohan")
                        .product("Laptop")
                        .quantity(1)
                        .price(50000)
                        .status("CREATED")
                        .build());

        long id2 = idGenerator.incrementAndGet();
        orderDb.put(id2,
                Orders.builder()
                        .id(id2)
                        .customerName("Ravi")
                        .product("Mobile")
                        .quantity(2)
                        .price(20000)
                        .status("SHIPPED")
                        .build());
    }

    public Orders createOrder(Orders order) {
        simulateLatency();
        long id = idGenerator.incrementAndGet();
        order.setId(id);
        orderDb.put(id, order);
        return order;
    }

    public Orders getOrderById(Long id) {
        simulateLatency();
        Orders order = orderDb.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        return order;
    }

    // Example of custom New Relic instrumentation.
    //  - Generally avoided unless additional business metrics or attributes
    //    are required, as it introduces vendor-specific dependencies.
    //  - For this project, New Relic Java Agent auto-instrumentation is sufficient.

    /*
    @Trace(dispatcher = true)
    public Orders getOrderById(Long id) {
        simulateLatency();
        Orders order = orderDb.get(id);
        if (order == null) {
            NewRelic.noticeError("Order not found: " + id);
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        NewRelic.addCustomParameter("OrderID", id);
        NewRelic.addCustomParameter("CustomerName", order.getCustomerName());
        NewRelic.addCustomParameter("Status", order.getStatus());
        return order;
    }
    */

    public List<Orders> getAllOrders() {
        simulateLatency();
        List<Orders> orders = new ArrayList<>(orderDb.values());
        return orders;
    }

    public Orders updateOrderById(Long id, Orders updatedOrders) {
        simulateLatency();
        Orders existing = orderDb.get(id);
        if (existing == null) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }

        existing.setStatus(updatedOrders.getStatus());
        existing.setQuantity(updatedOrders.getQuantity());
        existing.setPrice(updatedOrders.getPrice());
        return existing;
    }

    public void deleteOrderById(Long id) {
        simulateLatency();
        if (orderDb.remove(id) == null) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
    }

    private void simulateLatency() {
        try {
            Thread.sleep(300 + new Random().nextInt(700)); // 300-1000 ms delay
            for (int i = 0; i < 50000; i++) {
                Math.log(i + 1); // Simulate CPU work
            }
        } catch (InterruptedException ignored) {}
    }
}