package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.myFleet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetMyCarFleetResponse;
import com.cvnavi.logistics.i51eyun.app.callback.MyOnClickItemListener;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.order.MyOrderListener;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/8/3.
 */
public class MyFleetAdapter extends BaseAdapter {
    private Context context;
    private List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean> list;
    private ViewHolder holder;
    private MyOnClickItemListener listener;
    private MyOrderListener orderListener;

    public MyFleetAdapter(Context context, List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean> list, MyOnClickItemListener listener, MyOrderListener orderListener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.orderListener = orderListener;
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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_my_fleet_item, null);
            holder.carCodeTv = (TextView) convertView.findViewById(R.id.carCode_tv);
            holder.locationTv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.orgNameTv = (TextView) convertView.findViewById(R.id.org_name_tv);
            holder.arm_iv = (ImageView) convertView.findViewById(R.id.arm_iv);
            holder.state_iv = (TextView) convertView.findViewById(R.id.state_iv);
            holder.locatin_iv = (ImageView) convertView.findViewById(R.id.locatin_iv);
            holder.root = (LinearLayout) convertView.findViewById(R.id.root_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetMyCarFleetResponse.DataValueBean.MCarInfoListBean info = list.get(position);

        if (info != null) {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.myOnClickItem(position, info);

                    }
                }
            });
            holder.carCodeTv.setText(info.getCarCode());
            holder.orgNameTv.setText(info.getOrg_Name());
            holder.locationTv.setText(info.getCHS_Address());
            if (info.getCarStatus().equals("2")) {
                holder.arm_iv.setVisibility(View.VISIBLE);
                holder.arm_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderListener != null) {
                            orderListener.onClickOrder(position);
                        }
                    }
                });
                holder.state_iv.setBackgroundResource(R.drawable.shape_fleet_interput);
                holder.state_iv.setText("信号中断");
                holder.locatin_iv.setImageResource(R.drawable.location_grey);
            } else if (info.getCarStatus().equals("1")) {
                holder.arm_iv.setVisibility(View.INVISIBLE);
                holder.arm_iv.setOnClickListener(null);
                holder.state_iv.setBackgroundResource(R.drawable.shape_fleet_ting);
                holder.state_iv.setText("停止中");
                holder.locatin_iv.setImageResource(R.drawable.location_red);

            } else {
                holder.arm_iv.setVisibility(View.INVISIBLE);
                holder.arm_iv.setOnClickListener(null);
                holder.state_iv.setBackgroundResource(R.drawable.shape_fleet_sudu);
                holder.state_iv.setText(info.getSpeed() + "km/小时");
                holder.locatin_iv.setImageResource(R.drawable.location_green1);
            }

        }
        return convertView;
    }


    private class ViewHolder {

        TextView carCodeTv;
        TextView locationTv;
        LinearLayout locationLl;
        TextView orgNameTv;
        LinearLayout jigouLl;
        LinearLayout root;
        ImageView arm_iv;
        TextView state_iv;
        ImageView locatin_iv;
    }
}
