package com.increff.pos.service;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao dao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderItemPojo pojo) throws ApiException{
        checks(pojo);
        //check for duplicate in same order.
        if(dao.checkDuplicate(pojo.getProduct_id(), pojo.getOrder_id())!=null){
            throw new ApiException("Frontend Validation Breach: Duplicate barcodes detected");
        }
        dao.insert(pojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderItemPojo get(int id) throws ApiException{
        OrderItemPojo pojo = get(id);
        if(pojo==null){
            throw new ApiException("Order Item with given ID not found");
        }
        return pojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemData> getAll(int order_id) throws ApiException {
        List<OrderItemPojo> pojoList =  dao.selectAll(order_id);
        List<OrderItemData> dataList = new ArrayList<>();
        for(OrderItemPojo pojo: pojoList){
            OrderItemData data = convert(pojo);
            dataList.add(data);
        }
        return dataList;
    }

    private OrderItemData convert(OrderItemPojo pojo) throws ApiException {
        OrderItemData data = new OrderItemData();
        data.setId(pojo.getId());
        data.setOrder_id(pojo.getOrder_id());
        data.setProduct_id(pojo.getProduct_id());
        data.setName(productService.get(pojo.getProduct_id()).getName());
        data.setBarcode(productService.get(pojo.getProduct_id()).getBarcode());
        data.setQuantity(pojo.getQuantity());
        data.setSelling_price(pojo.getSelling_price());
        return data;
    }
    //Validations
    protected void checks(OrderItemPojo pojo) throws ApiException{
        //Negative quantity check
        if(pojo.getQuantity()<=0){
            throw new ApiException("Please enter positive value of quantity");
        }
        //Negative Selling price check
        if(pojo.getSelling_price()<0){
            throw new ApiException("Selling Price cannot be negative");
        }
        //check product with given id in inventory
        if(inventoryService.get(pojo.getProduct_id())==null){
            throw new ApiException("Product with given ID not available");
        }
        if(inventoryService.get(pojo.getProduct_id()).getQuantity()<pojo.getQuantity()){
            throw new ApiException("Not enough quantity is present in the inventory.");
        }
        if(productService.get(pojo.getProduct_id()).getMrp()<pojo.getSelling_price()){
            throw new ApiException("Selling price cannot be more than MRP.");
        }

    }
}
