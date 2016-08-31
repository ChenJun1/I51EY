package com.cvnavi.logistics.i51eyun.app.bean.request;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/19.
 */
public class GetOrederListRequest {

    public String BeginDate;
    public String EndDate;

///**
// * type 0或者不传 搜索为全部
// * 1 未签收
// * 2 部分签收
// * 3 已经签收
// * /
    public String DeliverStatus;

    public String Search_No;

    public String SendMan_Tel;

    public String Driver_Tel;


}
