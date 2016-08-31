package com.cvnavi.logistics.i51eyun.app.bean.model.MyOrder;

import java.io.Serializable;

/**
 * Created by ${ChenJ} on 2016/7/29.
 */
public class ExceptionInfoBean implements Serializable{
    public String Serial_Oid;        //流水号
    public String All_Ticket_No;        //全票号
    public String Exception_Mode;        //异常类型
    public String Operate_DateTime;        //操作时间
    public String IMGCopies;        //图片个数
}
