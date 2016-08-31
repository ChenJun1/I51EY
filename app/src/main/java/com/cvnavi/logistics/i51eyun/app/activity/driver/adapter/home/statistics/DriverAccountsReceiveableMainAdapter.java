package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.ReceivableAccount.GatherFeeStatistics;
import com.cvnavi.logistics.i51eyun.app.bean.model.ReceivableAccount.mReceivableAccount;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.widget.viewholder.ViewHolder;

import java.util.ArrayList;

/**
 * Created by fan on 2016/7/19.
 */
public class DriverAccountsReceiveableMainAdapter extends BaseAdapter{
    private mReceivableAccount list;
    private Context context;
    private ArrayList<GatherFeeStatistics> mGatherFeeStatistics;

    public DriverAccountsReceiveableMainAdapter(ArrayList<GatherFeeStatistics> mGatherFeeStatistics, Context context) {
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
        GatherFeeStatistics Bean = mGatherFeeStatistics.get(position);
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_account_receiveable_main_lv_item, null);
        }

        TextView Fee_text  = ViewHolder.get(convertView,R.id.Fee_text);
        TextView Fee_Name_text  = ViewHolder.get(convertView,R.id.Fee_Name_text);
        TextView Gather_Fee_text  = ViewHolder.get(convertView,R.id.Gather_Fee_text);
        TextView Surplus_Gather_Fee_text  = ViewHolder.get(convertView,R.id.Surplus_Gather_Fee_text);

        SetViewValueUtil.setTextViewValue(Fee_text,Bean.Fee);
        SetViewValueUtil.setTextViewValue(Fee_Name_text,Bean.Fee_Name);
        SetViewValueUtil.setTextViewValue(Gather_Fee_text,Bean.Gather_Fee);
        SetViewValueUtil.setTextViewValue(Surplus_Gather_Fee_text,Bean.Surplus_Gather_Fee);

        return convertView;
    }

}
