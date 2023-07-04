package com.increff.pos.service;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;

    private int orderId=0;
    @Transactional(rollbackOn = ApiException.class)
    public int add(List<OrderItemForm> formList) throws ApiException{
        if(formList.size()<1){
            throw new ApiException("Frontend Validation Breach: Empty Order List Not Supported");
        }
        OrderPojo pojo = new OrderPojo();
        LocalDateTime dateTime = LocalDateTime.now();
        pojo.setDate_time(dateTime);
        orderId = dao.insert(pojo).getId();
        addItems(formList);
        return orderId;
    }
    @Transactional(rollbackOn = ApiException.class)
    public void addItems(List<OrderItemForm> formList) throws ApiException{
        for(OrderItemForm form:formList){
            OrderItemPojo pojo = convert(form);
            orderItemService.add(pojo);

            //Updating in Inventory
            InventoryPojo inventoryPojo = inventoryService.get(pojo.getProduct_id());
            int quantity = inventoryPojo.getQuantity() - pojo.getQuantity();
            inventoryPojo.setQuantity(quantity);
            inventoryService.update(pojo.getProduct_id(),inventoryPojo);
        }
    }
    @Transactional
    public OrderPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo pojo = dao.select(id);
        if(pojo==null)
            throw new ApiException("Order with given id not found");
        return pojo;
    }

    @Transactional
    public List<OrderPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional
    public List<OrderPojo> getByDate(LocalDateTime startDate, LocalDateTime endDate){
        return dao.getByDate(startDate,endDate);
    }

    public void checkValidity(OrderItemForm form) throws ApiException{
        OrderItemPojo pojo = convert(form);
        orderItemService.checks(pojo);
    }
    private OrderItemPojo convert(OrderItemForm form) throws ApiException {
        normalize(form);
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrder_id(orderId);
        pojo.setProduct_id(productService.getByBarcode(form.getBarcode()).getId());
        pojo.setQuantity(form.getQuantity());
        pojo.setSelling_price(form.getSelling_price());
        return pojo;
    }

    protected static void normalize(OrderItemForm form){
        form.setBarcode(form.getBarcode().toLowerCase ().trim());
    }
}
