package com.increff.invoiceapp.service;


import com.increff.invoiceapp.model.InvoiceForm;
import com.increff.invoiceapp.model.OrderItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.DecimalFormat;

public class CreateXMLFile {
    private String xmlFilePath="./src/main/resources/xml/Invoice.xml";
    public void createXML(InvoiceForm invoiceForm) {
        System.out.println("CreateXML");
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("invoice");
            document.appendChild(root);


            Element order_id = document.createElement("order_id");
            order_id.appendChild(document.createTextNode(invoiceForm.getOrderId().toString()));
            root.appendChild(order_id);

            Element order_date = document.createElement("order_date");
            String orderDate=getOrderDate(invoiceForm.getPlacedDate());
            order_date.appendChild(document.createTextNode(orderDate));
            root.appendChild(order_date);

            // order item element
            for (OrderItem o : invoiceForm.getOrderItemList()){
                DecimalFormat df = new DecimalFormat("#.##");
                Element order_item = document.createElement("order_item");

                root.appendChild(order_item);

                // set an attribute to staff element
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(o.getOrderItemId().toString()));
                order_item.appendChild(id);

                // firstname element
                Element ProductId = document.createElement("product_name");
                ProductId.appendChild(document.createTextNode(o.getProductName()));
                order_item.appendChild(ProductId);

                // lastname element
                Element quantity = document.createElement("quantity");
                quantity.appendChild(document.createTextNode(o.getQuantity().toString()));
                order_item.appendChild(quantity);

                Element sellingPrice = document.createElement("selling_price");
                sellingPrice.appendChild(document.createTextNode(formatDouble(o.getSellingPrice())));
                order_item.appendChild(sellingPrice);

                Element amt = document.createElement("amt");
                amt.appendChild(document.createTextNode(formatDouble(o.getTotalAmount())));
                order_item.appendChild(amt);

            }

            Element amount = document.createElement("amount");
            amount.appendChild(document.createTextNode(formatDouble(invoiceForm.getAmount())));
            root.appendChild(amount);
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    private String getOrderDate(String placedDate){
        for(int i=0;i<placedDate.length();i++){
            if('T' == placedDate.charAt(i)){
                StringBuffer string = new StringBuffer(placedDate);
                string.setCharAt(i,' ');
                placedDate= String.valueOf(string);
            }
        }
        return placedDate;
    }

    private String formatDouble(double value) {
        return String.format("%.2f", value);
    }
}