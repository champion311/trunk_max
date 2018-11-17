package com.shosen.max.bean.eventbusevent;

public class CircleFragmentRefreshEvent {

    private boolean isRefresh;

    public CircleFragmentRefreshEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
