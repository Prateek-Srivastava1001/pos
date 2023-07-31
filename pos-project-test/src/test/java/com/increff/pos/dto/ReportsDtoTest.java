package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.*;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportsDtoTest extends AbstractUnitTest {
    @Autowired
    ReportsDto reportsDto;
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    OrderDto orderDto;
    @Autowired
    ProductService productService;
    // test generating and getting day-on-day sales report
    @Test
    public void testSchedulerReport() throws ApiException, InterruptedException {
        //Creating Order
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm product1 = FormHelper.createProduct("barcode1", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(product1);
        ProductForm product2 = FormHelper.createProduct("barcode2", "testbrand", "testcategory",
                "newName", 1000);
        productDto.add(product2);
        String barcode1 = "barcode1";
        int quantity = 20;
        int id1 = productService.getByBarcode(barcode1).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode1);
        inventoryDto.edit(id1, inventoryForm);
        String barcode2 = "barcode2";
        int quantity2 = 20;
        int id2 = productService.getByBarcode(barcode2).getId();
        InventoryForm inventoryForm2 = FormHelper.createInventory(quantity2, barcode2);
        inventoryDto.edit(id2, inventoryForm2);

        List<OrderItemForm> orderItems = new ArrayList<>();
        OrderItemForm orderItem1 = FormHelper.createOrderItem(barcode1, 2, 1000);
        orderItems.add(orderItem1);

        OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode2, 5, 999);
        orderItems.add(orderItem2);

        int orderId = orderDto.add(orderItems);
        //Running Scheduler
        reportsDto.generateDailyReport();
        //getting from schedulerPojo
        List<SchedulerPojo> pojoList = reportsDto.getScheduledData();
        assertEquals(1, pojoList.size());
        SchedulerPojo report = pojoList.get(0);
        assertEquals(1, report.getInvoiced_orders_count());
        assertEquals(7, report.getInvoiced_items_count());
        double revenue = 1000*2+999*5;
        assertEquals(revenue, report.getTotal_revenue(),0);
    }
    //run scheduler more than once should update the values to latest
    @Test
    public void testRunSchedulerTwice() throws ApiException, InterruptedException {
        //Creating Order
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm product1 = FormHelper.createProduct("barcode1", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(product1);
        ProductForm product2 = FormHelper.createProduct("barcode2", "testbrand", "testcategory",
                "newName", 1000);
        productDto.add(product2);
        String barcode1 = "barcode1";
        int quantity = 20;
        int id1 = productService.getByBarcode(barcode1).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode1);
        inventoryDto.edit(id1, inventoryForm);
        String barcode2 = "barcode2";
        int quantity2 =28;
        int id2 = productService.getByBarcode(barcode2).getId();
        InventoryForm inventoryForm2 = FormHelper.createInventory(quantity2, barcode2);
        inventoryDto.edit(id2, inventoryForm2);

        List<OrderItemForm> orderItems = new ArrayList<>();
        OrderItemForm orderItem1 = FormHelper.createOrderItem(barcode1, 2, 1000);
        orderItems.add(orderItem1);

        OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode2, 5, 999);
        orderItems.add(orderItem2);

        orderDto.add(orderItems);
        //Running Scheduler
        reportsDto.generateDailyReport();
        orderDto.add(orderItems);
        //Running Scheduler again
        reportsDto.generateDailyReport();
        //getting from schedulerPojo
        List<SchedulerPojo> pojoList = reportsDto.getScheduledData();
        assertEquals(1, pojoList.size());
        SchedulerPojo report = pojoList.get(0);
        assertEquals(2, report.getInvoiced_orders_count());
        assertEquals(14, report.getInvoiced_items_count());
        double revenue = (1000*2+999*5)*2;
        assertEquals(revenue, report.getTotal_revenue(),0);
    }
    //get scheduler report with date filter
    @Test
    public void testGetByDate() throws ApiException, InterruptedException {
        //Creating Order
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm product1 = FormHelper.createProduct("barcode1", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(product1);
        ProductForm product2 = FormHelper.createProduct("barcode2", "testbrand", "testcategory",
                "newName", 1000);
        productDto.add(product2);
        String barcode1 = "barcode1";
        int quantity = 10;
        int id1 = productService.getByBarcode(barcode1).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode1);
        inventoryDto.edit(id1, inventoryForm);
        String barcode2 = "barcode2";
        int quantity2 = 8;
        int id2 = productService.getByBarcode(barcode2).getId();
        InventoryForm inventoryForm2 = FormHelper.createInventory(quantity2, barcode2);
        inventoryDto.edit(id2, inventoryForm2);

        List<OrderItemForm> orderItems = new ArrayList<>();
        OrderItemForm orderItem1 = FormHelper.createOrderItem(barcode1, 2, 1000);
        orderItems.add(orderItem1);

        OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode2, 5, 999);
        orderItems.add(orderItem2);

        int orderId = orderDto.add(orderItems);
        //Running Scheduler
        reportsDto.generateDailyReport();
        //getting by date filter
        LocalDate date = LocalDate.now();
        String startDate = date.toString();
        String endDate = date.toString();
        ReportsForm reportsForm = FormHelper.createReports(startDate, endDate);

        List<SchedulerPojo> pojoList = reportsDto.getByDate(reportsForm);
        assertEquals(1, pojoList.size());
        SchedulerPojo report = pojoList.get(0);
        assertEquals(1, report.getInvoiced_orders_count());
        assertEquals(7, report.getInvoiced_items_count());
        double revenue = 1000*2+999*5;
        assertEquals(revenue, report.getTotal_revenue(),0);
    }

    //Test Sales Report
    @Test
    public void testGetSalesReport() throws ApiException{
        //Creating Order
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm product1 = FormHelper.createProduct("barcode1", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(product1);
        ProductForm product2 = FormHelper.createProduct("barcode2", "testbrand", "testcategory",
                "newName", 1000);
        productDto.add(product2);
        String barcode1 = "barcode1";
        int quantity = 10;
        int id1 = productService.getByBarcode(barcode1).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode1);
        inventoryDto.edit(id1, inventoryForm);
        String barcode2 = "barcode2";
        int quantity2 = 8;
        int id2 = productService.getByBarcode(barcode2).getId();
        InventoryForm inventoryForm2 = FormHelper.createInventory(quantity2, barcode2);
        inventoryDto.edit(id2, inventoryForm2);

        List<OrderItemForm> orderItems = new ArrayList<>();
        OrderItemForm orderItem1 = FormHelper.createOrderItem(barcode1, 2, 1000);
        orderItems.add(orderItem1);

        OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode2, 5, 999);
        orderItems.add(orderItem2);

        orderDto.add(orderItems);
        //getting date filter
        LocalDate date = LocalDate.now();
        String startDate = date.toString();
        String endDate = date.toString();
        ReportsForm reportsForm = FormHelper.createReports(startDate, endDate);
        //Getting sales report
        List<ReportsData> dataList = reportsDto.getSalesReport(reportsForm);
        ReportsData report = dataList.get(0);
        String expectedBrand = "testbrand";
        String expectedCategory = "testcategory";
        int expectedQuantity = 7;
        double expectedRevenue = 1000*2+999*5;

        assertEquals(expectedBrand, report.getBrand());
        assertEquals(expectedCategory, report.getCategory());
        assertEquals(expectedQuantity, report.getQuantity());
        assertEquals(expectedRevenue, report.getRevenue(), 0);
    }
    //Test Inventory Report
    @Test
    public void testInventoryReport() throws ApiException{
        //generating inventory
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        productForm = FormHelper.createProduct("secondbarcode", "testbrand", "testcategory",
                                                "newName", 1000);
        productDto.add(productForm);
        String barcode = "testbarcode";
        int quantity = 10;
        int id = productService.getByBarcode(barcode).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
        inventoryDto.edit(id, inventoryForm);
        inventoryForm = FormHelper.createInventory(quantity, "secondbarcode");
        id = productService.getByBarcode("secondbarcode").getId();
        inventoryDto.edit(id, inventoryForm);
        //getting inventory report
        List<ReportsData> dataList = reportsDto.getInventoryReport();
        ReportsData report = dataList.get(0);
        //assertions
        String expectedBrand = "testbrand";
        String expectedCategory = "testcategory";
        int expectedQuantity = 2*quantity;

        assertEquals(expectedBrand, report.getBrand());
        assertEquals(expectedCategory, report.getCategory());
        assertEquals(expectedQuantity, report.getQuantity());
    }
    //Sales Report when start date is after end date
    @Test
    public void testInvalidDatesSales() throws ApiException{
        try {
            //Creating Order
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm product1 = FormHelper.createProduct("barcode1", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(product1);
            ProductForm product2 = FormHelper.createProduct("barcode2", "testbrand", "testcategory",
                    "newName", 1000);
            productDto.add(product2);
            String barcode1 = "barcode1";
            int quantity = 10;
            int id1 = productService.getByBarcode(barcode1).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode1);
            inventoryDto.edit(id1, inventoryForm);
            String barcode2 = "barcode2";
            int quantity2 = 8;
            int id2 = productService.getByBarcode(barcode2).getId();
            InventoryForm inventoryForm2 = FormHelper.createInventory(quantity2, barcode2);
            inventoryDto.edit(id2, inventoryForm2);

            List<OrderItemForm> orderItems = new ArrayList<>();
            OrderItemForm orderItem1 = FormHelper.createOrderItem(barcode1, 2, 1000);
            orderItems.add(orderItem1);

            OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode2, 5, 999);
            orderItems.add(orderItem2);

            orderDto.add(orderItems);
            //getting date filter
            String startDate = "2023-07-10";
            String endDate = "2023-06-10";
            ReportsForm reportsForm = FormHelper.createReports(startDate, endDate);
            //Getting sales report
            List<ReportsData> dataList = reportsDto.getSalesReport(reportsForm);
        } catch (ApiException err){
            assertEquals("Start date cannot be after end date", err.getMessage());
        }
    }
    //Scheduler report when start date is after end date
    @Test
    public void testInvalidDatesScheduler() throws ApiException, InterruptedException {
        try {
            //Creating Order
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm product1 = FormHelper.createProduct("barcode1", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(product1);
            ProductForm product2 = FormHelper.createProduct("barcode2", "testbrand", "testcategory",
                    "newName", 1000);
            productDto.add(product2);
            String barcode1 = "barcode1";
            int quantity = 10;
            int id1 = productService.getByBarcode(barcode1).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode1);
            inventoryDto.edit(id1, inventoryForm);
            String barcode2 = "barcode2";
            int quantity2 = 8;
            int id2 = productService.getByBarcode(barcode2).getId();
            InventoryForm inventoryForm2 = FormHelper.createInventory(quantity2, barcode2);
            inventoryDto.edit(id2, inventoryForm2);

            List<OrderItemForm> orderItems = new ArrayList<>();
            OrderItemForm orderItem1 = FormHelper.createOrderItem(barcode1, 2, 1000);
            orderItems.add(orderItem1);

            OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode2, 5, 999);
            orderItems.add(orderItem2);

            int orderId = orderDto.add(orderItems);
            //Running Scheduler
            reportsDto.generateDailyReport();
            //getting by date filter
            String startDate = "2023-07-10";
            String endDate = "2023-06-10";
            ReportsForm reportsForm = FormHelper.createReports(startDate, endDate);

            List<SchedulerPojo> pojoList = reportsDto.getByDate(reportsForm);
        } catch (ApiException err){
            assertEquals("Start date cannot be after end date", err.getMessage());
        }
    }
}
