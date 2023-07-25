package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    ProductService productService;
    ConverterUtil converterUtil;

    //Getting inventory data by id
    public InventoryData get(int id) throws ApiException{
        InventoryPojo pojo = inventoryService.getCheck(id);
        return converterUtil.convert(pojo);
    }
    //Getting all data from DB
    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> pojoList = inventoryService.getAll();
        List<InventoryData> dataList = new ArrayList<>();
        for(InventoryPojo pojo : pojoList){
            InventoryData data = new InventoryData();
            ProductPojo product = productService.getCheck(pojo.getId());
            data.setId(pojo.getId());
            data.setQuantity(pojo.getQuantity());
            data.setBarcode(product.getBarcode());
            dataList.add(data);
        }
        return dataList;
    }
    //Updating by edit button
    public void edit(int id, InventoryForm form) throws ApiException {
        InventoryPojo pojo = converterUtil.convert(form);
        if(pojo.getQuantity()<0){
            throw new ApiException("Quantity cannot be negative");
        }
        if(pojo.getQuantity()>10000000){
            throw new ApiException("Quantity cannot be more than 10000000");
        }
        inventoryService.update(id, pojo);
    }
    //Updating by upload (adds the inventory to existing value)
    public void editByUpload(InventoryForm form) throws ApiException{
        String barcode = form.getBarcode();
        InventoryPojo pojo = converterUtil.convert(form);
        if(pojo.getQuantity()<0){
            throw new ApiException("Quantity cannot be negative");
        }
        ProductPojo productPojo = productService.getByBarcode(barcode);
        int previousQty = inventoryService.getCheck(productPojo.getId()).getQuantity();
        int updateQty = pojo.getQuantity()+previousQty;
        if(updateQty>10000000 || updateQty<0)
        {
            throw new ApiException("Quantity out of bound");
        }
        pojo.setQuantity(updateQty);
        inventoryService.update(productPojo.getId(), pojo);
    }
}
