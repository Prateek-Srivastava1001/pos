package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDaoTest extends AbstractUnitTest {
    @Autowired
    InventoryDao dao;

    // insert and select(id) methods test
    @Test
    public void testInsert(){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(1);
        pojo.setQuantity(10);
        dao.insert(pojo);

        InventoryPojo gotPojo = dao.select(1);
        assertEquals(10, gotPojo.getQuantity());
    }
    // selectAll method test
    @Test
    public void testSelectAll(){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(1);
        pojo.setQuantity(10);
        dao.insert(pojo);

        InventoryPojo newPojo = new InventoryPojo();
        newPojo.setId(2);
        newPojo.setQuantity(20);
        dao.insert(newPojo);

        List<InventoryPojo> pojoList = dao.selectAll();

        assertEquals(2, pojoList.size());
    }
}
