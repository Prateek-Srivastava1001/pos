package com.increff.pos.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class SchedulerPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private int invoiced_orders_count;
    private int invoiced_items_count;
    private double total_revenue;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getInvoiced_orders_count() {
        return invoiced_orders_count;
    }

    public void setInvoiced_orders_count(int invoiced_orders_count) {
        this.invoiced_orders_count = invoiced_orders_count;
    }

    public int getInvoiced_items_count() {
        return invoiced_items_count;
    }

    public void setInvoiced_items_count(int invoiced_items_count) {
        this.invoiced_items_count = invoiced_items_count;
    }

    public double getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(double total_revenue) {
        this.total_revenue = total_revenue;
    }
}
