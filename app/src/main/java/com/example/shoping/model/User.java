package com.example.shoping.model;

public class User {
   private String name;
    private  String email;
    private String phone;
    private String pass;
    private String diachi;
    public  User(){}

    public User(String name, String email, String phone, String pass,String diachi) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.diachi=diachi;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
