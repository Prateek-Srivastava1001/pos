package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemServiceTest extends AbstractUnitTest {
    @Autowired
    OrderItemService service;
    @Autowired
    ProductService productService;
    // add and getAll method test
    @Test
    public void testAdd() throws ApiException{
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(1);
        productPojo.setName("name");
        productPojo.setMrp(1000);
        productService.add(productPojo);
        int id = productService.getByBarcode("barcode").getId();

        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrder_id(1);
        pojo.setProduct_id(id);
        pojo.setQuantity(2);
        pojo.setSelling_price(1000);
        service.add(pojo);

        List<OrderItemData> pojoList = service.getAll(1);
        assertEquals(1, pojoList.size());
        OrderItemData data = pojoList.get(0);
        assertEquals(1, data.getOrder_id());
        assertEquals(id, data.getProduct_id());
        assertEquals(2, data.getQuantity());
        assertEquals(1000, data.getSelling_price(),0);
    }
    //adding duplicate barcode (product_id)
    @Test
    public void testAddDuplicateProduct() throws ApiException{
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(1);
            productPojo.setName("name");
            productPojo.setMrp(1000);
            productService.add(productPojo);
            int id = productService.getByBarcode("barcode").getId();

            OrderItemPojo pojo = new OrderItemPojo();
            pojo.setOrder_id(1);
            pojo.setProduct_id(id);
            pojo.setQuantity(2);
            pojo.setSelling_price(1000);
            service.add(pojo);

            OrderItemPojo newPojo = new OrderItemPojo();
            newPojo.setOrder_id(1);
            newPojo.setProduct_id(id);
            newPojo.setQuantity(3);
            newPojo.setSelling_price(500);
            service.add(newPojo);
        } catch (ApiException err){
            TestCase.assertEquals("Frontend Validation Breach: Duplicate barcodes detected", err.getMessage());
        }
    }
}
