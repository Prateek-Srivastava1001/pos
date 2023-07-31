package com.increff.pos.scheduler;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SalesScheduler {
    @Autowired
    SchedulerService schedulerService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    // Scheduler code to create Day on Day report
    @Async
    @Scheduled(cron = "59 59 23 * * *")
    public void createReport() throws ApiException, InterruptedException {
        System.out.println("Ran Scheduler...");
        SchedulerPojo pojo = new SchedulerPojo();
        LocalDate date = LocalDate.now();
        Thread.sleep(1100);
        int totalItems = 0;
        double totalRevenue = 0.0;
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate =  LocalDateTime.of(date, LocalTime.MAX);

        List<OrderPojo> orderPojoList = orderService.getByDate(startDate,endDate);
        int totalOrders = orderPojoList.size();

        for (OrderPojo orderPojo : orderPojoList) {
            int id = orderPojo.getId();
            List<OrderItemData> orderItemPojoList = orderItemService.getAll(id);
            for (OrderItemData data: orderItemPojoList) {
                totalItems += data.getQuantity();
                totalRevenue += (data.getQuantity() * data.getSelling_price());
            }
        }
        BigDecimal roundedValue = BigDecimal.valueOf(totalRevenue).setScale(2, RoundingMode.HALF_UP);
        totalRevenue = roundedValue.doubleValue();
        pojo.setDate(date);
        pojo.setInvoiced_orders_count(totalOrders);
        pojo.setInvoiced_items_count(totalItems);
        pojo.setTotal_revenue(totalRevenue);

        schedulerService.add(pojo);

    }
}
