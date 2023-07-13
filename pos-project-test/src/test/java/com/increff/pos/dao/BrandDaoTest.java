package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BrandDaoTest extends AbstractUnitTest {
    @Autowired
    BrandDao dao;

    // insert method test
    @Test
    public void testInsert(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");
        dao.insert(pojo);

        BrandPojo gotPojo = dao.selectAll().get(0);

        assertEquals("brand", gotPojo.getBrand());
        assertEquals("category", gotPojo.getCategory());
    }
    // select(id) method test
    @Test
    public void testSelect(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");
        dao.insert(pojo);

        int id = dao.selectAll().get(0).getId();
        BrandPojo gotPojo = dao.select(id);

        assertEquals("brand", gotPojo.getBrand());
        assertEquals("category", gotPojo.getCategory());
    }
    // selectAll method test
    @Test
    public void testSelectAll(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");
        dao.insert(pojo);

        BrandPojo newPojo = new BrandPojo();
        newPojo.setBrand("newbrand");
        newPojo.setCategory("newcategory");
        dao.insert(newPojo);

       List<BrandPojo> pojoList  = dao.selectAll();
       assertEquals(2, pojoList.size());
    }
    // checkForCombination method test
    @Test
    public void testCheckForCombination(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");
        dao.insert(pojo);

        BrandPojo gotPojo = dao.checkForCombination("brand", "category");
        assertEquals("brand", gotPojo.getBrand());
        assertEquals("category", gotPojo.getCategory());
    }
}
