package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.InventoryService;
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
public class InventoryApiController {
    @Autowired
    InventoryDto dto;
    @ApiOperation(value = "Gets by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException{
        return dto.get(id);
    }
    @ApiOperation(value = "Gets list of all inventory data")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return dto.getAll();
    }
    @ApiOperation(value = "Updates a record (Edit)")
    @RequestMapping(path = "/api/admin/inventory/{id}", method = RequestMethod.PUT)
    public void edit(@PathVariable int id, @RequestBody InventoryForm form) throws ApiException{
        dto.edit(id, form);
    }

    @ApiOperation(value = "Updates a record (Upload)")
    @RequestMapping(path = "/api/admin/inventory", method = RequestMethod.PUT)
    public void editByUpload(@RequestBody InventoryForm form) throws ApiException{
        dto.editByUpload(form);
    }
}
