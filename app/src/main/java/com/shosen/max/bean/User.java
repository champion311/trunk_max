package com.shosen.max.bean;

public class User {

//    {
//        "uid":2, "phone":"13366100567", "status":1, "sign":"", "headimg":
//        "http://api.shosen.cn:8080/file/download/fc8b4b4119244bf5b33350bfb5ec4be7.jpg", "name":
//        "将计就计n", "tabs":"1,2,3", "title":"", "province":"", "city":"", "sex":"0", "remainNum":
//        "9998", "orderNum":"1", "rewardMoney":"4000.0"
//    }

    private String uid;

    private String securityToken;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }


    private String headimg; //0,
    private String name; //0,
    private String phone; //string,
    private String sign; //0,
    private String status; //0,
    private String token; //string,
    private String remainNum;
    private String orderNum;
    private String tabs;
    private String title;
    private String province;
    private String city;
    private String sex;
    private String rewardMoney;
    private String openId;


//    "remainNum":"9994",
//            "orderNum":"5"


    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(String remainNum) {
        this.remainNum = remainNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setUserData(LoginResponse response) {
        this.uid = response.getUid();
        this.headimg = response.getHeadimg();
        this.name = response.getName();
        this.phone = response.getPhone();
        this.securityToken = response.getToken();
        this.sign = response.getSign();
        this.status = response.getStatus();
        this.remainNum = response.getRemainNum();
        this.orderNum = response.getOrderNum();
        this.tabs = response.getTabs();
        this.title = response.getTitle();
        this.province = response.getProvince();
        this.city = response.getCity();
        this.sex = response.getSex();
        this.rewardMoney = response.getRewardMoney();
        this.openId = response.getOpenId();
    }

    public void updateUserData(User response) {
        this.headimg = response.getHeadimg();
        this.name = response.getName();
        this.phone = response.getPhone();
        this.sign = response.getSign();
        this.status = response.getStatus();
        this.remainNum = response.getRemainNum();
        this.orderNum = response.getOrderNum();
        this.tabs = response.getTabs();
        this.title = response.getTitle();
        this.province = response.getProvince();
        this.city = response.getCity();
        this.sex = response.getSex();
        this.rewardMoney = response.getRewardMoney();
        this.openId = response.getOpenId();
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(String rewardMoney) {
        this.rewardMoney = rewardMoney;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
