package com.cvnavi.logistics.i51eyun.app.bean.model;

import java.io.Serializable;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/20.
 */
public class mOrderDetail implements Serializable {
    public String All_Ticket_No; //全票号
    public String Ticket_No;//货单号
    public String SendStation;//始发地
    public String ArrStation;//到达地
    public String ArrStation_Area;//到达站区县
    public String Distribution_Status; //配送状态
    public String Ticket_Status;//货单状态
    public String Goods_Breed;//品名
    public String Goods_Casing;//包装
    public String Goods_Num;//件数
    public String Bulk_Weight;//重量
    public String Goods_Bulk; //方量
    public String Lack_Num;//缺单数量
    public String SendMan_Name;//发货人
    public String SendMan_Tel;//发货人电话
    public String SendMan_Org;//发货单位
    public String SendMan_Address; //发货地址
    public String SendGoods_Ask;//要求
    public String ReceiveMan_Name;//收货人
    public String ReceiveMan_Tel;//收货人电话
    public String ReceiveMan_Org;//收货单位
    public String ReceiveMan_Address;//收货地址
    public String Ticket_Note;//备注
    public String YSK_Fee;//应收款
    public String UnDeliverGoods_Num;//未签收数量
    public String Deliver_Status_Oid;//签收状态(0未签收1部分签收2已签收)
}
