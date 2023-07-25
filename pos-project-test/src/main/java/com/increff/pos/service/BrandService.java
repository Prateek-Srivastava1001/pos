package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.NormalizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.util.StringUtil;

@Service
public class BrandService {
    @Autowired
    private BrandDao dao;
    @Autowired
    private NormalizeUtil normalizeUtil;

    //CREATE
    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo p) throws ApiException{
        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("Brand cannot be empty");
        }
        if(StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("Category cannot be empty");
        }
        //Brand - Category combination should be unique
        if(dao.checkForCombination(p.getBrand(),p.getCategory()) != null){
            throw new ApiException("Brand - Category combination already exists");
        }
        dao.insert(p);
    }

    //UPDATE
    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, BrandPojo pojo) throws ApiException{
        if(StringUtil.isEmpty(pojo.getBrand())) {
            throw new ApiException("Brand cannot be empty");
        }
        if(StringUtil.isEmpty(pojo.getCategory())) {
            throw new ApiException("Category cannot be empty");
        }
        BrandPojo toUpdate = getCheck(id);
        BrandPojo checker = dao.checkForCombination(pojo.getBrand(),pojo.getCategory());
        if(checker != null && dao.select(id) != checker){
            throw new ApiException("Brand - Category combination already exists");
        }
        toUpdate.setCategory(pojo.getCategory());
        toUpdate.setBrand(pojo.getBrand());
    }

    //Gets all brand data from BrandPojo
    public List<BrandPojo> getAll(){return dao.selectAll();}

    //Check if given id exists in Database
    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Brand-Category with given id does not exist id: " + id);
        }
        return p;
    }
    // Gets pojo based on brand-category combination, throws exception if it is not present
    public BrandPojo combinationChecker(String brand, String category) throws ApiException{
        BrandPojo pojo = dao.checkForCombination(brand, category);
        if(pojo == null){
            throw new ApiException("Brand-Category combination not found");
        }
        return pojo;
    }

}
