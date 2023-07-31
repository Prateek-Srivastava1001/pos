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
    private double maxMrp = 1000000000;
    //CREATE
    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo pojo) throws ApiException{
        String barcode = pojo.getBarcode();
        if(StringUtil.isEmpty(barcode)){
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(pojo.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(pojo.getMrp()<0){
            throw new ApiException("MRP cannot be negative. This is not how math works...");
        }
        if(pojo.getMrp()>maxMrp){
            throw new ApiException("MRP cannot be more than 1000000000");
        }
        if(dao.checkBarcode(pojo.getBarcode()) != null){
            throw new ApiException("Product Barcode already exists");
        }
        dao.insert(pojo);
    }
    //UPDATE
    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo pojo) throws ApiException{
        //Same checks as that in add
        if(StringUtil.isEmpty(pojo.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(pojo.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(pojo.getMrp()<0){
            throw new ApiException("MRP cannot be negative. This is not how math works...");
        }
        if(pojo.getMrp()>maxMrp){
            throw new ApiException("MRP cannot be more than 1000000000");
        }
        ProductPojo checker = dao.checkBarcode(pojo.getBarcode());
        if(checker != null && dao.select(id) != checker){
            throw new ApiException("Product Barcode already exists");
        }
        ProductPojo toUpdate = getCheck(id);
        toUpdate.setBarcode(pojo.getBarcode());
        toUpdate.setBrand_category(pojo.getBrand_category());
        toUpdate.setName(pojo.getName());
        toUpdate.setMrp(pojo.getMrp());
    }
    // Gets all products from database
    public List<ProductPojo> getAll(){return  dao.selectAll();}

    //READ
    public ProductPojo getCheck(int id) throws ApiException{
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product Details with given id does not exist id: " + id);
        }
        return p;
    }
    // Gets product corresponding to given barcode
    public ProductPojo getByBarcode(String barcode) throws ApiException{
        ProductPojo pojo = dao.checkBarcode(barcode);
        if(pojo==null){
            throw new ApiException("Product with given barcode not found");
        }
        return pojo;
    }
    // Gets product with given brand-category id (Used in inventory report)
    public List<ProductPojo> getByBrand(int id){return  dao.getByBrand(id);}
}
