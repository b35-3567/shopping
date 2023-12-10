package com.example.shoping.model;

public class SizeModel {
     private    String   product_id;
     private    String   size_name;
    private   Long      size_price;
    public SizeModel() {
    }

    public SizeModel(String product_id, String size_name, Long size_price) {
        this.product_id = product_id;
        this.size_name = size_name;
        this.size_price = size_price;

    }

    public String getProduct_id() {
        return product_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public Long getSize_price() {
        return size_price;
    }

    public void setSize_price(Long size_price) {
        this.size_price = size_price;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }


}
