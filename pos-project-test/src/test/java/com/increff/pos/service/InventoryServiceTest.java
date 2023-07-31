package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    InventoryService service;

    // add and getCheck methods test
    @Test
    public void testAdd() throws ApiException{
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(1);
        pojo.setQuantity(20);
        service.add(pojo);

        InventoryPojo gotPojo = service.getCheck(1);

        assertEquals(20, gotPojo.getQuantity());
    }
    // getCheck for non-existent id
    @Test
    public void testGettingNonExistentId() throws ApiException{
        try {
            service.getCheck(1);
        } catch (ApiException err){
            assertEquals("Product Details with given id does not exist id: 1", err.getMessage());
        }
    }
    // getAll method test
    @Test
    public void testGetAll() throws ApiException{
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(1);
        pojo.setQuantity(20);
        service.add(pojo);

        InventoryPojo newPojo = new InventoryPojo();
        newPojo.setId(2);
        newPojo.setQuantity(10);
        service.add(newPojo);
        List<InventoryPojo> pojoList = service.getAll();
        assertEquals(2, pojoList.size());
    }
    //Update method test
    @Test
    public void testUpdate() throws ApiException{
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(1);
        pojo.setQuantity(20);
        service.add(pojo);

        pojo.setQuantity(30);
        service.update(1, pojo);

        InventoryPojo gotPojo = service.getCheck(1);
        assertEquals(30, gotPojo.getQuantity());
    }
}
