package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
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
    @Test(expected = ApiException.class)
    public void testEmptyBrandAddition() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("");
        pojo.setCategory("category");

        brandService.add(pojo);
    }
    // Empty Category
    @Test(expected = ApiException.class)
    public void testEmptyCategoryAddition() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("");

        brandService.add(pojo);
    }
    //Duplicate brand category test
    @Test(expected = ApiException.class)
    public void testDuplicateEntry() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        brandService.add(pojo);
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
    @Test(expected = ApiException.class)
    public void testGetNonExistentId() throws ApiException{
        brandService.getCheck(1);
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
    @Test(expected = ApiException.class)
    public void testUpdateEmptyBrand() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        int id = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory()).getId();

        BrandPojo newPojo = new BrandPojo();
        newPojo.setBrand("");
        newPojo.setCategory("newcategory");

        brandService.update(id, newPojo);
    }
    //empty category on update
    @Test(expected = ApiException.class)
    public void testUpdateEmptyCategory() throws ApiException{
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand");
        pojo.setCategory("category");

        brandService.add(pojo);
        int id = brandService.combinationChecker(pojo.getBrand(), pojo.getCategory()).getId();

        BrandPojo newPojo = new BrandPojo();
        newPojo.setBrand("newbrand");
        newPojo.setCategory("");

        brandService.update(id, newPojo);
    }
    //brand-category combination already exist on update
    @Test(expected = ApiException.class)
    public void testUpdateExistingBrandCategory() throws ApiException{
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
    }
    // combination checker - non existent brand category combination
    @Test(expected = ApiException.class)
    public void testCombinationCheckerForNonExistentCombination() throws ApiException{
        brandService.combinationChecker("brand", "category");
    }
}
