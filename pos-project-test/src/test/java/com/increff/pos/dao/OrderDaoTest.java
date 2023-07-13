package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDaoTest extends AbstractUnitTest {
    @Autowired
    OrderDao dao;

    // insert and getByDate methods test
    @Test
    public void testInsert(){
        LocalDateTime date = LocalDateTime.now();
        OrderPojo pojo = new OrderPojo();
        pojo.setDate_time(date);
        dao.insert(pojo);

        List<OrderPojo> pojoList = dao.getByDate(date,date);
        assertEquals(1, pojoList.size());

        OrderPojo gotPojo = pojoList.get(0);
        assertEquals(date, gotPojo.getDate_time());
    }
    // select(id) method test
    @Test
    public void testSelect(){
        LocalDateTime date = LocalDateTime.now();
        OrderPojo pojo = new OrderPojo();
        pojo.setDate_time(date);
        dao.insert(pojo);

        int id = dao.getByDate(date, date).get(0).getId();

        OrderPojo gotPojo = dao.select(id);
        assertEquals(date, gotPojo.getDate_time());
    }
    //selectAll method test
    @Test
    public void testSelectAll(){
        LocalDateTime date = LocalDateTime.now();
        OrderPojo pojo = new OrderPojo();
        pojo.setDate_time(date);
        dao.insert(pojo);

        OrderPojo newPojo = new OrderPojo();
        newPojo.setDate_time(date);
        dao.insert(newPojo);

        List<OrderPojo> pojoList = dao.selectAll();
        assertEquals(2, pojoList.size());
    }
}
