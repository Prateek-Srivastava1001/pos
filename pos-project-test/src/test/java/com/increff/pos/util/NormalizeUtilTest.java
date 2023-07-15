package com.increff.pos.util;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.UserPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class NormalizeUtilTest extends AbstractUnitTest {
    @Autowired
    NormalizeUtil util;

    // test Normalize BrandPojo
    @Test
    public void testNormalizeBrandPojo(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("  BrAnD  ");
        pojo.setCategory("   cAtEgORy   ");

        String expectedBrand = "brand";
        String expectedCategory = "category";
        util.normalize(pojo);

        assertEquals(expectedBrand, pojo.getBrand());
        assertEquals(expectedCategory, pojo.getCategory());
    }
    // test normalize ProductForm
    @Test
    public void testNormalizeProductForm(){
        ProductForm form = FormHelper.createProduct("  BaRcODe  ", "  bRAnD  ", "  CaTeGoRY  ",
                                                        "  NaMe  ", 2000);
        String expectedBarcode = "barcode";
        String expectedBrand = "brand";
        String expectedCategory = "category";
        String expectedName = "name";
        double expectedMrp = 2000;

        util.normalize(form);

        assertEquals(expectedBarcode, form.getBarcode());
        assertEquals(expectedBrand, form.getBrand());
        assertEquals(expectedCategory, form.getCategory());
        assertEquals(expectedName, form.getName());
        assertEquals(expectedMrp, form.getMrp(), 0);
    }
    // test normalize OrderItemForm
    @Test
    public void testNormalizeOrderItemForm(){
        OrderItemForm form = FormHelper.createOrderItem("  BaRcoDE  ", 1, 100);

        util.normalize(form);
        assertEquals("barcode", form.getBarcode());
    }
    // test normalize UserPojo
    @Test
    public void testNormalizeUserPojo(){
        UserPojo pojo = new UserPojo();
        pojo.setEmail("  RaNdoM@GmAIL.com  ");
        pojo.setPassword("  PaSSWorD  ");
        pojo.setRole("  SuPeRVisOR  ");

        String expectedEmail = "random@gmail.com";
        String expectedPassword = "  PaSSWorD  "; // Password will not be normalized
        String expectedRole = "supervisor";

        util.normalize(pojo);

        assertEquals(expectedEmail, pojo.getEmail());
        assertEquals(expectedPassword, pojo.getPassword());
        assertEquals(expectedRole, pojo.getRole());
    }
}
