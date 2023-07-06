package com.increff.pos.util;

import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Component;

@Component
public class NormalizeUtil {
    public static void normalize(BrandPojo p) {
        p.setCategory(p.getCategory().toLowerCase ().trim());
        p.setBrand(p.getBrand().toLowerCase ().trim());
    }
    public static void normalize(ProductPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase ().trim());
        p.setName(p.getName().toLowerCase ().trim());
    }
    public static void normalize(OrderItemForm form){
        form.setBarcode(form.getBarcode().toLowerCase ().trim());
    }
    public static void normalize(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
        p.setRole(p.getRole().toLowerCase().trim());
    }
}
