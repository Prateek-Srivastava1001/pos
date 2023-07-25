package com.increff.pos.service;

import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;

import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.model.ReportsForm;
import com.increff.pos.pojo.SchedulerPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.increff.pos.util.StringUtil;

@Service
public class SchedulerService {
    @Autowired
    SchedulerDao schedulerDao;
    //CREATE
    @Transactional(rollbackOn = ApiException.class)
    public void add(SchedulerPojo pojo) throws ApiException{
        List<SchedulerPojo> checkerList = schedulerDao.selectByDate(pojo.getDate(), pojo.getDate());
        if(checkerList.size() == 0) {
            schedulerDao.insert(pojo);
        }
        else{
            SchedulerPojo checker = checkerList.get(0);
            checker.setTotal_revenue(pojo.getTotal_revenue());
            checker.setInvoiced_orders_count(pojo.getInvoiced_orders_count());
            checker.setInvoiced_items_count(pojo.getInvoiced_items_count());
        }
    }
    //READ
    public List<SchedulerPojo> getAll(){
        return schedulerDao.selectAll();
    }
    public List<SchedulerPojo> getByDate(ReportsForm form) throws ApiException {
        LocalDate startDate = LocalDate.parse(form.getStartDate());
        LocalDate endDate = LocalDate.parse(form.getEndDate());
        isValidDateRange(startDate,endDate);
        return schedulerDao.selectByDate(startDate,endDate);
    }
    //helper
    public static void isValidDateRange(LocalDate start, LocalDate end) throws ApiException {
        if (start.isAfter(end)) {
            throw new ApiException("Start date cannot be after end date");
        }
    }
}
