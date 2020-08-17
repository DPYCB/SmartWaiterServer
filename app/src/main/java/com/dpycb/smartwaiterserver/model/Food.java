package com.dpycb.smartwaiterserver.model;

public class Food {
    private String name;
    private String image;
    private String price;
    private String time;
    private String description;
    private String menuId;

    public Food() {
    }

    public Food(String name, String image, String price, String time, String description, String menuId) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.time = time;
        this.description = description;
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
