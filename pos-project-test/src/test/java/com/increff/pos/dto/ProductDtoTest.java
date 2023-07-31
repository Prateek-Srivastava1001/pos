package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import io.swagger.annotations.Api;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    ProductDto productDto;
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductService service;
    //ADD METHOD TESTS...
    //This function will test both add() and get() methods
    @Test
    public void testAdd() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                                                        " TesTNaMe ", 1000);
        productDto.add(productForm);

        String expectedBarcode = "testbarcode";
        String expectedBrand = "testbrand";
        String expectedCategory = "testcategory";
        String expectedName = "testname";
        double expectedMrp = 1000;

        ProductData data = productDto.get(service.getByBarcode(expectedBarcode).getId());
        assertEquals(expectedBarcode, data.getBarcode());
        assertEquals(expectedBrand, data.getBrand());
        assertEquals(expectedCategory, data.getCategory());
        assertEquals(expectedName, data.getName());
        assertEquals(expectedMrp, data.getMrp());
    }
    // product cannot be inserted if its brand-category does not exist in the Brand 	Master
    @Test
    public void testAddingNonExistentBrandCategoryCombination() throws ApiException {
        try {
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Brand-Category combination not found", err.getMessage());
        }
    }
    //valid brand but invalid category
    @Test
    public void testAddingInvalidCategory() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " otherCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Brand-Category combination not found", err.getMessage());
        }
    }
    //invalid brand but valid category
    @Test
    public void testAddingInvalidBrand() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", "invalidBrand", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Brand-Category combination not found", err.getMessage());
        }
    }
    //empty barcode not supported
    @Test
    public void testAddingEmptyBarcode() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Barcode cannot be empty", err.getMessage());
        }
    }
    //barcode is limited to 15 characters
    @Test
    public void testAddingLongBarcode() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("barcodeMoreThan15CharactersLong",
                    " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Barcode cannot be more than 15 characters long", err.getMessage());
        }
    }
    //barcode should be unique
    @Test
    public void testUniqueBarcode() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("barcode",
                    " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            productForm = FormHelper.createProduct("barcode", "testbrand", "testcategory",
                    "newName", 100);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Product Barcode already exists", err.getMessage());
        }
    }
    //empty name not supported
    @Test
    public void testAddingEmptyName() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                    "", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("name cannot be empty", err.getMessage());
        }
    }
    //name upto 50 characters supported
    @Test
    public void testAddingLongName() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", " TesTBrand ", " TestCaTegoRy ",
                    "Product Name More Than Fifty Characters Long Should Throw API Exception", 1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("Product name cannot be more than 50 characters long", err.getMessage());
        }
    }
    //mrp should be stored upto two decimal places in the database
    @Test
    public void testTrimDoubleValue() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000.234223);
        productDto.add(productForm);
        double expectedMrp = 1000.23;
        String expectedBarcode = "testbarcode";
        ProductPojo pojo = service.getByBarcode(expectedBarcode);
        assertEquals(expectedMrp, pojo.getMrp());
    }
    //mrp should not be negative
    @Test
    public void testNegativeMrp() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", " TesTBrand ", " TestCaTegoRy ",
                    "testName", -1000);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("MRP cannot be negative. This is not how math works...", err.getMessage());
        }
    }
    // mrp should not be more than 1000000000
    @Test
    public void testLargeMrp() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", " TesTBrand ", " TestCaTegoRy ",
                    "testName", 1000000001);
            productDto.add(productForm);
        } catch (ApiException err){
            Assert.assertEquals("MRP cannot be more than 1000000000", err.getMessage());
        }
    }
    //GETALL METHOD TESTS...
    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        productForm = FormHelper.createProduct("secondBarcode", "testbrand", "testcategory",
                                                "secondName", 1000);
        productDto.add(productForm);

        List<ProductData> dataList = productDto.getAll();
        assertEquals(2, dataList.size());
    }
    //UPDATE METHOD TESTS...
    @Test
    public void update() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);

        String expectedBarcode = "testbarcode";
        int id = service.getByBarcode(expectedBarcode).getId();
        ProductForm updatedForm = FormHelper.createProduct(expectedBarcode, "testbrand", "testcategory",
                                                            " newNaMe ", 2934.323232);
        productDto.update(id, updatedForm);
        ProductData data = productDto.get(id);
        String expectedName = "newname";
        String expectedBrand = "testbrand";
        String expectedCategory = "testcategory";
        double expectedMrp = 2934.32;

        assertEquals(expectedBarcode, data.getBarcode());
        assertEquals(expectedName, data.getName());
        assertEquals(expectedBrand, data.getBrand());
        assertEquals(expectedCategory, data.getCategory());
        assertEquals(expectedMrp, data.getMrp());
    }
    // Updating to non-existent brand category combination
    @Test
    public void testNonExistentBrandCategoryCombinationOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", "TesTBrand", "TestCaTegoRy",
                    "TesTNaMe", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct(expectedBarcode, "testbrand", "newcategory",
                    " newNaMe ", 2934.323232);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("Brand-Category combination not found", err.getMessage());
        }
    }
    // empty barcode on update
    @Test
    public void testEmptyBarcodeonUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", "TesTBrand", "TestCaTegoRy",
                    "TesTNaMe", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct("", "testbrand", "testcategory",
                    " newNaMe ", 2934.323232);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("Barcode cannot be empty", err.getMessage());
        }
    }
    // barcode>15 characters long test on update
    @Test
    public void testLongBarcodeOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", "TesTBrand", "TestCaTegoRy",
                    "TesTNaMe", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct("barcodeMoreThan15CharactersLong",
                    "testbrand", "testcategory",
                    " newNaMe ", 2934.323232);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("Barcode cannot be more than 15 characters long", err.getMessage());
        }
    }
    // name should not be empty
    @Test
    public void testEmptyNameOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", "TesTBrand", "TestCaTegoRy",
                    "TesTNaMe", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct(expectedBarcode, "testbrand", "testcategory",
                    "", 2934.323232);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("name cannot be empty", err.getMessage());
        }
    }
    // test name>50 characters on update
    @Test
    public void testLongNameOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("TeStBarCode", "TesTBrand", "TestCaTegoRy",
                    "TesTNaMe", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct(expectedBarcode, "testbrand", "testcategory",
                    "Product Name More Than Fifty Characters Should Throw Api Exception", 2934.323232);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("Product name cannot be more than 50 characters long", err.getMessage());
        }
    }
    //Negative MRP on update
    @Test
    public void testNegativeMrpOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct(expectedBarcode, "testbrand", "testcategory",
                    " newNaMe ", -10);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("MRP cannot be negative. This is not how math works...", err.getMessage());
        }
    }
    // very high value of mrp on update
    @Test
    public void testLargeMrpOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct(expectedBarcode, "testbrand", "testcategory",
                    " newNaMe ", 1000000001);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("MRP cannot be more than 1000000000", err.getMessage());
        }
    }
    // Existing barcode on update
    @Test
    public void testExistingBarcodeOnUpdate() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            productForm = FormHelper.createProduct("newbarcode", " TesTBrand ", " TestCaTegoRy ",
                    "newName", 1000);
            productDto.add(productForm);

            String expectedBarcode = "testbarcode";
            int id = service.getByBarcode(expectedBarcode).getId();
            ProductForm updatedForm = FormHelper.createProduct("newbarcode", "testbrand", "testcategory",
                    " newNaMe ", 2934.323232);
            productDto.update(id, updatedForm);
        } catch (ApiException err){
            Assert.assertEquals("Product Barcode already exists", err.getMessage());
        }
    }
    //Test get method for non-existent id
    @Test
    public void testGetForNonExistentId() throws ApiException{
        try {
            productDto.get(1);
        } catch (ApiException err){
            Assert.assertEquals("Product Details with given id does not exist id: 1", err.getMessage());
        }
    }
}
