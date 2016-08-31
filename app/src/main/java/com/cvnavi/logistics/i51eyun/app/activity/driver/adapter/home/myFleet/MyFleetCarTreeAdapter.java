package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.myFleet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarInfo;
import com.cvnavi.logistics.i51eyun.app.callback.MyOnClickItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权所有势航网络
 * Created by Chuzy on 2016/8/10.
 */
public class MyFleetCarTreeAdapter extends BaseAdapter {

    private List<mCarInfo> list;
    private Context context;
    private ViewHolder viewHolder;
    private MyOnClickItemListener listener;

    public MyFleetCarTreeAdapter(List<mCarInfo> list, Context context, MyOnClickItemListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_driver_car_list_parent_group, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final mCarInfo info = list.get(position);
        if (info != null) {
            viewHolder.parentGroupTV.setText(info.CarCode);
            viewHolder.rootLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.myOnClickItem(position, info);

                    }
                }
            });
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.parentGroupTV)
        TextView parentGroupTV;
        @BindView(R.id.root_ll)
        LinearLayout rootLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
