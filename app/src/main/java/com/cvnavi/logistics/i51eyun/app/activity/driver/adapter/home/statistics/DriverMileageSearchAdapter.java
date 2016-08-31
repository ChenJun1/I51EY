package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetMileageResponse;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;

import java.util.List;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/12.
 */
public class DriverMileageSearchAdapter extends BaseAdapter {


    private List<GetMileageResponse.DataValueBean.ListMileageBean> listMileage;
    private Context context;
    private ItemView itemView;


    public DriverMileageSearchAdapter(List<GetMileageResponse.DataValueBean.ListMileageBean> listMileage, Context context) {
        this.listMileage = listMileage;
        this.context = context;
    }


    @Override
    public int getCount() {
        if (listMileage == null) {
            return 0;


        } else {
            return listMileage.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return listMileage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            itemView = new ItemView();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_mileage_search_item, null);
            itemView.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            itemView.data_tv = (TextView) convertView.findViewById(R.id.data_tv);
            itemView.mileage_tv = (TextView) convertView.findViewById(R.id.mileage_tv);
            convertView.setTag(itemView);

        } else {
            itemView = (ItemView) convertView.getTag();
        }

        GetMileageResponse.DataValueBean.ListMileageBean info = listMileage.get(position);
        if (info != null) {
            itemView.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            itemView.data_tv.setText(info.getSummary_Date());
//            itemView.mileage_tv.setText(info.getSummary_Mileage());
            itemView.mileage_tv.setText(String.format(Utils.getResourcesString(R.string.total_mile_km), info.getSummary_Mileage()));
        }

        return convertView;
    }


    private class ItemView {
        public RelativeLayout rl;
        public TextView data_tv;
        public TextView mileage_tv;

    }

}
