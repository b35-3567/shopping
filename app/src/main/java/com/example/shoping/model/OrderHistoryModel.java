package com.example.shoping.model;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryModel implements Serializable {
    private String id;
    private String address;

    private String name;
    private String orderDate;
    private String orderTime;
    private String phone;
    private List<ProductItem> products; // Thêm danh sách sản phẩm
    private String totalAmount;
    private String voucherName;

    // Constructors, getters, and setters

    public static class ProductItem {
        private String imgUrl;
        private String productName;
        private String productSize;
        private int quantity;
        private int totalPrice;

        public ProductItem(String imgUrl, String productName, String productSize, int quantity, int totalPrice) {
            this.imgUrl = imgUrl;
            this.productName = productName;
            this.productSize = productSize;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
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

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }
    }

    public OrderHistoryModel(String id,String address, String name, String orderDate, String orderTime, String phone, List<ProductItem> products, String totalAmount, String voucherName) {
        this.id=id;
        this.address = address;
        this.name = name;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.phone = phone;
        this.products = products;
        this.totalAmount = totalAmount;
        this.voucherName = voucherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
}


