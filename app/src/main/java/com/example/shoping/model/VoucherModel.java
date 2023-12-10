package com.example.shoping.model;

public class VoucherModel {
    private String img_sale;
    private String name_sale;
    private  long  value;
    private String expirationDate;
    private boolean isSelected;
    public VoucherModel(String img_sale, String name_sale, long value, String expirationDate) {
        this.img_sale = img_sale;
        this.name_sale = name_sale;
        this.value = value;
        this.expirationDate = expirationDate;
    }

    public String getImg_sale() {
        return img_sale;
    }

    public void setImg_sale(String img_sale) {
        this.img_sale = img_sale;
    }

    public String getName_sale() {
        return name_sale;
    }

    public void setName_sale(String name_sale) {
        this.name_sale = name_sale;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
