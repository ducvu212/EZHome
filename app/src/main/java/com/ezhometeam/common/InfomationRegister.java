package com.ezhometeam.common;

/**
 * Created by n on 30/06/2017.
 */

public class InfomationRegister {
    private String address;
    private String phone;
    private String price;
    private String area;
    private String infomation;

    public String getUser() {
        return user;
    }

    private String user;


    public InfomationRegister() {
    }

    public InfomationRegister(String address, String phone, String price, String area, String infomation, String user) {
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.area = area;
        this.infomation = infomation;
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getArea() {
        return area;
    }

    public String getInfomation() {
        return infomation;
    }
}
