package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;

import java.util.ArrayList;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/23.
 */
public class DriverHomeFragmentYeWuAdapter extends BaseAdapter {

    private ArrayList<mMainService> list;
    private Context context;
    private ViewHolder viewHolder;

    public DriverHomeFragmentYeWuAdapter(ArrayList<mMainService> list, Context context) {
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
        if (list == null) {
            return 0;
        }
        return list.size();
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_driver_home_yewu_gv_item, null);

            viewHolder.menuItemImageView = (ImageView) convertView.findViewById(R.id.menuitem_iv);
            viewHolder.menuItemNameTextView = (TextView) convertView.findViewById(R.id.menuitem_tv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mMainService info = list.get(position);
        if (info != null) {
            switch (Integer.parseInt(info.ServiceID)) {
                case Constants.HOME_LOCATION_JK:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_jk);
                    break;
                case Constants.HOME_LOCATION_GJ:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_gj);
                    break;
                case Constants.HOME_LOCATION_YS:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_ys);
                    break;
                case Constants.HOME_LOCATION_BJ:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_bj);
                    break;
                case Constants.HOME_LOCATION_CL:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_cl);
                    break;
                case Constants.HOME_LOCATION_CLT:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_clt);
                    break;
                case Constants.HOME_LOCATION_YC:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_dingwei_yc);
                    break;

                case Constants.HOME_BUSINISS_HD:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_yewu_hd);
                    break;
                case Constants.HOME_BUSINISS_TJ:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_yewu_tj);
                    break;
                case Constants.HOME_BUSINISS_SJG:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_yewu_sjg);
                    break;
                case Constants.HOME_BUSINISS_RW:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_yewu_rw);
                    break;
                case Constants.HOME_BUSINISS_HDE:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_yewu_hde);
                case Constants.HOME_BUSINISS_CD:
                    viewHolder.menuItemImageView.setImageResource(R.drawable.home_yewu_cd);
            }

            viewHolder.menuItemNameTextView.setText(info.ServiceName);
        }


        return convertView;
    }


    private class ViewHolder {
        public ImageView menuItemImageView;
        public TextView menuItemNameTextView;
    }
}
