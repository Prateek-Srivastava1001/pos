package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name="Product")
public class ProductPojo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Size(max = 15, message="Barcode cannot be more than 15 characters long")
    private String barcode;
    private int brand_category;
    @Size(max = 50, message="Product name cannot be more than 50 characters long")
    private String name;
    private double mrp;

}
