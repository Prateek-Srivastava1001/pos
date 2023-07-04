package com.increff.invoiceapp.service;


import com.increff.invoiceapp.model.InvoiceForm;
import com.increff.invoiceapp.model.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    public void generateInvoice(InvoiceForm form)
    {
        List<OrderItem> items = form.getOrderItemList();
        Double amount = 0.0;
        for(OrderItem item : items) {
            Double cur = 0.0;
            cur = item.getSellingPrice() * item.getQuantity();
            amount+=cur;
            item.setTotalAmount(cur);
        }
        form.setAmount(amount);
        CreateXMLFile createXMLFile = new CreateXMLFile();

        createXMLFile.createXML(form);

        GeneratePDF generatePDF = new GeneratePDF();

        generatePDF.createPDF();
    }


}