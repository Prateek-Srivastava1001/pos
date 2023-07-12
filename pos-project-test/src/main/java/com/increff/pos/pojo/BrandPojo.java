package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name="Brand")
public class BrandPojo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Size(max = 30, message="Brand cannot be more than 30 characters long")
    private String brand;
    @Size(max = 50, message="Category cannot be more than 50 characters long")
    private String category;

}
