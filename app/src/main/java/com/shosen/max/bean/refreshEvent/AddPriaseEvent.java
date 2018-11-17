package com.shosen.max.bean.refreshEvent;

public class AddPriaseEvent {

    private String markStatus;

    private String markCount;


    public AddPriaseEvent(String markStatus, String markCount) {
        this.markStatus = markStatus;
        this.markCount = markCount;
    }

    public String getMarkStatus() {
        return markStatus;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }

    public String getMarkCount() {
        return markCount;
    }

    public void setMarkCount(String markCount) {
        this.markCount = markCount;
    }
}
