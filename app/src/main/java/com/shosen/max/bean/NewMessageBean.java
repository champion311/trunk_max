package com.shosen.max.bean;

public class NewMessageBean {

    private String noReadNum;

    private String type;//   1 未读消息   2已读消息

    public String getNoReadNum() {
        return noReadNum;
    }

    public void setNoReadNum(String noReadNum) {
        this.noReadNum = noReadNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NewMessageBean(String noReadNum, String type) {
        this.noReadNum = noReadNum;
        this.type = type;
    }
}
