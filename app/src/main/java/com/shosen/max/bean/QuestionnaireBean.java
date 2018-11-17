package com.shosen.max.bean;

import java.util.List;

public class QuestionnaireBean {
    //    {
//        "title": "1、如果您近期打算购车，哪些项目是您主要考虑的?",
//            "spanCount": 3,
//            "type": 0,
//            "data": [
//        "安全性能",
//                "操控性能",
//                "价格",
//                "内部空间",
//                "售后服务",
//                "智能系统",
//                "内饰风格",
//                "车辆外观"
//    ]
//    },
    private String title;
    private int spanCount;
    private int type;
    private List<String> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
