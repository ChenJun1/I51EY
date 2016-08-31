package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarSchedulingDriver;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.transportation.ChoiceDriverLinstener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${ChenJ} on 2016/7/25.
 */
public class ChoiceDriverListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<mCarSchedulingDriver> mSchedulingDrivers = new ArrayList<>();
    private ChoiceDriverLinstener linstener;

    public ChoiceDriverListViewAdapter(List<mCarSchedulingDriver> list, Context context, ChoiceDriverLinstener linstener) {
        this.mSchedulingDrivers = list;
        this.mContext = context;
        this.linstener = linstener;
    }

    @Override
    public int getCount() {
        return mSchedulingDrivers.size();
    }

    @Override
    public Object getItem(int position) {
        return mSchedulingDrivers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final mCarSchedulingDriver carSchedulingDriver = mSchedulingDrivers.get(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_choice_driver_item, null);
            holder.driveNameTv = (TextView) convertView.findViewById(R.id.driver_name_tv);
            holder.phoneNumTv = (TextView) convertView.findViewById(R.id.driver_phone_tv);
            holder.driver_ll = (LinearLayout) convertView.findViewById(R.id.driver_ll);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.driveNameTv.setText(carSchedulingDriver.Driver);
        holder.phoneNumTv.setText(carSchedulingDriver.Driver_Tel);
//        holder.driver_ll.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });

        holder.driver_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linstener != null) {
                    linstener.onClick(position + 1);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView driveNameTv;
        TextView phoneNumTv;
        LinearLayout driver_ll;
    }
}
