package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.dto.ReportsDto;
import com.increff.pos.model.ReportsData;
import com.increff.pos.model.ReportsForm;
import com.increff.pos.pojo.SchedulerPojo;
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
public class ReportsApiController {
    @Autowired
    ReportsDto dto;

    @ApiOperation(value = "Gets list of scheduler report")
    @RequestMapping(path = "/api/reports/scheduler", method = RequestMethod.GET)
    public List<SchedulerPojo> getScheduledData(){
        return dto.getScheduledData();
    }
    @ApiOperation(value = "Generates scheduler report")
    @RequestMapping(path = "/api/reports/scheduler/generate", method = RequestMethod.GET)
    public void generateDailyReport() throws ApiException, InterruptedException {
        dto.generateDailyReport();
    }

    @ApiOperation(value = "Gets list of scheduler report")
    @RequestMapping(path = "/api/reports/scheduler", method = RequestMethod.POST)
    public List<SchedulerPojo> getByDate(@RequestBody ReportsForm form) throws ApiException{
        return dto.getByDate(form);
    }

    @ApiOperation(value = "Gets list of inventory report")
    @RequestMapping(path = "/api/reports/inventory", method = RequestMethod.GET)
    public List<ReportsData> inventoryReport() throws ApiException {
        return dto.getInventoryReport();
    }

    @ApiOperation(value = "gets sales report for particular timeline")
    @RequestMapping(path = "/api/reports/sales", method = RequestMethod.POST)
    public List<ReportsData> salesReport(@RequestBody ReportsForm form) throws ApiException {
        return dto.getSalesReport(form);
    }
}
