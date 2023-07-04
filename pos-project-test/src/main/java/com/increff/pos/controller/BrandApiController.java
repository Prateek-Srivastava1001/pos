package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
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
public class BrandApiController {
    @Autowired
    private BrandService service;

    @ApiOperation(value = "Adds a brand-category combination to database")
    @RequestMapping(path = "/api/admin/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException{
        BrandPojo p = convert(form);
        service.add(p);
    }

    @ApiOperation(value = "Gets by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException{
        BrandPojo pojo = service.get(id);
        return convert(pojo);
    }

    @ApiOperation(value = "Gets list of all brand-category combination (Also used for brand report)")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll(){
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for(BrandPojo p: list){
            list2.add(convert(p));
        }
        return list2;
    }
    @ApiOperation(value = "Updates a record")
    @RequestMapping(path = "/api/admin/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException{
        BrandPojo pojo = convert(form);
        service.update(id, pojo);
    }

    private static BrandData convert(BrandPojo pojo){
        BrandData data = new BrandData();
        data.setCategory(pojo.getCategory());
        data.setBrand(pojo.getBrand());
        data.setId(pojo.getId());
        return data;
    }
    private static BrandPojo convert(BrandForm form){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand(form.getBrand());
        pojo.setCategory(form.getCategory());
        return pojo;
    }
}
