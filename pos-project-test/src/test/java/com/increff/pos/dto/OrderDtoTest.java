package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.*;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    OrderDto orderDto;
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    ProductService productService;

    //ADD, ADDITEMS and GET METHODS TEST...
    @Test
    public void testAdd() throws ApiException{
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
        List<OrderItemData> createdOrderList = orderDto.getOrderItemsByOrderId(orderId);

        assertEquals(2, createdOrderList.size());
        //Inventory should reduce in inventory table
        InventoryData inventoryData1 = inventoryDto.get(id1);
        InventoryData inventoryData2 = inventoryDto.get(id2);
        assertEquals(8, inventoryData1.getQuantity());
        assertEquals(3, inventoryData2.getQuantity());
    }
    //Empty order creation not supported
    @Test
    public void testEmptyOrderCreation() throws ApiException{
        try {
            List<OrderItemForm> forms = new ArrayList<>();
            orderDto.add(forms);
        }catch (ApiException err){
            assertEquals("Empty Order List Not Supported", err.getMessage());
        }
    }
    //Barcode not present in product table
    @Test
    public void testNonExistentBarcode() throws ApiException{
        try {
            List<OrderItemForm> forms = new ArrayList<>();
            OrderItemForm form = FormHelper.createOrderItem("barcode", 1, 10);
            forms.add(form);
            orderDto.add(forms);
        } catch (ApiException err){
            assertEquals("Product with given barcode not found", err.getMessage());
        }
    }
    //Duplicate barcode addition
    @Test
    public void testDuplicateBarcode() throws ApiException{
        try {
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

            OrderItemForm orderItem2 = FormHelper.createOrderItem(barcode1, 5, 999);
            orderItems.add(orderItem2);

            int orderId = orderDto.add(orderItems);
            List<OrderItemData> createdOrderList = orderDto.getOrderItemsByOrderId(orderId);
        } catch (ApiException err){
            assertEquals("Frontend Validation Breach: Duplicate barcodes detected", err.getMessage());
        }
    }
    //generate invoice for non-existent orderId
    @Test
    public void testInvoicingNonExistentOrderId() throws ApiException{
        try {
            orderDto.generateInvoiceForOrder(1);
        } catch (ApiException err){
            assertEquals("Order with given id not found", err.getMessage());
        }
    }
    //enough inventory not present
    @Test
    public void testInventoryNotPresent() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            List<OrderItemForm> orderItems = new ArrayList<>();
            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 11, 1000);
            orderItems.add(orderItem);

            orderDto.add(orderItems);
        } catch (ApiException err){
            assertEquals("Not enough quantity is present in the inventory.", err.getMessage());
        }
    }
    //selling price should not be more than mrp
    @Test
    public void testHigherSellingPrice() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            List<OrderItemForm> orderItems = new ArrayList<>();
            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 1, 10000);
            orderItems.add(orderItem);

            orderDto.add(orderItems);
        } catch (ApiException err){
            assertEquals("Selling price cannot be more than MRP.", err.getMessage());
        }
    }
    //quantity should be strictly positive (zero quantity not supported)
    @Test
    public void testZeroQuantity() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            List<OrderItemForm> orderItems = new ArrayList<>();
            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 0, 100);
            orderItems.add(orderItem);

            orderDto.add(orderItems);
        } catch (ApiException err){
            assertEquals("Please enter positive value of quantity", err.getMessage());
        }
    }
    //GET ALL ORDERS
    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        String barcode = "testbarcode";
        int quantity = 10;
        int id = productService.getByBarcode(barcode).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
        inventoryDto.edit(id, inventoryForm);

        List<OrderItemForm> orderItems = new ArrayList<>();
        OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 1, 1000);
        orderItems.add(orderItem);

        orderDto.add(orderItems);
        orderDto.add(orderItems);
        List<OrderPojo> pojoList = orderDto.getListOrder();
        assertEquals(2, pojoList.size());
    }

    // ...CHECKER METHOD (USED FOR VALIDATION WHILE ADDING PRODUCTS IN CART) TESTS...
    @Test
    public void testChecker() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        String barcode = "testbarcode";
        int quantity = 10;
        int id = productService.getByBarcode(barcode).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
        inventoryDto.edit(id, inventoryForm);

        OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 1, 1000);
        orderDto.checker(orderItem);
    }
    //check non existent barcode
    @Test
    public void testCheckerNonExistentBarcode() throws ApiException{
        try {
            OrderItemForm orderItem = FormHelper.createOrderItem("barcode", 1, 1000);
            orderDto.checker(orderItem);
        } catch (ApiException err){
            assertEquals("Product with given barcode not found", err.getMessage());
        }
    }
    //negative selling price check
    @Test
    public void testCheckerNegativeSellingPrice() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 2, -1000);
            orderDto.checker(orderItem);
        } catch (ApiException err){
            assertEquals("Selling Price cannot be negative", err.getMessage());
        }
    }
    //zero quantity test
    @Test
    public void testCheckerZeroQuantity() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 0, 1000);
            orderDto.checker(orderItem);
        } catch (ApiException err){
            assertEquals("Please enter positive value of quantity", err.getMessage());
        }
    }
    //not enough inventory present
    @Test
    public void testCheckerNotEnoughInventory() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 100, 1000);
            orderDto.checker(orderItem);
        } catch (ApiException err){
            assertEquals("Not enough quantity is present in the inventory.", err.getMessage());
        }
    }
    //Selling price should not be more than mrp
    @Test
    public void testCheckerSellingPriceLimit() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);

            OrderItemForm orderItem = FormHelper.createOrderItem(barcode, 1, 1001);
            orderDto.checker(orderItem);
        } catch (ApiException err){
            assertEquals("Selling price cannot be more than MRP.", err.getMessage());
        }
    }

    //TEST INOVICE GENERATION
    @Test
    public void testInvoiceGeneration() throws Exception{
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
        ResponseEntity<byte[]> response=orderDto.getInvoicePDF(orderId);
        assertNotEquals(null, response);
    }

}
