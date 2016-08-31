package com.cvnavi.logistics.i51eyun.app.bean.litepal;

import org.litepal.crud.DataSupport;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/18.
 */
public class Inquiries extends DataSupport {


    private String userTel;//手机号


    private String orderId;//货单号
    private String arriving;//到达地
    private String data;//搜索日期
    private String bengining;//始发地
    private String All_Ticket_No;//全票号

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getArriving() {
        return arriving;
    }

    public void setArriving(String arriving) {
        this.arriving = arriving;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBengining() {
        return bengining;
    }

    public void setBengining(String bengining) {
        this.bengining = bengining;
    }

    public String getAll_Ticket_No() {
        return All_Ticket_No;
    }

    public void setAll_Ticket_No(String all_Ticket_No) {
        All_Ticket_No = all_Ticket_No;
    }


    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }
}
