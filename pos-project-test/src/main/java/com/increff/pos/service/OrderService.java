package com.increff.pos.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.InvoiceForm;
import com.increff.pos.model.OrderItem;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.increff.pos.util.NormalizeUtil.normalize;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public int add(OrderPojo pojo) throws ApiException{
        int orderId = dao.insert(pojo).getId();
        return orderId;
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



}
