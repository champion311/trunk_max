package com.shosen.max.bean;

public class OrderResponse {
    private String orderNo;
    private String invitorUserPhone;
    private String invitorUserName;
    private String bookUserPhone;
    private String bookUserName;
    private String bookTime;
    private String bookStatus;
    //BookStatus` int(1) DEFAULT '1' COMMENT '1:接收预定2:己缴预定款3:己缴全款4:取消定单5:删除定单',

    private String bookMoney;
    private String province;
    private String city;
    private String bookRemark;
    private String id;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getInvitorUserPhone() {
        return invitorUserPhone;
    }

    public void setInvitorUserPhone(String invitorUserPhone) {
        this.invitorUserPhone = invitorUserPhone;
    }

    public String getInvitorUserName() {
        return invitorUserName;
    }

    public void setInvitorUserName(String invitorUserName) {
        this.invitorUserName = invitorUserName;
    }

    public String getBookUserPhone() {
        return bookUserPhone;
    }

    public void setBookUserPhone(String bookUserPhone) {
        this.bookUserPhone = bookUserPhone;
    }

    public String getBookUserName() {
        return bookUserName;
    }

    public void setBookUserName(String bookUserName) {
        this.bookUserName = bookUserName;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getBookMoney() {
        return bookMoney;
    }

    public void setBookMoney(String bookMoney) {
        this.bookMoney = bookMoney;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBookRemark() {
        return bookRemark;
    }

    public void setBookRemark(String bookRemark) {
        this.bookRemark = bookRemark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
