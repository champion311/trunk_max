package com.shosen.max.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "allowance_table")
public class TableBean {

    @SmartColumn(id = 1, name = "日期")
    private String date;

    @SmartColumn(id = 2, name = "好友名称")
    private String friendName;

    public TableBean(String date, String friendName, String contactMethod, String payMoney) {
        this.date = date;
        this.friendName = friendName;
        this.contactMethod = contactMethod;
        this.payMoney = payMoney;
    }

    @SmartColumn(id = 3, name = "联系方式")
    private String contactMethod;

    @SmartColumn(id = 4, name = "已支付金额")
    private String payMoney;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}
