package com.shosen.max.bean;

public class GridBean {

    private String data;

    private boolean isSelectd;

    public GridBean(String data, boolean isSelectd) {
        this.data = data;
        this.isSelectd = isSelectd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSelectd() {
        return isSelectd;
    }

    public void setSelectd(boolean selectd) {
        isSelectd = selectd;
    }
}
