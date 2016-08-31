package com.cvnavi.logistics.i51eyun.app.bean.model.StowageStatisticsSummary;

import java.util.List;

/**
 * Created by fan on 2016/7/21.
 * 配载统计数据
 */
public class mStowageStatisticsSummary {
    public String Letter_Count;//	发车数量
    public String Shuttle_Fee;//	成本
    public String Ticket_Fee;//	收入
    public String Profit_Fee;//	利润
    public String EntiretyCount;//	整车数量
    public String BulkloadCount;//	配货数量
    public List<DetailList> DetailList;//
}
