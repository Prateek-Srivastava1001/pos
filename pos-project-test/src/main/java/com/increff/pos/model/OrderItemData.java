package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends  OrderItemForm{
    private int id;
    private int order_id;
    private int product_id;
    private String name;

}
