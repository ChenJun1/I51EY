package com.cvnavi.logistics.i51eyun.app.bean.model;

import java.io.Serializable;

/**
 * 车辆排班查询
 * Created by JohnnyYuan on 2016/7/5.
 */
public class mCarShift implements Serializable {
    /// <summary>
    /// 车牌号
    /// </summary>
    public String CarCode;
    /// <summary>
    /// 装车状态
    /// </summary>
    public String Traffic_Mode;
    /// <summary>
    /// 线路状态
    /// </summary>
    public String Line_Type;
    /// <summary>
    /// 线路
    /// </summary>
    public String Line_Oid;


    /// <summary>
    /// 配载状态
    /// </summary>
    public String Schedule_Status;
    /// <summary>
    /// 排班日期
    /// </summary>
    public String CarCode_Date;
    /// <summary>
    /// 预发车时间
    /// </summary>
    public String Forecast_Leave_DateTime;
    /// <summary>
    /// 实际发车时间
    /// </summary>
    public String Leave_DateTime;
    /// <summary>
    /// 排班顺序
    /// </summary>
    public String CarCode_No;
    /// <summary>
    /// 车辆
    /// </summary>
    public String CarCodeSerial_Oid;
    /// <summary>
    /// 主驾
    /// </summary>
    public String MainDriverSerial_Oid;
    /// <summary>
    /// 联系电话
    /// </summary>
    public String Driver_Tel;
    /// <summary>
    /// 副驾
    /// </summary>
    public String SecondDriverSerial_Oid;
    /// <summary>
    /// 备注
    /// </summary>
    public String CarSchedule_Note;

    /**
     * 主司机姓名
     */
    public String MainDriver;

    /**
     * 副司机姓名
     */
    public String SecondDriver;

    /**
     * 路线名称
     */
    public String Line_Name;

    //流水号
    public String Serial_Oid;

    public String LPSCarCode_Key;

    public String BoxCarCode_Key;//挂车Key

    public String BoxCarCode=null;//挂车号

}
