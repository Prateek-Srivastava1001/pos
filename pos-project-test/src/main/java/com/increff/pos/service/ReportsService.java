package com.increff.pos.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.ReportsData;
import com.increff.pos.model.ReportsForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportsService {
    @Autowired
    BrandService brandService;
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @Transactional(rollbackOn = ApiException.class)
    public List<ReportsData> getSalesReport(ReportsForm dates) throws ApiException{
        String startDate = dates.getStartDate() + " 00:00:00";
        String endDate = dates.getEndDate() + " 23:59:59";
        List<ReportsData> salesReport= new ArrayList<>();
        HashMap<Integer,ReportsData> map = new HashMap<Integer, ReportsData>();//HashMap<Brand_id, ReportsData>
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime sDate = LocalDateTime.parse(startDate,formatter);
        LocalDateTime eDate = LocalDateTime.parse(endDate, formatter);
        isValidDateTimeRange(sDate, eDate);
        List<OrderPojo> orderList = orderService.getByDate(sDate, eDate);

        //Iterating through each order
        for(OrderPojo order : orderList){
            List<OrderItemData> orderItemList = orderItemService.getAll(order.getId());
            //Iterating through each orderItem
            for(OrderItemData orderItem : orderItemList){
                //we will bet brandCategory id from product pojo
                int brandId = productService.get(orderItem.getProduct_id()).getBrand_category();
                //check in map, if not present, create
                DecimalFormat df = new DecimalFormat("#.##");
                if (!map.containsKey(brandId)) {
                    BrandPojo brandPojo = brandService.get(brandId);
                    ReportsData reportsData = new ReportsData();
                    reportsData.setBrand(brandPojo.getBrand());
                    reportsData.setCategory(brandPojo.getCategory());
                    map.put(brandId, reportsData);
                }
                // Update the values in reportsData for particular brandId
                ReportsData reportsData = map.get(brandId);
                //changing quantity
                int quantity = reportsData.getQuantity() + orderItem.getQuantity();
                reportsData.setQuantity(quantity);
                //changing revenue
                double revenue = reportsData.getRevenue() + (orderItem.getQuantity()*orderItem.getSelling_price());
                reportsData.setRevenue(Double.parseDouble(df.format(revenue)));
                //putting updated values in map
                map.put(brandId, reportsData);
            }
        }
        // converting data in map to List<ReportsData>
        for(Map.Entry<Integer, ReportsData> entry : map.entrySet()){
            ReportsData finalReport = entry.getValue();
            salesReport.add(finalReport);
        }
        return salesReport;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ReportsData> getInventoryReport() throws ApiException{
        List<ReportsData> inventoryReport = new ArrayList<>();
        List<BrandPojo> brandList = brandService.getAll();
        for(BrandPojo brand: brandList){
            int quantity = 0;
            List<ProductPojo> products = productService.getByBrand(brand.getId());
            for(ProductPojo product: products){
                quantity += inventoryService.get(product.getId()).getQuantity();
            }
            ReportsData data = new ReportsData();
            data.setQuantity(quantity);
            data.setBrand(brand.getBrand());
            data.setCategory(brand.getCategory());
            inventoryReport.add(data);
        }
        return inventoryReport;
    }

    private void isValidDateTimeRange(LocalDateTime start, LocalDateTime end) throws ApiException{
        if (start.isAfter(end)) {
            throw new ApiException("Start date cannot be after end date");
        }
    }

}
