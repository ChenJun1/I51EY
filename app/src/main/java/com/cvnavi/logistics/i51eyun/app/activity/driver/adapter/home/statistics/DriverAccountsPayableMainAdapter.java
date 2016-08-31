package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.PaymentStatistics.PayFeeStatistics;
import com.cvnavi.logistics.i51eyun.app.bean.model.ReceivableAccount.mReceivableAccount;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.widget.viewholder.ViewHolder;

import java.util.ArrayList;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/30.
 */
public class DriverAccountsPayableMainAdapter extends BaseAdapter {

    private mReceivableAccount list;
    private Context context;
    private ArrayList<PayFeeStatistics> mGatherFeeStatistics;

    public DriverAccountsPayableMainAdapter(ArrayList<PayFeeStatistics> mGatherFeeStatistics, Context context) {
        this.mGatherFeeStatistics = mGatherFeeStatistics;
        this.context = context;

    }


    @Override
    public int getCount() {
        if (mGatherFeeStatistics == null) {
            return 0;
        }
        return mGatherFeeStatistics.size();
    }

    @Override
    public Object getItem(int position) {
        return mGatherFeeStatistics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PayFeeStatistics Bean = mGatherFeeStatistics.get(position);
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_account_payable_main_lv_item, null);
        }

        TextView Fee_text  = ViewHolder.get(convertView,R.id.Fee_text);
        TextView Fee_Name_text  = ViewHolder.get(convertView,R.id.Fee_Name_text);
        TextView Gather_Fee_text  = ViewHolder.get(convertView,R.id.Gather_Fee_text);
        TextView Surplus_Gather_Fee_text  = ViewHolder.get(convertView,R.id.Surplus_Gather_Fee_text);

        SetViewValueUtil.setTextViewValue(Fee_text,Bean.Fee);
        SetViewValueUtil.setTextViewValue(Fee_Name_text,Bean.Fee_Name);
        SetViewValueUtil.setTextViewValue(Gather_Fee_text,Bean.Pay_Fee);
        SetViewValueUtil.setTextViewValue(Surplus_Gather_Fee_text,Bean.Surplus_Pay_Fee);

        return convertView;
    }
}
