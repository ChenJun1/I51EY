package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarShift;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.trans.OnClickItemListener;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;

import java.util.List;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/27.
 * 司机调度运输界面listview 适配器
 */
public class DriverTransListViewAdapter extends BaseAdapter {

    private Context context;
    private List<mCarShift> list;
    private ViewHolder holder;
    private OnClickItemListener listener;

    public DriverTransListViewAdapter(List<mCarShift> list, Context context, OnClickItemListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
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


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_driver_home_trans_listview_item, null);
            holder.typeBtn = (Button) convertView.findViewById(R.id.type_btn);
            holder.licenseTv = (TextView) convertView.findViewById(R.id.license_tv);
            holder.stateTv = (TextView) convertView.findViewById(R.id.state_tv);
            holder.orderTv = (TextView) convertView.findViewById(R.id.order_tv);
            holder.lineBtn = (Button) convertView.findViewById(R.id.line_btn);
            holder.routeTv = (TextView) convertView.findViewById(R.id.route_tv);
            holder.scheduleDateTv = (TextView) convertView.findViewById(R.id.scheduleDate_tv);
            holder.preDepartureTv = (TextView) convertView.findViewById(R.id.preDeparture_tv);
            holder.realDepartureTv = (TextView) convertView.findViewById(R.id.realDeparture_tv);
            holder.layout_ll = (LinearLayout) convertView.findViewById(R.id.layout_ll);
            holder.BoxCarCode_tv = (TextView) convertView.findViewById(R.id.BoxCarCode_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setInfo(position, holder);

        return convertView;
    }


    private void setInfo(final int position, ViewHolder holder) {
        final mCarShift info = list.get(position);

        if (info != null) {
            if (info.Traffic_Mode != null) {
                holder.typeBtn.setVisibility(View.VISIBLE);
                if (info.Traffic_Mode.equals("整车")) {
                    holder.typeBtn.setBackgroundResource(R.drawable.shape_rounded_btn_8ddc8c);
                    holder.typeBtn.setText("整");
                } else {
                    holder.typeBtn.setText("配");
                    holder.typeBtn.setBackgroundResource(R.drawable.shape_rounded_btn_8ddc8c);
                }
            } else {
                holder.typeBtn.setVisibility(View.GONE);
            }
            holder.typeBtn.setVisibility(View.GONE);

            String BoxCarCode = "";
            if (TextUtils.isEmpty(info.BoxCarCode)) {
                BoxCarCode = "无";
            } else {
                BoxCarCode = info.BoxCarCode;
            }
            SetViewValueUtil.setTextViewValue(holder.BoxCarCode_tv, BoxCarCode);

            holder.layout_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(position);
                    }
                }
            });

            holder.layout_ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onLongClick(info, position);
                    }
                    return false;
                }
            });


            holder.typeBtn.setText(info.Traffic_Mode);
            holder.licenseTv.setText(info.CarCode);
            holder.lineBtn.setText(info.Line_Type);
            holder.routeTv.setText(info.Line_Name);


//            LogUtil.d("-->> 排班日期 CarCode_Date = " + info.CarCode_Date + "||预发车时间 Forecast_Leave_DateTime= " + info.Forecast_Leave_DateTime);
//            排班日期 CarCode_Date = 20160714||预发车时间 Forecast_Leave_DateTime= 2016-07-13 11:56:00
            holder.scheduleDateTv.setText(DateUtil.strOldFormat2NewFormat(info.CarCode_Date, DateUtil.FORMAT_YMD_SN, DateUtil.FORMAT_MD));
            holder.preDepartureTv.setText(DateUtil.strOldFormat2NewFormat(info.Forecast_Leave_DateTime, DateUtil.FORMAT_YMDHM, DateUtil.FORMAT_MDHM));
            holder.realDepartureTv.setText(DateUtil.strOldFormat2NewFormat(info.Leave_DateTime, DateUtil.FORMAT_YMDHM, DateUtil.FORMAT_MDHM));
            holder.stateTv.setText(info.Schedule_Status);

            if (info.Line_Type.equals("干线")) {
                holder.lineBtn.setBackgroundResource(R.drawable.shape_rounded_btn_f56164);
                holder.lineBtn.setText("干");
            } else {
                holder.lineBtn.setBackgroundResource(R.drawable.shape_rounded_btn_8e61d2);
                holder.lineBtn.setText("支");
            }

            if (!TextUtils.isEmpty(info.CarCode_No)) {
                switch (Integer.parseInt(info.CarCode_No)) {
                    case 1:
                        holder.orderTv.setBackgroundResource(R.drawable.placeholder1);
                        break;
                    case 2:
                        holder.orderTv.setBackgroundResource(R.drawable.placeholder2);
                        break;
                    case 3:
                        holder.orderTv.setBackgroundResource(R.drawable.placeholder3);
                        break;
                    case 4:
                        holder.orderTv.setBackgroundResource(R.drawable.placeholder4);
                        break;
                    default:
                        holder.orderTv.setBackgroundResource(R.drawable.placeholder4);
                        break;
                }
            }

            holder.orderTv.setText(info.CarCode_No);
        }

    }


    class ViewHolder {
        Button typeBtn;
        TextView licenseTv;
        TextView stateTv;
        TextView orderTv;
        Button lineBtn;
        TextView routeTv;
        TextView scheduleDateTv;
        TextView preDepartureTv;
        TextView realDepartureTv;
        LinearLayout layout_ll;
        TextView BoxCarCode_tv;

    }
}
