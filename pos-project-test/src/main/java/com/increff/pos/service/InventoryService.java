package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.InventoryData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class InventoryService {
    @Autowired
    InventoryDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo pojo) throws ApiException{
        if(pojo.getQuantity()<0){
            throw new ApiException("Quantity cannot be negative");
        }
        dao.insert(pojo);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, InventoryPojo pojo) throws ApiException{
        InventoryPojo toUpdate = getCheck(id);
        toUpdate.setQuantity(pojo.getQuantity());
    }

    @Transactional
    public List<InventoryPojo> getAll() throws ApiException {
        List<InventoryPojo> pojoList =  dao.selectAll();
        return pojoList;
    }

    @Transactional
    public InventoryPojo getCheck(int id) throws ApiException{
        InventoryPojo pojo = dao.select(id);
        if (pojo == null) {
            throw new ApiException("Product Details with given id does not exist id: " + id);
        }
        return pojo;
    }
}
