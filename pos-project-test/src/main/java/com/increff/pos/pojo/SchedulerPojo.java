package com.increff.pos.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="DailyReport")
public class SchedulerPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private int invoiced_orders_count;
    private int invoiced_items_count;
    private double total_revenue;
}
