package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.OrderPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    OrderService service;

    // add, getCheck and getByDate method test
    @Test
    public void testAdd() throws ApiException{
        OrderPojo pojo = new OrderPojo();
        LocalDateTime dateTime = LocalDateTime.now();
        pojo.setDate_time(dateTime);

        service.add(pojo);
        List<OrderPojo> pojoList = service.getByDate(dateTime, dateTime);

        assertEquals(1, pojoList.size());
        int id = pojoList.get(0).getId();
        OrderPojo gotPojo = service.getCheck(id);
        assertEquals(dateTime, gotPojo.getDate_time());
    }
    //getAll method test
    @Test
    public void testGetAll() throws ApiException{
        OrderPojo pojo = new OrderPojo();
        LocalDateTime dateTime = LocalDateTime.now();
        pojo.setDate_time(dateTime);
        service.add(pojo);

        OrderPojo newPojo = new OrderPojo();
        newPojo.setDate_time(LocalDateTime.now());
        service.add(newPojo);

        List<OrderPojo> pojoList = service.getAll();
        assertEquals(2, pojoList.size());
    }
    // getting non existent id
    @Test
    public void testGetNonExistentId() throws ApiException{
        try {
            service.getCheck(1);
        } catch (ApiException err){
            TestCase.assertEquals("Order with given id not found", err.getMessage());
        }
    }
}
