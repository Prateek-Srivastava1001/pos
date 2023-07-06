package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConverterUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.StringUtil;
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

    public void add(BrandForm form) throws ApiException {
        BrandPojo p = converterUtil.convert(form);
        normalizeUtil.normalize(p);
        service.add(p);
    }
    public BrandData get(int id) throws ApiException{
        BrandPojo pojo = service.getCheck(id);
        return converterUtil.convert(pojo);
    }

    public List<BrandData> getAll(){
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for(BrandPojo p: list){
            list2.add(converterUtil.convert(p));
        }
        return list2;
    }

    public void update(int id, BrandForm form) throws ApiException{
        BrandPojo pojo = converterUtil.convert(form);
        normalizeUtil.normalize(pojo);
        service.update(id, pojo);
    }
}
