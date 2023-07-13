package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest extends AbstractUnitTest {
    @Autowired
    ProductDao dao;

    // insert and checkBarcode method test
    @Test
    public void testInsert(){
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setMrp(1000);
        pojo.setName("name");

        dao.insert(pojo);

        ProductPojo gotPojo = dao.checkBarcode("barcode");

        assertEquals("barcode", gotPojo.getBarcode());
        assertEquals(1, gotPojo.getBrand_category());
        assertEquals(1000, gotPojo.getMrp(), 0);
        assertEquals("name", gotPojo.getName());
    }
    //select(id) method test
    @Test
    public void testSelect(){
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setMrp(1000);
        pojo.setName("name");

        dao.insert(pojo);
        int id = dao.checkBarcode("barcode").getId();

        ProductPojo gotPojo = dao.select(id);
        assertEquals("barcode", gotPojo.getBarcode());
        assertEquals(1, gotPojo.getBrand_category());
        assertEquals(1000, gotPojo.getMrp(), 0);
        assertEquals("name", gotPojo.getName());
    }
    // selectAll mehtod test
    @Test
    public void testSelectAll(){
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setMrp(1000);
        pojo.setName("name");
        dao.insert(pojo);

        ProductPojo newPojo = new ProductPojo();
        newPojo.setBarcode("newbarcode");
        newPojo.setBrand_category(1);
        newPojo.setMrp(999);
        newPojo.setName("newname");
        dao.insert(newPojo);

        List<ProductPojo> pojoList = dao.selectAll();

        assertEquals(2, pojoList.size());
    }
    // getByBrand method test
    @Test
    public void testGetByBrand() {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setMrp(1000);
        pojo.setName("name");
        dao.insert(pojo);

        ProductPojo newPojo = new ProductPojo();
        newPojo.setBarcode("newbarcode");
        newPojo.setBrand_category(1);
        newPojo.setMrp(999);
        newPojo.setName("newname");
        dao.insert(newPojo);

        List<ProductPojo> pojoList = dao.getByBrand(1);

        assertEquals(2, pojoList.size());
    }
}
