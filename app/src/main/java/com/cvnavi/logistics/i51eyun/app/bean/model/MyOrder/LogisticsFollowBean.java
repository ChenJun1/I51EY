package com.cvnavi.logistics.i51eyun.app.bean.model.MyOrder;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class LogisticsFollowBean {
    public String Ticket_No;           //货单号
    public String Transport_Status;           //物流当前状态
    public List<LogisticsFollowNoteBean> TrackTicket;
}
