package com.cvnavi.logistics.i51eyun.app.config;

/**
 * Created by JohnnyYuan on 2016/7/5.
 */
public class TMSService extends HostAddress {
    public static String TMSService_Address = Host_Url + "/TMS/TMS/";

    // 车辆排班查询
    public static String GetShiftList_TAG = "GetShiftList";
    public static String GetShiftList_Request_Url = TMSService_Address + GetShiftList_TAG;

    // 车辆排班添加
    public static String AddCarShift_TAG = "AddCarShift";
    public static String AddCarShift_Request_Url = TMSService_Address + AddCarShift_TAG;

    // 车辆排班修改
    public static String EditCarShift_TAG = "EditCarShift";
    public static String EditCarShift_Request_Url = TMSService_Address + EditCarShift_TAG;

    // 车辆排班删除
    public static String DelCarShift_TAG = "DelCarShift";
    public static String DelCarShift_Request_Url = TMSService_Address + DelCarShift_TAG;

    // 车辆排班线路选择
    public static String GetLineList_TAG = "GetLineList";
    public static String GetLineList_Request_Url = TMSService_Address + GetLineList_TAG;

    // 车辆排班司机选择
    public static String GetDriverList_TAG = "GetDriverList";
    public static String GetDriverList_Request_Url = TMSService_Address + GetDriverList_TAG;

    //删除司机
    public static String DelDriver_TAG = "DelDriver";
    public static String DelDriver_Request_Url = TMSService_Address + DelDriver_TAG;

    //添加司机
    public static String AddDriver_TAG = "AddDriver";
    public static String AddDriver_Request_Url = TMSService_Address + AddDriver_TAG;

    //修改司机
    public static String UpdateDriver_TAG = "EditDriver";
    public static String UpdateDriver_Request_Url = TMSService_Address + UpdateDriver_TAG;

    //我的任务列表
    public static String GetDetailedList_TAG = "GetDetailedList";
    public static String GetDetailedList_Request_Url = TMSService_Address + GetDetailedList_TAG;

    //我的任务 派车单明细
    public static String DetailedListDetail_TAG = "DetailedListDetail";
    public static String DetailedListDetail_Request_Url = TMSService_Address + DetailedListDetail_TAG;

    //我的任务 线路跟踪
    public static String GetCarLineNode_TAG = "GetCarLineNode";
    public static String GetCarLineNode_Request_Url = TMSService_Address + GetCarLineNode_TAG;

    //我的任务 确认到车 节点选择
    public static String LineNodeChoice_TAG = "LineNodeChoice";
    public static String LineNodeChoice_Request_Url = TMSService_Address + LineNodeChoice_TAG;

    //我的任务 线路跟踪 查看照片
    public static String SeeImage_TAG = "SeeImage";
    public static String SeeImage_Request_Url = TMSService_Address + SeeImage_TAG;

    //我的任务 到车确认
    public static String ShuttleBusConfirm_TAG = "ShuttleBusConfirm";
    public static String ShuttleBusConfirm_Request_Url = TMSService_Address + ShuttleBusConfirm_TAG;

    //我的任务 提货确认
    public static String DeliveryConfirm_TAG = "DeliveryConfirm";
    public static String DeliveryConfirm_Request_Url = TMSService_Address + DeliveryConfirm_TAG;

    //货单统计
    public static String OrederStatistics_TAG = "OrederStatistics";
    public static String GetOrederStatistics_Request_Url = TMSService_Address + OrederStatistics_TAG;


    //货单统计
    public static String Selectticket_TAG = "Selectticket";
    public static String Selectticket_Request_Url = TMSService_Address + Selectticket_TAG;//货单统计

    //应收款统计
    public static String GetReceivableAccount_TAG = "ReceivablesStatistics";
    public static String GetReceivableAccount_Request_Url = TMSService_Address + GetReceivableAccount_TAG;


    //应收款详情
    public static String GetReceivablesList_TAG = "GetReceivablesList";
    public static String GetReceivablesList_Request_Url = TMSService_Address + GetReceivablesList_TAG;


    //应付款统计
    public static String PaymentStatistics_TAG = "PaymentStatistics";
    public static String GetPaymentStatistics_Request_Url = TMSService_Address + PaymentStatistics_TAG;


    //应付款明细
    public static String GetPaymentList_TAG = "GetPaymentList";
    public static String GetPaymentList_Request_Url = TMSService_Address + GetPaymentList_TAG;


    //配载统计
    public static String StowageStatisticsSummary_TAG = "StowageStatisticsSummary";
    public static String GetStowageStatisticsSummary_Request_Url = TMSService_Address + StowageStatisticsSummary_TAG;


    //配载清单列表
    public static String GetStowageStatisticsList_TAG = "GetStowageStatisticsList";
    public static String GetStowageStatisticsList_Request_Url = TMSService_Address + GetStowageStatisticsList_TAG;

    //我的货单（列表）
    public static String GetOrederList_TAG = "GetOrederList";
    public static String GetOrederList_Request_Url = TMSService_Address + GetOrederList_TAG;

    //货单明细查询
    public static String OrederDetailed_TAG = "OrederDetailed";
    public static String OrederDetailed_Request_Url = TMSService_Address + OrederDetailed_TAG;

    //确认签收
    public static String ConfirmSign_TAG = "ConfirmSign";
    public static String ConfirmSign_Request_Url = TMSService_Address + ConfirmSign_TAG;

    public static String ConfirmDevlierSearch_TAG = "ConfirmDevlierSearch";
    public static String ConfirmDevlierSearch_Request_Url = TMSService_Address + ConfirmDevlierSearch_TAG;

    //车辆异常拍照
    public static String CarAbnormalPhoto_TAG = "CarAbnormalPhoto";
    public static String CarAbnormalPhoto_Request_Url = TMSService_Address + CarAbnormalPhoto_TAG;

    //货单异常拍照
    public static String OrderAbnormalPhoto_TAG = "OrderAbnormalPhoto";
    public static String OrderAbnormalPhoto_Request_Url = TMSService_Address + OrderAbnormalPhoto_TAG;

    //物流异常信息查询
    public static String GetExceptInfo_TAG = "GetExceptInfo";
    public static String GetExceptInfo_Request_Url = TMSService_Address + GetExceptInfo_TAG;

    //物流跟踪图片查询
    public static String SelectImage_TAG = "SelectImage";
    public static String SelectImage_Request_Url = TMSService_Address + SelectImage_TAG;

    //46．司机未开始的任务列表
    public static String GetDriverNoStartTask_TAG = "GetDriverNoStartTask";
    public static String GetDriverNoStartTask_Request_Url = TMSService_Address + GetDriverNoStartTask_TAG;



    //46．查询码长度
    public static String QuerySimpleCodeLength_TAG = "QuerySimpleCodeLength";
    public static String QuerySimpleCodeLength_Request_Url = TMSService_Address + QuerySimpleCodeLength_TAG;//46．查询码长度

    //．车辆异常图片查询
    public static String GetCarExceptImgInfo_TAG = "GetCarExceptImgInfo";
    public static String GetCarExceptImgInfo_Request_Url = TMSService_Address + GetCarExceptImgInfo_TAG;


}
