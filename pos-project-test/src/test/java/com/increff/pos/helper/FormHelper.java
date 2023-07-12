package com.increff.pos.helper;

import com.increff.pos.model.*;

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
    public static InventoryForm createInventory(int quantity, String barcode){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }
    public static OrderItemForm createOrderItem(String barcode, int quantity, double sellingPrice){
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSelling_price(sellingPrice);
        return orderItemForm;
    }
    public static ReportsForm createReports(String startDate, String endDate){
        ReportsForm reportsForm = new ReportsForm();
        reportsForm.setStartDate(startDate);
        reportsForm.setEndDate(endDate);
        return reportsForm;
    }
    public static LoginForm createUser(String email, String password){
        LoginForm form = new LoginForm();
        form.setEmail(email);
        form.setPassword(password);
        return form;
    }
}
