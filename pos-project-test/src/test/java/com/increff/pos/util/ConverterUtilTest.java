package com.increff.pos.util;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.BrandData;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConverterUtilTest extends AbstractUnitTest {
    ConverterUtil util;

    // test BrandPojo -> BrandData
    @Test
    public void testBrandPojoToBrandData(){
        BrandPojo pojo = new BrandPojo();
        pojo.setId(1);
        pojo.setBrand("brand");
        pojo.setCategory("category");

        BrandData data = util.convert(pojo);

        assertEquals(1, data.getId());
        assertEquals("brand", data.getBrand());
        assertEquals("category", data.getCategory());
    }
    // test BrandData -> BrandPojo

}
