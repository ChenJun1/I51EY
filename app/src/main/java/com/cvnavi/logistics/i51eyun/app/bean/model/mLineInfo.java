package com.cvnavi.logistics.i51eyun.app.bean.model;

/**
 * 车辆排班线路选择
 * Created by JohnnyYuan on 2016/7/6.
 */
public class mLineInfo {

    public String Line_Oid;//	线路主键
    public String Company_Oid;//公司编号
    public String Line_Name;//线路名称
    public String Line_Type_Oid;//	线路类型1干线2支线
    public String Line_Type;//	线路类型
    public String Operate_Name;//	操作人
    public String Operate_Org;//	操作机构
    public String Operate_DateTime;//	操作时间
    public String Add_Status_Oid;//	默认1定位系统，2TMS系统，添加标志
    public String Begin_Reference_Marker_Key;//	开始节点
    public String End_Reference_Marker_Key;//	结束节点
    public String Dispatch_Type_Oid;//
    public String Punish_Rule_Oid;
    public String Punish_Fee;//
    public String IsPunish_Sum;


}
