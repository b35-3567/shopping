package com.example.shoping.model;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    private String productName;
    private String productSize;
    private int  totalPrice;
    private int quantity;
    private String img_url;

private   Long productPrice;
private String productId;
    public MyCartModel(String productName, String productSize, int totalPrice, int quantity, String img_url) {
        this.productName = productName;
        this.productSize = productSize;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.img_url = img_url;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public MyCartModel(){}
    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
