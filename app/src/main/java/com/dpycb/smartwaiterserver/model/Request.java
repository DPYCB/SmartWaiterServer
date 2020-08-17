package com.dpycb.smartwaiterserver.model;

import java.util.List;

public class Request {
    private String tableName;
    private String total;
    private String time;
    private String status;
    private String comment;
    private List<Order> foods;

    public Request() {
    }

    public Request(String tableName, String total, String time, String status, String comment, List<Order> foods) {
        this.tableName = tableName;
        this.total = total;
        this.time = time;
        this.status = status;
        this.comment = comment;
        this.foods = foods;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
