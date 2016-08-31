package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.cvnavi.logistics.i51eyun.app.R;

import java.util.ArrayList;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/23.
 */
public class DriverHomeFragmentListViewAdapter extends BaseAdapter {
    private  ArrayList<String> list;
    private Context context ;
    private ItemView itemView;

    public DriverHomeFragmentListViewAdapter(Context context, ArrayList<String> list){
        this.list = list;
        this.context = context;

    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if(list == null){
            return 0;

        }else{
            return list.size();
        }
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            itemView = new ItemView();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_driver_home_listview_item,null);
            itemView.textView = (TextView) convertView.findViewById(R.id.order_num_tv);
            convertView.setTag(itemView);

        }else{
            itemView = (ItemView) convertView.getTag();
        }

        itemView.textView.setText(list.get(position));

        return convertView;
    }


    private class ItemView{
        public TextView textView;
    }



}
