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
    @Test(expected = ApiException.class)
    public void testAddingNonExistentBrandCategoryCombination() throws ApiException {
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
    }
    //valid brand but invalid category
    @Test(expected = ApiException.class)
    public void testAddingInvalidCategory() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " otherCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
    }
    //invalid brand but valid category
    @Test(expected = ApiException.class)
    public void testAddingInvalidBrand() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", "invalidBrand", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
    }
    //empty barcode not supported
    @Test(expected = ApiException.class)
    public void testAddingEmptyBarcode() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
    }
    //barcode is limited to 15 characters
    @Test(expected = ApiException.class)
    public void testAddingLongBarcode() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("barcodeMoreThan15CharactersLong",
                " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
    }
    //barcode should be unique
    @Test(expected = ApiException.class)
    public void testUniqueBarcode() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("barcode",
                " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        productForm = FormHelper.createProduct("barcode", "testbrand", "testcategory",
                                                "newName", 100);
        productDto.add(productForm);
    }
    //empty name not supported
    @Test(expected = ApiException.class)
    public void testAddingEmptyName() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("  TeStBarCode  ", " TesTBrand ", " TestCaTegoRy ",
                "", 1000);
        productDto.add(productForm);
    }
    //name upto 50 characters supported
    @Test(expected = ApiException.class)
    public void testAddingLongName() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("TeStBarCode", " TesTBrand ", " TestCaTegoRy ",
                "Product Name More Than Fifty Characters Long Should Throw API Exception", 1000);
        productDto.add(productForm);
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
    @Test(expected = ApiException.class)
    public void testNegativeMrp() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("TeStBarCode", " TesTBrand ", " TestCaTegoRy ",
                "testName", -1000);
        productDto.add(productForm);
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
    @Test(expected = ApiException.class)
    public void testNonExistentBrandCategoryCombinationOnUpdate() throws ApiException{
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
    }
    // empty barcode on update
    @Test(expected = ApiException.class)
    public void testEmptyBarcodeonUpdate() throws ApiException{
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
    }
    @Test(expected = ApiException.class)
    public void testLongBarcodeOnUpdate() throws ApiException{
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
    }
    @Test(expected = ApiException.class)
    public void testEmptyNameOnUpdate() throws ApiException{
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
    }
    @Test(expected = ApiException.class)
    public void testLongNameOnUpdate() throws ApiException{
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
    }
    //Negative MRP on update
    @Test(expected = ApiException.class)
    public void testNegativeMrpOnUpdate() throws ApiException{
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
    }
    // Existing barcode on update
    @Test(expected = ApiException.class)
    public void testExistingBarcodeOnUpdate() throws ApiException{
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
    }
    //Test get method for non-existent id
    @Test(expected = ApiException.class)
    public void testGetForNonExistentId() throws ApiException{
        productDto.get(1);
    }
}
