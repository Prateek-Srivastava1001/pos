package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    BrandService brandService;

    // Add and CombinationChecker methods test
    @Test
    public void testBrandAddition() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        String expectedBrand = "brand";
        String expectedCategory = "category";

        BrandPojo gotPojo = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory());

        assertEquals(expectedBrand, gotPojo.getBrand());
        assertEquals(expectedCategory, gotPojo.getCategory());
    }
    // Empty Brand
    @Test
    public void testEmptyBrandAddition() throws ApiException{
        try {
            BrandPojo pojo = new BrandPojo();
            pojo.setBrand("");
            pojo.setCategory("category");

            brandService.add(pojo);
        } catch (ApiException err){
            assertEquals("Brand cannot be empty", err.getMessage());
        }
    }
    // Empty Category
    @Test
    public void testEmptyCategoryAddition() throws ApiException{
        try {
            BrandPojo pojo = new BrandPojo();
            pojo.setBrand("brand");
            pojo.setCategory("");

            brandService.add(pojo);
        } catch (ApiException err){
            assertEquals("Category cannot be empty", err.getMessage());
        }
    }
    //Duplicate brand category test
    @Test
    public void testDuplicateEntry() throws ApiException{
        try {
            BrandPojo pojo = new BrandPojo();
            pojo.setBrand("brand");
            pojo.setCategory("category");

            brandService.add(pojo);
            brandService.add(pojo);
        } catch (ApiException err){
            assertEquals("Brand - Category combination already exists", err.getMessage());
        }
    }
    // getAll method test
    @Test
    public void testGetAll() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        BrandPojo newPojo = new BrandPojo();
        newPojo.setBrand("newbrand");
        newPojo.setCategory("newcategory");
        brandService.add(newPojo);

        List<BrandPojo> brandList =  brandService.getAll();

        assertEquals(2, brandList.size());
    }
    //getCheck method test
    @Test
    public void testGet() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        int id = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory()).getId();
        BrandPojo gotPojo = brandService.getCheck(id);
        String expectedBrand = "brand";
        String expectedCategory = "category";

        assertEquals(expectedBrand, gotPojo.getBrand());
        assertEquals(expectedCategory, gotPojo.getCategory());
    }
    // getting non-existent id
    @Test
    public void testGetNonExistentId() throws ApiException{
        try {
            brandService.getCheck(1);
        } catch (ApiException err){
            assertEquals("Brand-Category with given id does not exist id: 1", err.getMessage());
        }
    }
    // Update method tests...
    @Test
    public void testUpdate() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        int id = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory()).getId();

        BrandPojo newPojo = new BrandPojo();
        newPojo.setBrand("newbrand");
        newPojo.setCategory("newcategory");

        brandService.update(id, newPojo);
        BrandPojo gotPojo = brandService.getCheck(id);

        String expectedBrand = "newbrand";
        String expectedCategory = "newcategory";

        assertEquals(expectedBrand, gotPojo.getBrand());
        assertEquals(expectedCategory, gotPojo.getCategory());
    }
    //empty brand on update
    @Test
    public void testUpdateEmptyBrand() throws ApiException{
        try {
            BrandPojo pojo = new BrandPojo();
            pojo.setBrand("brand");
            pojo.setCategory("category");

            brandService.add(pojo);
            int id = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory()).getId();

            BrandPojo newPojo = new BrandPojo();
            newPojo.setBrand("");
            newPojo.setCategory("newcategory");

            brandService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("Brand cannot be empty", err.getMessage());
        }
    }
    //empty category on update
    @Test
    public void testUpdateEmptyCategory() throws ApiException{
        try {
            BrandPojo pojo = new BrandPojo();
            pojo.setBrand("brand");
            pojo.setCategory("category");

            brandService.add(pojo);
            int id = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory()).getId();

            BrandPojo newPojo = new BrandPojo();
            newPojo.setBrand("newbrand");
            newPojo.setCategory("");

            brandService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("Category cannot be empty", err.getMessage());
        }
    }
    //brand-category combination already exist on update
    @Test
    public void testUpdateExistingBrandCategory() throws ApiException{
        try {
            BrandPojo pojo = new BrandPojo();
            pojo.setBrand("brand");
            pojo.setCategory("category");

            brandService.add(pojo);
            BrandPojo secondPojo = new BrandPojo();
            secondPojo.setBrand("secondbrand");
            secondPojo.setCategory("secondcategory");
            brandService.add(secondPojo);
            int id = brandService.combinationChecker(secondPojo.getBrand(), secondPojo.getCategory()).getId();

            BrandPojo newPojo = new BrandPojo();
            newPojo.setBrand("brand");
            newPojo.setCategory("category");

            brandService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("Brand - Category combination already exists", err.getMessage());
        }
    }
    // combination checker - non existent brand category combination
    @Test
    public void testCombinationCheckerForNonExistentCombination() throws ApiException{
        try {
            brandService.combinationChecker("brand", "category");
        } catch (ApiException err){
            assertEquals("Brand-Category combination not found", err.getMessage());
        }
    }
}
