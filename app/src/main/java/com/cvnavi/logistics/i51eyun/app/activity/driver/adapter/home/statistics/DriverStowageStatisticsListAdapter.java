package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskCarryDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.TaskBean;
import com.cvnavi.logistics.i51eyun.app.bean.model.mGetStowageStatisticsList;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.widget.viewholder.ViewHolder;

import java.util.ArrayList;

/**
 * Created by fan on 2016/7/21.
 * 配载清单列表适配器
 */
public class DriverStowageStatisticsListAdapter extends BaseAdapter{

    private ArrayList<mGetStowageStatisticsList> list;
    private Context context;

    public DriverStowageStatisticsListAdapter(ArrayList<mGetStowageStatisticsList> list, Context context) {
        this.list = list;
        this.context = context;

    }
    @Override
    public int getCount() {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        mGetStowageStatisticsList  bean  = list.get(position);
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_stowage_statistics_list_item,parent,false);

        }

        TextView Operate_DateTime = ViewHolder.get(convertView,R.id.Operate_DateTime);
        Button Traffic_Mode_btn = ViewHolder.get(convertView,R.id.Traffic_Mode_btn);
        TextView CarCode_text = ViewHolder.get(convertView,R.id.CarCode_text);
        Button Line_Oid_btn = ViewHolder.get(convertView,R.id.Line_Oid_btn);
        TextView Line_Name_text = ViewHolder.get(convertView,R.id.Line_Name_text);
        TextView Ticket_Count_text = ViewHolder.get(convertView,R.id.Ticket_Count_text);
        TextView Profit_Fee_text = ViewHolder.get(convertView,R.id.Profit_Fee_text);
//        TextView Letter_Status_text = ViewHolder.get(convertView,R.id.Letter_Status_text);
        TextView BoxCarCode_text  = ViewHolder.get(convertView,R.id.BoxCarCode_text);
        TextView sendCity_tv  = ViewHolder.get(convertView,R.id.sendCity_tv);
        TextView weiqianshou  = ViewHolder.get(convertView,R.id.weiqianshou);
        TextView arriveCity_tv  = ViewHolder.get(convertView,R.id.arriveCity_tv);
        View view = ViewHolder.get(convertView,R.id.view);
        LinearLayout stowage_layout = ViewHolder.get(convertView,R.id.stowage_layout);
        LinearLayout end_time_text_layout = ViewHolder.get(convertView,R.id.end_time_text_layout);


        if (position>0){
            if (!TextUtils.isEmpty(bean.Letter_Date)
                    && !TextUtils.isEmpty(DateUtil.strOldFormat2NewFormat(list.get(position - 1).Letter_Date,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD))
                    && DateUtil.strOldFormat2NewFormat(bean.Letter_Date,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD).equals(DateUtil.strOldFormat2NewFormat(list.get(position - 1).Letter_Date,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD))) {
                view.setVisibility(View.GONE);
                Operate_DateTime.setVisibility(View.GONE);
            } else {
                Operate_DateTime.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                Operate_DateTime.setText(DateUtil.strOldFormat2NewFormat(bean.Letter_Date,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD));
            }
        }else {
            Operate_DateTime.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            Operate_DateTime.setText(DateUtil.strOldFormat2NewFormat(bean.Letter_Date,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD));
        }

        SetViewValueUtil.setTextViewValue(Traffic_Mode_btn,bean.Traffic_Mode);
        SetViewValueUtil.setTextViewValue(CarCode_text,bean.CarCode);
        SetViewValueUtil.setTextViewValue(Line_Oid_btn,bean.Line_Type);
        if (!TextUtils.isEmpty(bean.Line_Name)){
            SetViewValueUtil.setTextViewValue(Line_Name_text,bean.Line_Name);
        }else {
            SetViewValueUtil.setTextViewValue(Line_Name_text,"线路：无");
        }

        SetViewValueUtil.setTextViewValue(Ticket_Count_text,bean.Ticket_Count+"票");
        SetViewValueUtil.setTextViewValue(Profit_Fee_text,bean.Profit_Fee+"元");
//        SetViewValueUtil.setTextViewValue(Letter_Status_text,bean.Letter_Status);
        SetViewValueUtil.setTextViewValue(BoxCarCode_text,bean.BoxCarCode+"挂");
        SetViewValueUtil.setTextViewValue(sendCity_tv,DateUtil.strOldFormat2NewFormat(bean.Leave_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MDHM));
        SetViewValueUtil.setTextViewValue(arriveCity_tv,DateUtil.strOldFormat2NewFormat(bean.Arrive_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MDHM));

        if (TextUtils.isEmpty(bean.Arrive_DateTime)){
            end_time_text_layout.setVisibility(View.GONE);
            weiqianshou.setVisibility(View.VISIBLE);
        }else {
            end_time_text_layout.setVisibility(View.VISIBLE);
            weiqianshou.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(bean.BoxCarCode)){
            SetViewValueUtil.setTextViewValue(BoxCarCode_text,"无");
        }

        stowage_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list!=null){
                    mGetStowageStatisticsList  bean = list.get(position);
                    Intent intent = new Intent(context, MyTaskCarryDetailedActivity.class);
                    TaskBean taskBean=bean;
                    intent.putExtra(Constants.TASKINFO,taskBean);
                    intent.putExtra(Constants.DriverStowageStatisticsListActivity,Constants.DriverStowageStatisticsListActivity);
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }
}
