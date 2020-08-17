package com.dpycb.smartwaiterserver.model;

public class Order {
    private String FoodID;
    private String FoodName;
    private String Price;
    private String Quantity;
    private String Time;

    public Order() {
    }

    public Order(String foodID, String foodName, String price, String quantity, String time) {
        this.FoodID = foodID;
        this.FoodName = foodName;
        this.Price = price;
        this.Quantity = quantity;
        this.Time = time;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        this.FoodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        this.FoodName = foodName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        this.Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }
}
