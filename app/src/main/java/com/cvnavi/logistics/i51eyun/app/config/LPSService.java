package com.cvnavi.logistics.i51eyun.app.config;

/**
 * Created by JohnnyYuan on 2016/7/4.
 */
public class LPSService extends HostAddress {
    public static String LPSService_Address = Host_Url + "/LPS/LPS/";

    // 车辆列表查询
    public static String GetCarList_TAG = "GetCarList";
    public static String GetCarList_Request_Url = LPSService_Address + GetCarList_TAG;

    // 车辆监控
    public static String GetGPSInfo_TAG = "GetGPSInfo";
    public static String GetGPSInfo_Request_Url = LPSService_Address + GetGPSInfo_TAG;

    // 历史轨迹记录分析
    public static String GetHistoryLocusAnalysis_TAG = "GetHistoryLocusAnalysis";
    public static String GetHistoryLocusAnalysis_Request_Url = LPSService_Address + GetHistoryLocusAnalysis_TAG;

    // 历史轨迹记录详情
    public static String GetHistoryLocus_TAG = "GetHistoryLocus";
    public static String GetHistoryLocus_Request_Url = LPSService_Address + GetHistoryLocus_TAG;

    // 获取车辆key，by车牌号
    public static String GetCarCodeKey_TAG = "GetCarCodeKey";
    public static String GetCarCodeKey_Request_Url = LPSService_Address + GetCarCodeKey_TAG;


    public static String GetMileages_TAG = "GetMileages";
    public static String GetMileages_Request_Url = LPSService_Address + GetMileages_TAG;

    //定位信息
    public static String GetCarGPSAggregate_TAG = "GetCarGPSAggregate";
    public static String GetCarGPSAggregate_Request_Url = LPSService_Address + GetCarGPSAggregate_TAG;
  //报警信息
    public static String GetAlarmInfo_TAG = "GetAlarmInfo";
    public static String GetAlarmInfo_Request_Url = LPSService_Address + GetAlarmInfo_TAG;

    //我的车队
    public static String GetMyCarFleet_TAG = "GetMyCarFleet";
    public static String GetMyCarFleet_Request_Url = LPSService_Address + GetMyCarFleet_TAG;


}
