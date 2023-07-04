package com.increff.pos.controller;
import java.util.List;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
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
public class OrderApiController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @ApiOperation(value = "Adds a order combination to database")
    @RequestMapping(path = "/api/admin/order", method = RequestMethod.POST)
    public int add(@RequestBody List<OrderItemForm> forms) throws ApiException{
      return  orderService.add(forms);

    }

    @ApiOperation(value = "Checks if the entered form is valid for submission")
    @RequestMapping(path = "/api/admin/order/check", method = RequestMethod.POST)
    public void checking(@RequestBody OrderItemForm form) throws ApiException{
        orderService.checkValidity(form);
    }

    @ApiOperation(value = "Gets all orders in a particular orderId")
    @RequestMapping(path = "/api/order/{order_id}", method = RequestMethod.GET)
    public List<OrderItemData> getALlOrder(@PathVariable int order_id) throws ApiException {
        return orderItemService.getAll(order_id);
    }

    @ApiOperation(value = "Gets all orders in OrderPojo")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderPojo> getListOrder() throws ApiException{
        return orderService.getAll();
    }
}
