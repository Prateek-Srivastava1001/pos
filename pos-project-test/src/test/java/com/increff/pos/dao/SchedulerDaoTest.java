package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.SchedulerPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SchedulerDaoTest extends AbstractUnitTest {
    @Autowired
    SchedulerDao dao;

    // insert and selectByDate methods test
    @Test
    public void testInsert(){
        SchedulerPojo pojo = new SchedulerPojo();
        LocalDate date = LocalDate.now();
        pojo.setDate(date);
        pojo.setInvoiced_orders_count(2);
        pojo.setInvoiced_items_count(10);
        pojo.setTotal_revenue(1000);
        dao.insert(pojo);

        List<SchedulerPojo> pojoList = dao.selectByDate(date, date);
        assertEquals(1, pojoList.size());
        SchedulerPojo gotPojo = pojoList.get(0);

        assertEquals(date, gotPojo.getDate());
        assertEquals(2, gotPojo.getInvoiced_orders_count());
        assertEquals(10, gotPojo.getInvoiced_items_count());
        assertEquals(1000, gotPojo.getTotal_revenue(),0);
    }
    //selectAll method test
    @Test
    public void testSelectAll(){
        SchedulerPojo pojo = new SchedulerPojo();
        LocalDate date = LocalDate.parse("2023-06-10");
        pojo.setDate(date);
        pojo.setInvoiced_orders_count(2);
        pojo.setInvoiced_items_count(10);
        pojo.setTotal_revenue(1000);
        dao.insert(pojo);

        SchedulerPojo newPojo = new SchedulerPojo();
        LocalDate newDate = LocalDate.parse("2023-06-11");
        newPojo.setDate(newDate);
        newPojo.setInvoiced_items_count(20);
        newPojo.setInvoiced_orders_count(1);
        newPojo.setTotal_revenue(20000);
        dao.insert(newPojo);

        List<SchedulerPojo> pojoList = dao.selectAll();

        assertEquals(2, pojoList.size());
    }
}
