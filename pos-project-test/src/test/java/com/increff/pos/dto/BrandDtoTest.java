package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto dto;
    @Autowired
    BrandService service;

    //add() function tests
    @Test
    public void addTest() throws ApiException {
        BrandForm form = FormHelper.createBrand("  TestBrand  ", "  TestCategory  ");
        dto.add(form);

        String expectedBrand = "testbrand";
        String expectedCategory = "testcategory";

        BrandPojo pojo = service.combinationChecker(expectedBrand, expectedCategory);
        assertEquals(expectedBrand, pojo.getBrand());
        assertEquals(expectedCategory, pojo.getCategory());
    }
    @Test(expected = ApiException.class)
    public void testEmptyBrandAddition() throws ApiException{
        BrandForm form = FormHelper.createBrand("","testcategory");
        dto.add(form);
    }
    @Test(expected = ApiException.class)
    public void testEmptyCategoryAddition() throws ApiException{
        BrandForm form = FormHelper.createBrand("testbrand", "");
        dto.add(form);
    }
    //brand>30 characters test
    @Test(expected = ApiException.class)
    public void testLargeBrand() throws ApiException{
        BrandForm form = FormHelper.createBrand("This brand name is supposed to be more than 30 characters long",
                "testcategory");
        dto.add(form);
    }
    @Test(expected = ApiException.class)
    public void testLargeCategory() throws ApiException{
        BrandForm form = FormHelper.createBrand("testbrand",
                "This category name is supposed to be more than 50 characters long");
        dto.add(form);
    }
    //duplicate brand-category combination
    @Test(expected = ApiException.class)
    public void testDuplicateEntry() throws ApiException{
        BrandForm form = FormHelper.createBrand("testbrand","testcategory");
        dto.add(form);
        BrandForm duplicate = FormHelper.createBrand("testbrand","testcategory");
        dto.add(duplicate);
    }
    //get(id) function test
    @Test
    public void getTest() throws ApiException{
        BrandForm form = FormHelper.createBrand("testbrand", "testcategory");
        dto.add(form);
        int id = service.combinationChecker(form.getBrand(), form.getCategory()).getId();
        BrandData data = dto.get(id);
        assertEquals(form.getBrand(), data.getBrand());
        assertEquals(form.getCategory(), data.getCategory());
    }
    @Test(expected = ApiException.class)
    public void testGettingNonExistingId() throws ApiException{
        BrandData data = dto.get(1);
    }
    //getAll() function test
    @Test
    public void getAllTest() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand1", "category1");
        dto.add(form);
        form = FormHelper.createBrand("brand2", "category2");
        dto.add(form);
        List<BrandData> dataList = dto.getAll();
        assertEquals(2,dataList.size());
    }
    //Update function test
    @Test
    public void updateTest() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand", "category");
        dto.add(form);
        int id = service.combinationChecker(form.getBrand(), form.getCategory()).getId();
        BrandForm updatedForm = FormHelper.createBrand("updatedBrand", "updatedCategory");
        String expectedBrand="updatedbrand";
        String expectedCategory="updatedcategory";
        dto.update(id, updatedForm);
        BrandData data = dto.get(id);
        assertEquals(expectedBrand, data.getBrand());
        assertEquals(expectedCategory, data.getCategory());
    }
    //test updating for the id not present in table
    @Test(expected = ApiException.class)
    public void testUpdatingNonExistingId() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand", "category");
        dto.update(1,form);
    }
    //Updating to duplicate value
    @Test(expected = ApiException.class)
    public void testUpdatingDuplicate() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand1", "category1");
        dto.add(form);
        BrandForm form1 = FormHelper.createBrand("brand2", "category2");
        dto.add(form1);
        int id = service.combinationChecker(form1.getBrand(), form1.getCategory()).getId();
        dto.update(id, form);
    }
    // Updating to empty brand
    @Test(expected = ApiException.class)
    public void testUpdatingEmptyBrand() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand1", "category1");
        dto.add(form);
        BrandForm form1 = FormHelper.createBrand("", "category2");
        int id = service.combinationChecker(form.getBrand(), form.getCategory()).getId();
        dto.update(id, form1);
    }
    // Updating to empty category
    @Test(expected = ApiException.class)
    public void testUpdatingEmptyCategory() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand1", "category1");
        dto.add(form);
        BrandForm form1 = FormHelper.createBrand("brand2", "");
        int id = service.combinationChecker(form.getBrand(), form.getCategory()).getId();
        dto.update(id, form1);
    }
    // Updating to brand value having length more than 30 characters
    @Test(expected = ApiException.class)
    public void testUpdatingLongBrand() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand1", "category1");
        dto.add(form);
        BrandForm form1 = FormHelper.createBrand("This brand name is supposed to be more than 30 characters long",
                                            "category2");
        int id = service.combinationChecker(form.getBrand(), form.getCategory()).getId();
        dto.update(id, form1);
    }
    @Test(expected = ApiException.class)
    public void testUpdatingLongCategory() throws ApiException{
        BrandForm form = FormHelper.createBrand("brand1", "category1");
        dto.add(form);
        BrandForm form1 = FormHelper.createBrand("brand2",
                "This category name is supposed to be more than 50 characters long");
        int id = service.combinationChecker(form.getBrand(), form.getCategory()).getId();
        dto.update(id, form1);
    }
}
