package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.widget.viewholder.ViewHolder;

import java.util.List;

/**
 * Created by fan on 2016/7/18.
 */
public class DriverMyGridtextAdapter extends BaseAdapter{
    private Context mContext;
    public List<String> img_text = null;
    public String[] imgs = null;

    public DriverMyGridtextAdapter(Context mContext, List<String> str, String[] imgs) {
        super();
        this.mContext = mContext;
        this.img_text = str;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return img_text.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_statistics_gv_item, parent, false);
        }
        TextView tv = ViewHolder.get(convertView, R.id.tv_item);
        TextView iv = ViewHolder.get(convertView, R.id.iv_item);
        tv.setText(img_text.get(position));
        iv.setText(imgs[position]);
        return convertView;
    }
}
