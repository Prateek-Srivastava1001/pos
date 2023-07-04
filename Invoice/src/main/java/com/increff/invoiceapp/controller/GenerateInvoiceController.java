package com.increff.invoiceapp.controller;

import com.increff.invoiceapp.dto.InvoiceDto;
import com.increff.invoiceapp.model.InvoiceForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api
@RestController
public class GenerateInvoiceController {

    @Autowired
    private InvoiceDto invoiceDto;

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path = "/api/invoice", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getInvoicePDF(@RequestBody InvoiceForm form) throws IOException {
        return invoiceDto.getInvoicePDF(form);
    }

}