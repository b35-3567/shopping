package com.example.shoping.model;

public class ShippingOrderModel {
    private double latitude;
    private double longitude;
    private String orderId;
    private  String OrderModel;

    public ShippingOrderModel(double latitude, double longitude, String orderId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderId = orderId;
    }
    // Thêm constructor không có đối số
    public ShippingOrderModel() {
        // Khởi tạo hoặc thực hiện các công việc khác nếu cần
    }
    public ShippingOrderModel(double latitude, double longitude, String orderId, String orderModel) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderId = orderId;
        OrderModel = orderModel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderModel() {
        return OrderModel;
    }

    public void setOrderModel(String orderModel) {
        OrderModel = orderModel;
    }
}
