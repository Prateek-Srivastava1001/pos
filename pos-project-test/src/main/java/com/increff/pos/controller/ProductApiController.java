package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.increff.pos.service.ApiException;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {
    @Autowired
    private ProductService service;
    @ApiOperation(value = "Adds a brand-category combination to database")
    @RequestMapping(path = "/api/admin/product", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException{
        ProductPojo pojo = convert(form);
        service.add(pojo);
    }

    @ApiOperation(value = "Gets by ID")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException{
        ProductPojo pojo = service.get(id);
        return convert(pojo);
    }

    @ApiOperation(value = "Gets list of all product data")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll(){
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo pojo : list)
            list2.add(convert(pojo));
        return list2;
    }

    @ApiOperation(value = "Updates a record")
    @RequestMapping(path = "/api/admin/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException{
        ProductPojo pojo = convert(form);
        service.update(id, pojo);
    }


    private static ProductData convert(ProductPojo pojo){
        ProductData data = new ProductData();
        data.setId(pojo.getId());
        data.setBarcode(pojo.getBarcode());
        data.setBrand_category(pojo.getBrand_category());
        data.setName(pojo.getName());
        data.setMrp(pojo.getMrp());
        return  data;
    }
    private static ProductPojo convert(ProductForm form){
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setBrand_category(form.getBrand_category());
        pojo.setName(form.getName());
        pojo.setMrp(form.getMrp());
        return pojo;
    }
}
