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
    //CREATE
    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderItemPojo pojo) throws ApiException{
        //check for duplicate in same order.
        if(dao.checkDuplicate(pojo.getProduct_id(), pojo.getOrder_id())!=null){
            throw new ApiException("Frontend Validation Breach: Duplicate barcodes detected");
        }
        dao.insert(pojo);
    }
    // gets all orderItems corresponding to particular order Id
    public List<OrderItemData> getAll(int order_id) throws ApiException {
        List<OrderItemPojo> pojoList =  dao.selectAll(order_id);
        List<OrderItemData> dataList = new ArrayList<>();
        for(OrderItemPojo pojo: pojoList){
            OrderItemData data = convert(pojo);
            dataList.add(data);
        }
        return dataList;
    }
    //helper
    private OrderItemData convert(OrderItemPojo pojo) throws ApiException {
        OrderItemData data = new OrderItemData();
        data.setId(pojo.getId());
        data.setOrder_id(pojo.getOrder_id());
        data.setProduct_id(pojo.getProduct_id());
        data.setName(productService.getCheck(pojo.getProduct_id()).getName());
        data.setBarcode(productService.getCheck(pojo.getProduct_id()).getBarcode());
        data.setQuantity(pojo.getQuantity());
        data.setSelling_price(pojo.getSelling_price());
        return data;
    }

}
