package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mGetOrederList;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.widget.viewholder.ViewHolder;

import java.util.ArrayList;

/**
 * Created by fan on 2016/7/20.
 */
public class DriverOrderListAdapter extends BaseAdapter{

    private ArrayList<mGetOrederList> list;
    private Context context;

    public DriverOrderListAdapter(ArrayList<mGetOrederList> list, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        mGetOrederList  bean  = list.get(position);
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_order_list_item,parent,false);

        }

        TextView Operate_DateTime = ViewHolder.get(convertView,R.id.Operate_DateTime);
        TextView Ticket_No_text = ViewHolder.get(convertView,R.id.Ticket_No_text);
        TextView SendStation_text = ViewHolder.get(convertView,R.id.SendStation_text);
        TextView ArrStation_text = ViewHolder.get(convertView,R.id.ArrStation_text);
//        TextView Goods_Breed_text = ViewHolder.get(convertView,R.id.Goods_Breed_text);
//        TextView Goods_Num = ViewHolder.get(convertView,R.id.Goods_Num);
        TextView Ticket_Fee_text = ViewHolder.get(convertView,R.id.Ticket_Fee_text);
        TextView weiqianshou = ViewHolder.get(convertView,R.id.weiqianshou);
        TextView zuofei = ViewHolder.get(convertView,R.id.zuofei);
        LinearLayout qianshou_layout = ViewHolder.get(convertView,R.id.qianshou_layout);

        View view = ViewHolder.get(convertView,R.id.view);


        if (position>0){
            if (!TextUtils.isEmpty(bean.Operate_DateTime)
                    && !TextUtils.isEmpty(DateUtil.strOldFormat2NewFormat(list.get(position - 1).Operate_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD))
                    && DateUtil.strOldFormat2NewFormat(bean.Operate_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD).equals(DateUtil.strOldFormat2NewFormat(list.get(position - 1).Operate_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD))) {
                view.setVisibility(View.GONE);
                Operate_DateTime.setVisibility(View.GONE);
            } else {
                Operate_DateTime.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                Operate_DateTime.setText(DateUtil.strOldFormat2NewFormat(bean.Operate_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD));
            }
        }else {
            Operate_DateTime.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            Operate_DateTime.setText(DateUtil.strOldFormat2NewFormat(bean.Operate_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MD));
        }

        SetViewValueUtil.setTextViewValue(Ticket_No_text,bean.Ticket_No);
        SetViewValueUtil.setTextViewValue(SendStation_text,DateUtil.strOldFormat2NewFormat(bean.Operate_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MDHM));
        SetViewValueUtil.setTextViewValue(Ticket_Fee_text,bean.Ticket_Fee+"元");

        if (bean.Deliver_Status.equals("未签收")){
            weiqianshou.setVisibility(View.VISIBLE);
            qianshou_layout.setVisibility(View.GONE);
        }else {
            weiqianshou.setVisibility(View.GONE);
            qianshou_layout.setVisibility(View.VISIBLE);
            SetViewValueUtil.setTextViewValue(ArrStation_text,DateUtil.strOldFormat2NewFormat(bean.Deliver_DateTime,DateUtil.FORMAT_YMDHMS,DateUtil.FORMAT_MDHM));
        }
        if (bean.Ticket_Status.equals("正常")){
            zuofei.setVisibility(View.GONE);
        }else if (bean.Ticket_Status.equals("作废")){
            zuofei.setVisibility(View.VISIBLE);
            weiqianshou.setVisibility(View.GONE);
        }



//        if (TextUtils.isEmpty(bean.Deliver_Status)){
//            weiqianshou.setVisibility(View.GONE);
//            qianshou_layout.setVisibility(View.VISIBLE);
//        }else {
//            weiqianshou.setVisibility(View.VISIBLE);
//            SetViewValueUtil.setTextViewValue(weiqianshou,bean.Deliver_Status);
//            qianshou_layout.setVisibility(View.GONE);
//        }

        return convertView;
    }
}
