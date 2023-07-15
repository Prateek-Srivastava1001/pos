package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;

@Getter
@Setter
@Entity
@Table(name="Inventory")
public class InventoryPojo {
    @Id
    private int id;
    private int quantity;

}
