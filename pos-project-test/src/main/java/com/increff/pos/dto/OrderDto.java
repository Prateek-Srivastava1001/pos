package com.increff.pos.dto;

import com.increff.pos.model.InvoiceForm;
import com.increff.pos.model.OrderItem;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.increff.pos.util.NormalizeUtil.normalize;

@Component
public class OrderDto {
    @Value("${invoice.url}")
    private String url;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;
    private int orderId=0;
    public int add(List<OrderItemForm> forms) throws ApiException{
        if(forms.size()<1){
            throw new ApiException("Empty Order List Not Supported");
        }
        OrderPojo pojo = new OrderPojo();
        LocalDateTime dateTime = LocalDateTime.now();
        pojo.setDate_time(dateTime);
        orderId = orderService.add(pojo);
        addItems(forms);
        return orderId;
    }
    public void addItems(List<OrderItemForm> formList) throws ApiException{
        for(OrderItemForm form:formList){
            normalize(form);
            OrderItemPojo pojo = convert(form);
            orderItemService.add(pojo);
            //Updating in Inventory
            InventoryPojo inventoryPojo = inventoryService.getCheck(pojo.getProduct_id());
            int quantity = inventoryPojo.getQuantity() - pojo.getQuantity();
            inventoryPojo.setQuantity(quantity);
            inventoryService.update(pojo.getProduct_id(),inventoryPojo);
        }
    }
    public void checker(OrderItemForm form) throws ApiException{
        normalize(form);
        OrderItemPojo pojo = convert(form);
        orderItemService.checks(pojo);
    }
    public List<OrderItemData> getOrderItemsByOrderId(int order_id) throws ApiException{
        return orderItemService.getAll(order_id);
    }
    public List<OrderPojo> getListOrder() throws ApiException{
        return orderService.getAll();
    }
    public ResponseEntity<byte[]> getInvoicePDF(int id) throws Exception {
        InvoiceForm invoiceForm = generateInvoiceForOrder(id);
        RestTemplate restTemplate = new RestTemplate();
        byte[] contents = Base64.getDecoder().decode(restTemplate.postForEntity(url, invoiceForm, byte[].class).getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "invoice.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }
    public InvoiceForm generateInvoiceForOrder(int orderId) throws ApiException{
        InvoiceForm invoiceForm = new InvoiceForm();
        OrderPojo orderPojo = orderService.getCheck(orderId);
        invoiceForm.setOrderId(orderId);
        invoiceForm.setAmount(0.0);
        invoiceForm.setPlacedDate(orderPojo.getDate_time().toString());
        List<OrderItem> dataList = new ArrayList<>();
        List<OrderItemData> transferList = orderItemService.getAll(orderId);
        Integer i =0;
        for(OrderItemData element: transferList){
            OrderItem orderItem = new OrderItem();
            i++;
            orderItem.setOrderItemId(i);
            orderItem.setProductName(element.getName());
            orderItem.setQuantity(element.getQuantity());
            orderItem.setSellingPrice(element.getSelling_price());
            orderItem.setTotalAmount(10.0);
            dataList.add(orderItem);
        }
        invoiceForm.setOrderItemList(dataList);
        return invoiceForm;
    }
    private OrderItemPojo convert(OrderItemForm form) throws ApiException {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrder_id(orderId);
        pojo.setProduct_id(productService.getByBarcode(form.getBarcode()).getId());
        pojo.setQuantity(form.getQuantity());
        BigDecimal roundedValue = BigDecimal.valueOf(form.getSelling_price()).setScale(2, RoundingMode.HALF_UP);
        double sellingPrice = roundedValue.doubleValue();
        pojo.setSelling_price(sellingPrice);
        return pojo;
    }

}
