package com.shosen.max.bean;

import com.google.gson.annotations.SerializedName;

public class WxPayBean {

//"package":"Sign=WXPay","appid":"wx482945bce5f90e1b",
//        "sign":"373C47277F22C4F147B959322927364B","partnerid":"1516012071",
//        "prepayid":"wx0914122323607691868659ec0683113200",
//        "noncestr":"1539065543215","timestamp":"1539065543"


    @SerializedName("appid")
    private String appId;

    @SerializedName("partnerid")
    private String partnerId;

    @SerializedName("package")
    private String packAge;

    @SerializedName("noncestr")
    private String nonceStr;

    @SerializedName("timestamp")
    private String timeStamp;

    @SerializedName("prepayid")
    private String prepayId;

    @SerializedName("sign")
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPackAge() {
        return packAge;
    }

    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
