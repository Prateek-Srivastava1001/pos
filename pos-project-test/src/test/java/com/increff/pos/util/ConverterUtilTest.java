package com.increff.pos.util;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    // test BrandForm -> BrandPojo
    @Test
    public void testBrandFormToBrandPojo(){
        BrandForm form = FormHelper.createBrand("brand", "category");

        BrandPojo pojo = util.convert(form);

        assertEquals("brand", pojo.getBrand());
        assertEquals("category", pojo.getCategory());
    }
    // test ProductPojo -> ProductData
    @Test
    public void testProductPojoToProductData(){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandPojo.setId(2);
        ProductPojo productPojo = new ProductPojo();
        productPojo.setId(1);
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(2);
        productPojo.setName("name");
        productPojo.setMrp(1000.5);

        ProductData data = util.convert(productPojo, brandPojo);

        assertEquals("brand", data.getBrand());
        assertEquals("category", data.getCategory());
        assertEquals("barcode", data.getBarcode());
        assertEquals(1, data.getId());
        assertEquals(1000.5, data.getMrp(),0);
        assertEquals("name", data.getName());
    }
    // test ProductForm -> ProductPojo
    @Test
    public void testProductFormToProductPojo(){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setId(1);

        ProductForm form = FormHelper.createProduct("barcode", "brand", "category", "name",
                                                        1000.232323);

        ProductPojo productPojo = util.convert(form, brandPojo);

        assertEquals(1000.23, productPojo.getMrp(), 0);
        assertEquals("barcode", productPojo.getBarcode());
        assertEquals(1, productPojo.getBrand_category());
        assertEquals("name", productPojo.getName());
    }
    // test InventoryPojo -> InventoryData
    @Test
    public void testInventoryPojoToInventoryData(){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(1);
        pojo.setQuantity(20);

        InventoryData data = util.convert(pojo);

        assertEquals(1, data.getId());
        assertEquals(20, data.getQuantity());
    }
    // test InventoryForm -> InventoryPojo
    @Test
    public void testInventoryFormToInventoryPojo(){
        InventoryForm form = new InventoryForm();
        form.setQuantity(20);

        InventoryPojo pojo = util.convert(form);

        assertEquals(20, pojo.getQuantity());
    }
    // test UserPojo -> Authentication object
    @Test
    public void testUserPojoToAuthentication(){
        UserPojo pojo = new UserPojo();
        pojo.setEmail("random@gmail.com");
        pojo.setPassword("password");
        pojo.setRole("supervisor");

        Authentication authentication = util.convert(pojo);

        assertNotNull(authentication);
    }
    // test LoginForm -> UserPojo
    @Test
    public void testLoginFormToUserPojo(){
        String[] supervisor = new String[] {"prateek@gmail.com", "supervisor@gmail.com", "admin@gmail.com"};
        LoginForm operatorForm = FormHelper.createUser("random@gmail.com", "password");
        //email not present in supervisor array, hence operator role will be assigned
        UserPojo operatorPojo = util.convert(operatorForm, supervisor);
        assertEquals("random@gmail.com", operatorPojo.getEmail());
        assertEquals("password", operatorPojo.getPassword());
        assertEquals("operator", operatorPojo.getRole());

        LoginForm supervisorForm = FormHelper.createUser("prateek@gmail.com", "password");
        // email is present in supervisor array, hence supervisor role will be assigned
        UserPojo supervisorPojo = util.convert(supervisorForm, supervisor);
        assertEquals("prateek@gmail.com", supervisorPojo.getEmail());
        assertEquals("password", supervisorPojo.getPassword());
        assertEquals("supervisor", supervisorPojo.getRole());

    }
}
