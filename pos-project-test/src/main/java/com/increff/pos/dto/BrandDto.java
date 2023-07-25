package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConverterUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {
    @Autowired
    private BrandService service;
    private ConverterUtil converterUtil;
    private NormalizeUtil normalizeUtil;
    private ValidateUtil validateUtil;

    // Adding BrandForm to the DataBase
    public void add(BrandForm form) throws ApiException {
        BrandPojo pojo = converterUtil.convert(form);
        normalizeUtil.normalize(pojo);
        validateUtil.checkValid(pojo);
        service.add(pojo);
    }
    // Getting Brand data by ID
    public BrandData get(int id) throws ApiException{
        BrandPojo pojo = service.getCheck(id);
        return converterUtil.convert(pojo);
    }

    //Getting all data from DB
    public List<BrandData> getAll(){
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for(BrandPojo p: list){
            list2.add(converterUtil.convert(p));
        }
        return list2;
    }

    //Updating
    public void update(int id, BrandForm form) throws ApiException{
        BrandPojo pojo = converterUtil.convert(form);
        normalizeUtil.normalize(pojo);
        validateUtil.checkValid(pojo);
        service.update(id, pojo);
    }
}
