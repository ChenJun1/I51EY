package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.GatherFeeStatistics.GatherFeeList;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.viewholder.ViewHolder;

import java.util.ArrayList;

/**
 * Created by fan on 2016/7/19.
 * 应收款详情list
 */
public class DriverAccountReveivableListAdapter extends BaseAdapter{
    private ArrayList<GatherFeeList> list;
    private Context context;

    public DriverAccountReveivableListAdapter(ArrayList<GatherFeeList> list, Context context) {
        this.list = list;
        this.context = context;

    }


    @Override
    public int getCount() {
        if (list == null) {
            return 0;

        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GatherFeeList bean = list.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_accounts_receiveable_lv_item, null);
        }

        TextView Ticket_No_text = ViewHolder.get(convertView,R.id.Ticket_No_text);
        TextView Gather_Fee_text = ViewHolder.get(convertView,R.id.Gather_Fee_text);
        TextView Operate_DateTime_text = ViewHolder.get(convertView,R.id.Operate_DateTime_text);
        TextView state_tv = ViewHolder.get(convertView,R.id.state_tv);
        TextView Fee_name_text = ViewHolder.get(convertView,R.id.Fee_name_text);

        SetViewValueUtil.setTextViewValue(Ticket_No_text,bean.Ticket_No);
        SetViewValueUtil.setTextViewValue(Gather_Fee_text,bean.Fee);
        SetViewValueUtil.setTextViewValue(Operate_DateTime_text,bean.Operate_DateTime);
        SetViewValueUtil.setTextViewValue(state_tv,bean.Gather_Status);
        SetViewValueUtil.setTextViewValue(Fee_name_text,bean.Fee_Name);

        if (bean.Gather_Status.equals("未收款")){
            state_tv.setTextColor(Utils.getResourcesColor(R.color.color_fd4e80));
        }else if (bean.Gather_Status.equals("已收款")){
            state_tv.setTextColor(Utils.getResourcesColor(R.color.color_55d48a));
        }else if (bean.Gather_Status.equals("差额收款")){
            state_tv.setTextColor(Utils.getResourcesColor(R.color.color_78a8fd));
        }
        return convertView;
    }
}
