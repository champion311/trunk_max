package com.shosen.max.bean;

public class OrderCarResponse {

//    {
//        code = 100, msg = '预定成功', data = {orderNo = 201810091539056430278171, invitorUserID =, invitorUserPhone =, invitorUserName =, bookUserID =, bookUserPhone = 18811447873, bookUserName =, contactName =, contactPhone =, bookTime = 1539056430278, bookStatus = 1.0, bookMoney = 25000.0, province = 浙江省, city = 杭州市, bookRemark =, id = 12.0}, open = '0', reSign = '0'
//    }
//
//    orderNo =201810091539056430278171,invitorUserID =,invitorUserPhone =,invitorUserName =,bookUserID =,bookUserPhone =18811447873,bookUserName =,contactName =,contactPhone =,bookTime =1539056430278,bookStatus =1.0,bookMoney =25000.0,province =浙江省,city =杭州市,bookRemark =,id =12.0

    private String orderNo;

    private String invitorUserID;

    private String invitorUserPhone;

    private String invitorUserName;

    private String bookUserID;

    private String bookUserPhone;

    private String bookUserName;

    private String contactName;

    private String contactPhone;

    private String bookTime;

    private String bookStatus;

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

    public String getInvitorUserID() {
        return invitorUserID;
    }

    public void setInvitorUserID(String invitorUserID) {
        this.invitorUserID = invitorUserID;
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

    public String getBookUserID() {
        return bookUserID;
    }

    public void setBookUserID(String bookUserID) {
        this.bookUserID = bookUserID;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
