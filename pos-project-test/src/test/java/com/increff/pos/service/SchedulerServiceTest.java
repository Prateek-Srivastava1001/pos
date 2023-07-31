package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.ReportsForm;
import com.increff.pos.pojo.SchedulerPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SchedulerServiceTest extends AbstractUnitTest {
    @Autowired
    SchedulerService service;

    // add and getAll methods test
    @Test
    public void testAdd() throws ApiException{
        SchedulerPojo pojo = new SchedulerPojo();
        LocalDate date = LocalDate.parse("2023-07-10");
        pojo.setDate(date);
        pojo.setInvoiced_items_count(10);
        pojo.setInvoiced_orders_count(2);
        pojo.setTotal_revenue(20100);

        service.add(pojo);

        List<SchedulerPojo> pojoList = service.getAll();
        assertEquals(1, pojoList.size());
        SchedulerPojo gotPojo = pojoList.get(0);

        assertEquals(date, gotPojo.getDate());
        assertEquals(2, gotPojo.getInvoiced_orders_count());
        assertEquals(10, gotPojo.getInvoiced_items_count());
        assertEquals(20100, gotPojo.getTotal_revenue(),0);
    }
    // test getbyDate filter
    @Test
    public void testGetByDate() throws ApiException{
        SchedulerPojo pojo = new SchedulerPojo();
        LocalDate date = LocalDate.parse("2023-07-10");
        pojo.setDate(date);
        pojo.setInvoiced_items_count(10);
        pojo.setInvoiced_orders_count(2);
        pojo.setTotal_revenue(20100);
        service.add(pojo);

        SchedulerPojo newPojo = new SchedulerPojo();
        LocalDate newDate = LocalDate.parse("2023-07-11");
        newPojo.setDate(newDate);
        newPojo.setInvoiced_items_count(20);
        newPojo.setInvoiced_orders_count(1);
        newPojo.setTotal_revenue(50000);
        service.add(newPojo);

        ReportsForm form = FormHelper.createReports("2023-07-10", "2023-07-10");
        List<SchedulerPojo> pojoList = service.getByDate(form);
        assertEquals(1, pojoList.size());
        SchedulerPojo gotPojo = pojoList.get(0);

        assertEquals(date, gotPojo.getDate());
        assertEquals(2, gotPojo.getInvoiced_orders_count());
        assertEquals(10, gotPojo.getInvoiced_items_count());
        assertEquals(20100, gotPojo.getTotal_revenue(),0);
    }
    // test getByDate with invalid date
    @Test
    public void testGetByInvalidDate() throws ApiException{
        try {
            ReportsForm form = FormHelper.createReports("2023-07-10", "2023-06-10");
            service.getByDate(form);
        } catch (ApiException err){
            TestCase.assertEquals("Start date cannot be after end date", err.getMessage());
        }
    }
    // test adding more than once on same date
    @Test
    public void testAddTwice() throws ApiException{
        SchedulerPojo pojo = new SchedulerPojo();
        LocalDate date = LocalDate.parse("2023-07-10");
        pojo.setDate(date);
        pojo.setInvoiced_items_count(10);
        pojo.setInvoiced_orders_count(2);
        pojo.setTotal_revenue(20100);
        service.add(pojo);

        SchedulerPojo newPojo = new SchedulerPojo();
        LocalDate newDate = LocalDate.parse("2023-07-10");
        newPojo.setDate(newDate);
        newPojo.setInvoiced_items_count(20);
        newPojo.setInvoiced_orders_count(1);
        newPojo.setTotal_revenue(50000);
        service.add(newPojo);

        List<SchedulerPojo> pojoList = service.getAll();
        assertEquals(1, pojoList.size());
        SchedulerPojo gotPojo = pojoList.get(0);

        assertEquals(date, gotPojo.getDate());
        assertEquals(1, gotPojo.getInvoiced_orders_count());
        assertEquals(20, gotPojo.getInvoiced_items_count());
        assertEquals(50000, gotPojo.getTotal_revenue(),0);
    }

}
