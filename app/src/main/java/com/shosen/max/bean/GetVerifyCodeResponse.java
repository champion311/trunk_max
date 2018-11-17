package com.shosen.max.bean;

public class GetVerifyCodeResponse {
//
//    2 = {LinkedTreeMap$Node@6355} "message" -> "验证码发送成功"
//            0 = {LinkedTreeMap$Node@6353} "respTime" -> "1.537844628074E12"
//            1 = {LinkedTreeMap$Node@6354} "respStatus" -> "1.0"

    private String message;

    private String respTime;

    private String respStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
}
