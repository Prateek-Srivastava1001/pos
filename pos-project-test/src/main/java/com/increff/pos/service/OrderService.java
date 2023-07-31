package com.increff.pos.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.increff.pos.util.NormalizeUtil.normalize;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;
    //CREATE
    @Transactional(rollbackOn = ApiException.class)
    public int add(OrderPojo pojo) throws ApiException{
        int orderId = dao.insert(pojo).getId();
        return orderId;
    }
    // returns order from given id, throws exception if it does not exist
    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo pojo = dao.select(id);
        if(pojo==null)
            throw new ApiException("Order with given id not found");
        return pojo;
    }
    // gets all orders from db
    public List<OrderPojo> getAll(){
        return dao.selectAll();
    }
    // gets all orders in a given timeframe (used in reports)
    public List<OrderPojo> getByDate(LocalDateTime startDate, LocalDateTime endDate){
        return dao.getByDate(startDate,endDate);
    }



}
