package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
public class BrandForm {
    @NotBlank
    @Size(max = 30, message="Brand cannot be more than 30 characters long")
    private String brand;
    @NotBlank
    @Size(max = 50, message = "Category cannot be more than 50 characters long")
    private String category;

}
