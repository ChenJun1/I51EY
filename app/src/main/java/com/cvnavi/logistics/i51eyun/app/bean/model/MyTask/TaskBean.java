package com.cvnavi.logistics.i51eyun.app.bean.model.MyTask;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/19.
 */
public class TaskBean implements Serializable {

    public String Letter_Oid;//清单ID
    public String Ticket_Count;//总票数
    public String Goods_Num;//总货物数量
    public String Goods_Weight;//总货物重量
    public String Bulk_Weight;//总货物体积
    public String Letter_Type;//清单类型
    public String Letter_Type_Oid;//清单类型ID
    public String TicketStatus;//清单完成状态
    public String Letter_Date;//配载时间
    public String Line_Oid;//线路ID(与Dict_Line_Sheet表关联)
    public String Line_Name;//线路名
    public String Line_Type;//线路类型(干线,支线)
    public String Traffic_Mode;//配载方式(配货、整车)/派车方式
    public String IsSH;//是否送货
    public String AllReceivableAccount;//总应收款
    public String Shuttle_Fee;//班车费/承运发货费（成本）
    public String Ticket_Fee;//收入
    public String Profit_Fee;//利润
    public String Prepayments;//预付款
    public String CarCode_No;//排班序号
    public String ZX_Fee;//装卸费（原来其他费用）
    public String Settlement_Fee;//剩余结款
    public String Pay_Type_Oid;//付款类型编号(与Dict_Pay_Type_Sheet关联)
    public String DriverSerial_Oid;//司机编号（与Dict_Driver_Sheet关联）
    public String Driver;//司机姓名
    public String Driver_Tel;//司机电话
    public String CarCodeSerial_Oid;//车辆ID（与Dict_CarCode_Sheet关联）
    public String CarCode;//车牌号
    public String GPS_Key;//车头GPS编号
    public String GPS;//GPS牌号
    public String BoxCarCode;//挂车号
    public String BoxGPS_Key;//车箱gps
    public String Operate_Name;//制单人
    public String Operate_Org;//制单机构
    public String Operate_DateTime;//制单时间
    public String IsAddLetter;//是否补录（是/否）
    public String Transaction_Status_Oid;//业务状态(与Dict_Transaction_Status_Sheet表关联)
    public String fullCar_Destination;//整车地址
    public String BLat;//整车地址坐标经度
    public String BLng;//整车地址坐标纬度

    public String CarCode_Key;
    public String Driver_Key;

}
