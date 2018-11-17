package com.shosen.max.bean;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private T data;

    @SerializedName("open")
    private String open;

    @SerializedName("reSign")
    private String reSign;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getReSign() {
        return reSign;
    }

    public void setReSign(String reSign) {
        this.reSign = reSign;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", open='" + open + '\'' +
                ", reSign='" + reSign + '\'' +
                '}';
    }
}
