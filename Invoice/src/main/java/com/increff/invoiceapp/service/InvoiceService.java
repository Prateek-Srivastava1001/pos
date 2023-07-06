package com.increff.invoiceapp.service;


import com.increff.invoiceapp.model.InvoiceForm;
import com.increff.invoiceapp.model.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvoiceService {
    public void generateInvoice(InvoiceForm form)
    {
        System.out.println("Invoice Service");
        System.out.println(form.getOrderId());
        System.out.println(form.getPlacedDate());
        System.out.println(form.getAmount());
        List<OrderItem> items = form.getOrderItemList();
        Double amount = 0.0;
        for(OrderItem item : items) {
            System.out.println(item.getProductName());
            System.out.println(item.getOrderItemId());
            System.out.println(item.getQuantity());
            System.out.println(item.getSellingPrice());
            System.out.println(item.getTotalAmount());
            System.out.println(" break");
            Double cur = 0.0;
            cur = item.getSellingPrice() * item.getQuantity();
            amount+=cur;
            BigDecimal roundedValue = BigDecimal.valueOf(cur).setScale(2, RoundingMode.HALF_UP);
            cur = roundedValue.doubleValue();
            item.setTotalAmount(cur);
        }
        BigDecimal roundedValue = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
        amount = roundedValue.doubleValue();
        form.setAmount(amount);
        CreateXMLFile createXMLFile = new CreateXMLFile();

        createXMLFile.createXML(form);

        GeneratePDF generatePDF = new GeneratePDF();

        generatePDF.createPDF();
    }


}