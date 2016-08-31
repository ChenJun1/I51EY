package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;

import java.util.List;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/25.
 */
public class DriverStaticAdapter extends BaseAdapter {
    private List<mMainService> mList;
    private Context mContext;
    private ItemView itemView;


    public DriverStaticAdapter(List<mMainService> mList, Context mContext) {

        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;

        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            itemView = new ItemView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_driver_transportation_list_item, null);
            itemView.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            convertView.setTag(itemView);
        } else {
            itemView = (ItemView) convertView.getTag();
        }
        itemView.content_tv.setText(mList.get(position).ServiceName);
        return convertView;
    }

    private class ItemView {
        public TextView content_tv;

    }
}
