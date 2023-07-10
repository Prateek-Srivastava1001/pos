package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private Integer orderItemId;
    private Double sellingPrice;
    private Double totalAmount;
    private String productName;
    private Integer quantity;

}
