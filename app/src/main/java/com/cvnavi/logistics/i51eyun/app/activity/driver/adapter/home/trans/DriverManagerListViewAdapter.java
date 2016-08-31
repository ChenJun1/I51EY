package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarSchedulingDriver;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ${ChenJ} on 2016/7/25.
 */
public class DriverManagerListViewAdapter extends BaseAdapter {

    private Context context;
    private List<mCarSchedulingDriver> driverList = new ArrayList<>();

    public DriverManagerListViewAdapter(List<mCarSchedulingDriver> dataList, Context context) {
        this.context = context;
        this.driverList = dataList;
    }

    @Override
    public int getCount() {
        return driverList.size();
    }

    @Override
    public Object getItem(int position) {
        return driverList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_driver_manager_item, null);
            holder.driveNameTv = (TextView) convertView.findViewById(R.id.driver_name_tv);
            holder.phoneNumTv = (TextView) convertView.findViewById(R.id.driver_phone_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final mCarSchedulingDriver carSchedulingDriver = driverList.get(position);
        holder.driveNameTv.setText(carSchedulingDriver.Driver);
        holder.phoneNumTv.setText(carSchedulingDriver.Driver_Tel);

        return convertView;
    }

    class ViewHolder {
        TextView driveNameTv;
        TextView phoneNumTv;
    }
}
