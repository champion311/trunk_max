package com.shosen.max.bean;

public class VerifyCodeResponse {

    //{"respTime":1539055486087,"respStatus":1,"message":"验证码发送成功"}

    private String respTime;

    private String respStatus;

    private String message;

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public String getRespStatus() {
        return respStatus;
    }

    public void setRespStatus(String respStatus) {
        this.respStatus = respStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
