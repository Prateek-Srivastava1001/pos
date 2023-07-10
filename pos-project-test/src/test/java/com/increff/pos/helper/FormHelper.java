package com.increff.pos.helper;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductForm;

public class FormHelper {
    public static BrandForm createBrand(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
    public static ProductForm createProduct(String barcode, String brand, String category, String name, double mrp){
        ProductForm productForm = new ProductForm();
        productForm.setBarcode(barcode);
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setName(name);
        productForm.setMrp(mrp);
        return productForm;
    }
}
