package com.increff.invoiceapp.service;

import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GeneratePDF {
    private String xmlFilePath="./src/main/resources/xml/Invoice.xml" ;
    private String xslFilePath="./src/main/resources/xsl/Invoice.xsl" ;
    private String generatedDirPath="./generated" ;
    public void createPDF() {
        System.out.println("Create PDF");
        try {
            File xmlfile = new File(xmlFilePath);
            File xsltfile = new File(xslFilePath);
            File pdfDir = new File(generatedDirPath);
            pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, "invoice.pdf");
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
            // Setup output
            OutputStream out = new FileOutputStream(pdfFile);
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

}