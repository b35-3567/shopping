package com.example.shoping.model;

import java.io.Serializable;
public class ProductModel implements Serializable {
    private String name;
    private String description;
    private int price;
    private String type;
    private String img_url;
    private String size;
    private int quantity;
    // Trường tạm thời để lưu trữ product_id

    private String documentId;
    public ProductModel(String name, String description, int price, String type, String img_url, String documentId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.img_url = img_url;
        this.documentId = documentId;
    }

    public ProductModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    // New getter and setter for document ID
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductModel(String name, String description, int price, String type, String img_url, String size, int quantity, String documentId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.img_url = img_url;
        this.size = size;
        this.quantity = quantity;
        this.documentId = documentId;
    }
}
