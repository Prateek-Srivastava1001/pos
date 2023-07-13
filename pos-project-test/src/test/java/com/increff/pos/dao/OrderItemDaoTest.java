package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDaoTest extends AbstractUnitTest {
    @Autowired
    OrderItemDao dao;

    // insert and checkDuplicate methods test
    @Test
    public void testInsert(){
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrder_id(1);
        pojo.setProduct_id(2);
        pojo.setQuantity(10);
        pojo.setSelling_price(1000);
        dao.insert(pojo);

        OrderItemPojo gotPojo = dao.checkDuplicate(2,1);
        assertEquals(1, gotPojo.getOrder_id());
        assertEquals(2, gotPojo.getProduct_id());
        assertEquals(10, gotPojo.getQuantity());
        assertEquals(1000, gotPojo.getSelling_price(),0);
    }
    // selectAll method test
    @Test
    public void testSelectAll(){
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrder_id(1);
        pojo.setProduct_id(2);
        pojo.setQuantity(10);
        pojo.setSelling_price(1000);
        dao.insert(pojo);

        OrderItemPojo newPojo = new OrderItemPojo();
        newPojo.setOrder_id(1);
        newPojo.setProduct_id(1);
        newPojo.setQuantity(23);
        newPojo.setSelling_price(1000);
        dao.insert(newPojo);

        List<OrderItemPojo> pojoList = dao.selectAll(1);
        assertEquals(2, pojoList.size());
    }
}
