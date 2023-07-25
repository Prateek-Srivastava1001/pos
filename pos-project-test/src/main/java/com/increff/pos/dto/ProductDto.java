package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
    @Autowired
    ProductService productService;
    @Autowired
    BrandService brandService;
    @Autowired
    InventoryService inventoryService;
    ConverterUtil converterUtil;
    NormalizeUtil normalizeUtil;
    ValidateUtil validateUtil;
    //Adds Product in DB and initializes Inventory with zero quantity
    public void add(ProductForm form) throws ApiException {
        normalizeUtil.normalize(form);
        BrandPojo brandPojo = brandService.combinationChecker(form.getBrand(), form.getCategory());
        ProductPojo pojo = converterUtil.convert(form, brandPojo);
        validateUtil.checkValid(pojo);
        InventoryPojo inventory = new InventoryPojo();
        productService.add(pojo);
        ProductPojo newPojo = productService.getByBarcode(pojo.getBarcode());
        inventory.setId(newPojo.getId());
        inventory.setQuantity(0);
        inventoryService.add(inventory);
    }
    //Getting particular data from db
    public ProductData get(int id) throws ApiException{
        ProductPojo pojo = productService.getCheck(id);
        BrandPojo brandPojo = brandService.getCheck(pojo.getBrand_category());
        return converterUtil.convert(pojo, brandPojo);
    }
    //Getting all data from db
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo pojo : list) {
            BrandPojo brandPojo = brandService.getCheck(pojo.getBrand_category());
            list2.add(converterUtil.convert(pojo, brandPojo));
        }
        return list2;
    }
    //updating particular product
    public void update(int id, ProductForm form) throws ApiException{
        normalizeUtil.normalize(form);
        BrandPojo brandPojo = brandService.combinationChecker(form.getBrand(), form.getCategory());
        ProductPojo pojo = converterUtil.convert(form, brandPojo);
        validateUtil.checkValid(pojo);
        productService.update(id, pojo);
    }
}
