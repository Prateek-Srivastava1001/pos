package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.util.StringUtil;
@Service
public class ProductService {
    @Autowired
    private ProductDao dao;
    @Autowired
    private InventoryService inventoryService;

    //CREATE
    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException{
        normalize(p);
        InventoryPojo inventory = new InventoryPojo();
        String barcode = p.getBarcode();

        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(p.getMrp()<0){
            throw new ApiException("MRP cannot be negative. This is not how math works...");
        }
        //Brand - Category combination should be unique
        if(dao.checkIfBrandIdExists(p.getBrand_category()) == null){
            throw new ApiException("There is no Brand-Category combination for given data");
        }
        if(dao.checkBarcode(barcode) != null){
            throw new ApiException("Product Barcode already exists");
        }
        dao.insert(p);
        ProductPojo newPojo = dao.checkBarcode(barcode);
        inventory.setId(newPojo.getId());
        inventory.setQuantity(0);
        inventoryService.add(inventory);
    }
    //READ
    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException{
        return getCheck(id);
    }
    //UPDATE
    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException{
        normalize(p);
        //Same checks as that in add
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(p.getMrp()<0){
            throw new ApiException("MRP cannot be negative. This is not how math works...");
        }
        //Brand - Category combination should be unique
        if(dao.checkIfBrandIdExists(p.getBrand_category()) == null){
            throw new ApiException("There is no Brand-Category combination for given data");
        }
        ProductPojo checker = dao.checkBarcode(p.getBarcode());
        if(checker != null && dao.select(id) != checker){
            throw new ApiException("Product Barcode already exists");
        }
        ProductPojo toUpdate = getCheck(id);
        toUpdate.setBarcode(p.getBarcode());
        toUpdate.setBrand_category(p.getBrand_category());
        toUpdate.setName(p.getName());
        toUpdate.setMrp(p.getMrp());
    }

    @Transactional
    public List<ProductPojo> getAll(){return  dao.selectAll();}

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException{
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product Details with given id does not exist id: " + id);
        }
        return p;
    }
    protected static void normalize(ProductPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase ().trim());
        p.setName(p.getName().toLowerCase ().trim());
    }
    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getByBarcode(String barcode) throws ApiException{
        ProductPojo pojo = dao.checkBarcode(barcode);
        if(pojo==null){
            throw new ApiException("Product with given barcode not found");
        }
        return pojo;
    }
    @Transactional
    public List<ProductPojo> getByBrand(int id){return  dao.getByBrand(id);}
}
