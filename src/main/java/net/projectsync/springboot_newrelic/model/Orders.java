package net.projectsync.springboot_newrelic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    private Long id;
    private String customerName;
    private String product;
    private int quantity;
    private double price;
    private String status;

    public double getTotalAmount() {
        return price * quantity;
    }
}