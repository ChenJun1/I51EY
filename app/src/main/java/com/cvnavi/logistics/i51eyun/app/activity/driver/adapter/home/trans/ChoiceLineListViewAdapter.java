package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mLineInfo;

import java.util.List;

/**
 * Created by ${ChenJ} on 2016/7/25.
 */
public class ChoiceLineListViewAdapter extends BaseAdapter {

    private Context context;
    private List<mLineInfo> dataList;


    public ChoiceLineListViewAdapter(List<mLineInfo> list, Context context) {
        this.context = context;
        this.dataList = list;
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_choice_line_item, null);
            holder.lineTypeTv = (TextView) convertView.findViewById(R.id.line_type_tv);
            holder.lineNameTv = (TextView) convertView.findViewById(R.id.line_name_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final mLineInfo lineInfo = dataList.get(position);
        holder.lineTypeTv.setText(lineInfo.Line_Type);
        holder.lineNameTv.setText(lineInfo.Line_Name);

        return convertView;
    }

    class ViewHolder {
        TextView lineTypeTv;
        TextView lineNameTv;
    }
}
