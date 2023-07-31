package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Component
public class ConverterUtil {
    // BrandPojo -> BrandData conversion
    public static BrandData convert(BrandPojo pojo){
        BrandData data = new BrandData();
        data.setCategory(pojo.getCategory());
        data.setBrand(pojo.getBrand());
        data.setId(pojo.getId());
        return data;
    }
    // BrandForm -> BrandPojo conversion
    public static BrandPojo convert(BrandForm form){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand(form.getBrand());
        pojo.setCategory(form.getCategory());
        return pojo;
    }
    // ProductPojo -> ProductData conversion. Data contains Brand and Category, hence using BrandPojo
    public static ProductData convert(ProductPojo pojo, BrandPojo brandPojo){
        ProductData data = new ProductData();
        data.setId(pojo.getId());
        data.setBarcode(pojo.getBarcode());
        data.setBrand(brandPojo.getBrand());
        data.setCategory(brandPojo.getCategory());
        data.setName(pojo.getName());
        data.setMrp(pojo.getMrp());
        return  data;
    }
    // ProductForm -> ProductPojo conversion. we get BrandId from brandPojo
    public static ProductPojo convert(ProductForm form, BrandPojo brandPojo){
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setBrand_category(brandPojo.getId());
        pojo.setName(form.getName());
        BigDecimal roundedValue = BigDecimal.valueOf(form.getMrp()).setScale(2, RoundingMode.HALF_UP);
        double mrp = roundedValue.doubleValue();
        pojo.setMrp(mrp);
        return pojo;
    }
    //InventoryPojo -> InventoryData conversion
    public static InventoryData convert(InventoryPojo pojo){
        InventoryData data = new InventoryData();
        data.setId(pojo.getId());
        data.setQuantity(pojo.getQuantity());
        return data;
    }
    // InventoryForm -> InventoryPojo conversion
    public static InventoryPojo convert(InventoryForm form){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setQuantity(form.getQuantity());
        return pojo;
    }
    // converting UserPojo to Authentication object
    public static Authentication convert(UserPojo p) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(p.getEmail());
        principal.setId(p.getId());

        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(p.getRole()));
        // you can add more roles if required

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }
    // LoginForm -> UserPojo conversion, uses String[] array for assigning supervisor role
    public static UserPojo convert(LoginForm form, String[] array){
        int flag=0;
        UserPojo pojo = new UserPojo();
        pojo.setEmail(form.getEmail());
        pojo.setPassword(form.getPassword());
        // assigning Supervisor role if email is present in array
        for(String supervisor: array){
            if(supervisor.equals(pojo.getEmail())) {
                flag = 1;
            }
        }
        if(flag==1){
            pojo.setRole("supervisor");
            System.out.println("Supervisor role assigned");
        }
        else{
            pojo.setRole("operator");
            System.out.println("operator role assigned");
        }
        return pojo;
    }


}
