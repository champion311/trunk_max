package com.shosen.max.bean.eventbusevent;

public class WeChatPayEvent {

    private int payResStatus;

    public WeChatPayEvent(int payResStatus) {
        this.payResStatus = payResStatus;
    }

    public int getPayResStatus() {
        return payResStatus;
    }

    public void setPayResStatus(int payResStatus) {
        this.payResStatus = payResStatus;
    }
}
