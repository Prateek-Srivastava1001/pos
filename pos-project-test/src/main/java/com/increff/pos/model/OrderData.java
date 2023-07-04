package com.increff.pos.model;

import java.time.LocalDateTime;

public class OrderData {
    private int id;
    private LocalDateTime date_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate_time() {
        return date_time;
    }

    public void setDate_time(LocalDateTime date_time) {
        this.date_time = date_time;
    }
}
