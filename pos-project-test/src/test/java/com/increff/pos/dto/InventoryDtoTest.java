package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductService productService;
    //Inventory with quantity 0 is auto initialized when product is created.

    //GET METHOD TEST
    @Test
    public void testGet() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        int id = productService.getByBarcode("testbarcode").getId();
        InventoryData data = inventoryDto.get(id);
        int expectedQuantity = 0;
        assertEquals(id, data.getId());
        assertEquals(expectedQuantity, data.getQuantity());
    }
    //GETALL METHOD TEST
    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        productForm = FormHelper.createProduct("newbarcode", "testbrand", "testcategory",
                                                "newname", 500);
        productDto.add(productForm);
        List<InventoryData> dataList = inventoryDto.getAll();
        assertEquals(2, dataList.size());
    }
    //EDIT METHOD TESTS...
    @Test
    public void testEdit() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        String barcode = "testbarcode";
        int expectedQuantity = 10;
        int id = productService.getByBarcode(barcode).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(expectedQuantity, barcode);
        inventoryDto.edit(id, inventoryForm);
        InventoryData data = inventoryDto.get(id);
        assertEquals(expectedQuantity, data.getQuantity());
    }
    // quantity should not be negative
    @Test
    public void testNegativeQuantityEdit() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = -10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);
        }catch (ApiException err){
            assertEquals("Quantity cannot be negative", err.getMessage());
        }
    }
    // inventory cannot be edited if product for corresponding does not exist
    @Test
    public void testNonExistentIdEdit() throws ApiException{
        try {
            InventoryForm inventoryForm = FormHelper.createInventory(10, "barcode");
            inventoryDto.edit(1, inventoryForm);
        }catch (ApiException err){
            assertEquals("Product Details with given id does not exist id: 1", err.getMessage());
        }
    }
    // large value of quantity on edit
    @Test
    public void testLargeQuantityOnEdit() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10000001;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.edit(id, inventoryForm);
        } catch (ApiException err){
            assertEquals("Quantity cannot be more than 10000000", err.getMessage());
        }
    }

    //EDITBYUPLOAD METHOD TESTS...
    @Test
    public void testEditByUpload() throws ApiException{
        BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
        brandDto.add(brandForm);
        ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                " TesTNaMe ", 1000);
        productDto.add(productForm);
        String barcode = "testbarcode";
        int quantity = 10;
        int id = productService.getByBarcode(barcode).getId();
        InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
        inventoryDto.editByUpload(inventoryForm);
        inventoryDto.editByUpload(inventoryForm);
        int expectedQuantity = 2*quantity;

        InventoryData data = inventoryDto.get(id);
        assertEquals(expectedQuantity, data.getQuantity());
    }
    // test adding negative quantity by upload
    @Test
    public void testNegativeQuantityEditByUpload() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = -10;
            int id = productService.getByBarcode(barcode).getId();
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.editByUpload(inventoryForm);
        } catch (ApiException err){
            assertEquals("Quantity cannot be negative", err.getMessage());
        }
    }
    // large quantity edit on upload
    @Test
    public void testLargeQuantityEditByUpload() throws ApiException{
        try {
            BrandForm brandForm = FormHelper.createBrand("TestBrand", "TestCategory");
            brandDto.add(brandForm);
            ProductForm productForm = FormHelper.createProduct("testbarcode", " TesTBrand ", " TestCaTegoRy ",
                    " TesTNaMe ", 1000);
            productDto.add(productForm);
            String barcode = "testbarcode";
            int quantity = 10000001;
            InventoryForm inventoryForm = FormHelper.createInventory(quantity, barcode);
            inventoryDto.editByUpload(inventoryForm);
        } catch (ApiException err){
            assertEquals("Quantity out of bound", err.getMessage());
        }
    }
    // inventory cannot be changed by upload method if product for corresponding barcode does not exists
    @Test
    public void testNonExistentBarcodeEditByUpload() throws ApiException{
        try {
            InventoryForm inventoryForm = FormHelper.createInventory(10, "nonExistent");
            inventoryDto.editByUpload(inventoryForm);
        } catch (ApiException err){
            assertEquals("Product with given barcode not found", err.getMessage());
        }
    }

}
