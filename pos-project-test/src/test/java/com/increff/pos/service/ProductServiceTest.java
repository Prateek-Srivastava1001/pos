package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService productService;
    // add method and getByBarcode method test
    @Test
    public void testAdd() throws ApiException{
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setName("name");
        pojo.setMrp(1000);

        productService.add(pojo);

        ProductPojo gotPojo = productService.getByBarcode("barcode");

        String expectedBarcode = "barcode";
        int expectedBrandCategory = 1;
        String expectedName = "name";
        double expectedMrp = 1000;
        assertEquals(expectedBarcode, gotPojo.getBarcode());
        assertEquals(expectedBrandCategory, gotPojo.getBrand_category());
        assertEquals(expectedMrp, gotPojo.getMrp());
        assertEquals(expectedName, gotPojo.getName());
    }
    // empty barcode addition test
    @Test
    public void testAddEmptyBarcode() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);

            productService.add(pojo);
        } catch (ApiException err){
            assertEquals("Barcode cannot be empty", err.getMessage());
        }
    }
    // empty name addition
    @Test
    public void testAddEmptyName() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("");
            pojo.setMrp(1000);

            productService.add(pojo);
        } catch (ApiException err){
            assertEquals("name cannot be empty", err.getMessage());
        }
    }
    //negative mrp addition
    @Test
    public void testAddNegativeMrp() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(-1000);

            productService.add(pojo);
        } catch (ApiException err){
            assertEquals("MRP cannot be negative. This is not how math works...", err.getMessage());
        }
    }
    // mrp should be less than 1000000001
    @Test
    public void testLargeMrp() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000000001);

            productService.add(pojo);
        } catch (ApiException err){
            assertEquals("MRP cannot be more than 1000000000", err.getMessage());
        }
    }
    // duplicate barcode addition
    @Test
    public void testAddDuplicate() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);
            productService.add(pojo);

            ProductPojo newPojo = new ProductPojo();
            newPojo.setBarcode("barcode");
            newPojo.setBrand_category(1);
            newPojo.setName("newname");
            newPojo.setMrp(100);
            productService.add(newPojo);
        } catch (ApiException err){
            assertEquals("Product Barcode already exists", err.getMessage());
        }
    }

    // getAll method check
    @Test
    public void testGetAll() throws ApiException{
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setName("name");
        pojo.setMrp(1000);
        productService.add(pojo);

        ProductPojo newPojo = new ProductPojo();
        newPojo.setBarcode("newbarcode");
        newPojo.setBrand_category(1);
        newPojo.setName("newname");
        newPojo.setMrp(100);
        productService.add(newPojo);

        List<ProductPojo> pojoList = productService.getAll();

        assertEquals(2, pojoList.size());
    }

    // getCheck method test
    @Test
    public void testGetCheck() throws ApiException{
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setName("name");
        pojo.setMrp(1000);
        productService.add(pojo);

        int id = productService.getByBarcode("barcode").getId();

        ProductPojo gotPojo = productService.getCheck(id);
        String expectedBarcode = "barcode";
        int expectedBrandCategory = 1;
        String expectedName = "name";
        double expectedMrp = 1000;
        assertEquals(expectedBarcode, gotPojo.getBarcode());
        assertEquals(expectedBrandCategory, gotPojo.getBrand_category());
        assertEquals(expectedMrp, gotPojo.getMrp());
        assertEquals(expectedName, gotPojo.getName());
    }
    //getCheck for non-existent id
    @Test
    public void testGetCheckForNonExistentId() throws ApiException{
        try {
            ProductPojo gotPojo = productService.getCheck(1);
        } catch (ApiException err){
            assertEquals("Product Details with given id does not exist id: 1", err.getMessage());
        }
    }
    // getByBarcode for non-existent barcode
    @Test
    public void testGetByBarcodeForNonExistentBarcode() throws ApiException{
        try {
            productService.getByBarcode("nonExistent");
        } catch (ApiException err){
            assertEquals("Product with given barcode not found", err.getMessage());
        }
    }
    // getByBrand method test
    @Test
    public void testGetByBrand() throws ApiException{
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setName("name");
        pojo.setMrp(1000);
        productService.add(pojo);

        ProductPojo newPojo = new ProductPojo();
        newPojo.setBarcode("newbarcode");
        newPojo.setBrand_category(1);
        newPojo.setName("newname");
        newPojo.setMrp(100);
        productService.add(newPojo);

        List<ProductPojo> pojoList = productService.getByBrand(1);

        assertEquals(2, pojoList.size());
    }
    // Update method tests...
    @Test
    public void testUpdate() throws ApiException{
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode");
        pojo.setBrand_category(1);
        pojo.setName("name");
        pojo.setMrp(1000);

        productService.add(pojo);

        int id = productService.getByBarcode("barcode").getId();

        ProductPojo newPojo = new ProductPojo();
        newPojo.setBarcode("newbarcode");
        newPojo.setBrand_category(2);
        newPojo.setName("newname");
        newPojo.setMrp(200);

        productService.update(id, newPojo);

        ProductPojo gotPojo = productService.getCheck(id);
        String expectedBarcode = "newbarcode";
        String expectedName = "newname";
        int expectedBrandCategory = 2;
        double expectedMrp = 200;

        assertEquals(expectedBarcode, gotPojo.getBarcode());
        assertEquals(expectedName, gotPojo.getName());
        assertEquals(expectedBrandCategory, gotPojo.getBrand_category());
        assertEquals(expectedMrp, gotPojo.getMrp());
    }
    // Updating with empty barcode
    @Test
    public void testUpdateWithEmptyBarcode() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);

            productService.add(pojo);

            int id = productService.getByBarcode("barcode").getId();

            ProductPojo newPojo = new ProductPojo();
            newPojo.setBarcode(""); //empty barcode
            newPojo.setBrand_category(2);
            newPojo.setName("newname");
            newPojo.setMrp(200);

            productService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("Barcode cannot be empty", err.getMessage());
        }
    }
    // empty name on update
    @Test
    public void testEmptyNameOnUpdate() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);

            productService.add(pojo);

            int id = productService.getByBarcode("barcode").getId();

            ProductPojo newPojo = new ProductPojo();
            newPojo.setBarcode("newbarcode");
            newPojo.setBrand_category(2);
            newPojo.setName("");
            newPojo.setMrp(200);

            productService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("name cannot be empty", err.getMessage());
        }
    }
    //Updating with duplicate barcode
    @Test
    public void testDuplicateBarcodeOnUpdate() throws ApiException{
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);

            productService.add(pojo);

            ProductPojo newPojo = new ProductPojo();
            newPojo.setBarcode("newbarcode");
            newPojo.setBrand_category(2);
            newPojo.setName("newname");
            newPojo.setMrp(200);
            productService.add(newPojo);
            int id = productService.getByBarcode("newbarcode").getId();

            newPojo.setBarcode("barcode");
            productService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("Product Barcode already exists", err.getMessage());
        }
    }
    //Updating with negative mrp
    @Test
    public void testNegativeMrpOnUpdate() throws ApiException {
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);

            productService.add(pojo);

            int id = productService.getByBarcode("barcode").getId();

            ProductPojo newPojo = new ProductPojo();
            newPojo.setBarcode("newbarcode");
            newPojo.setBrand_category(2);
            newPojo.setName("newname");
            newPojo.setMrp(-200);

            productService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("MRP cannot be negative. This is not how math works...", err.getMessage());
        }
    }
    // very large mrp on update
    @Test
    public void testLargeMrpOnUpdate() throws ApiException {
        try {
            ProductPojo pojo = new ProductPojo();
            pojo.setBarcode("barcode");
            pojo.setBrand_category(1);
            pojo.setName("name");
            pojo.setMrp(1000);

            productService.add(pojo);

            int id = productService.getByBarcode("barcode").getId();

            ProductPojo newPojo = new ProductPojo();
            newPojo.setBarcode("newbarcode");
            newPojo.setBrand_category(2);
            newPojo.setName("newname");
            newPojo.setMrp(1000000001);

            productService.update(id, newPojo);
        } catch (ApiException err){
            assertEquals("MRP cannot be more than 1000000000", err.getMessage());
        }
    }
}
