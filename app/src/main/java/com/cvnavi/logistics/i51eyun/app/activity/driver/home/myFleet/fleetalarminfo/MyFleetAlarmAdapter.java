package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetalarminfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.myFleet.mAlarmBean;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${ChenJ} on 2016/8/9.
 */
public class MyFleetAlarmAdapter extends BaseAdapter {
    private List<mAlarmBean> list;
    private LayoutInflater inflater;

    public MyFleetAlarmAdapter(List<mAlarmBean> list, Context context) {
        super();
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public mAlarmBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mAlarmBean bean = getItem(position);
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_give_an_alarm_info, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if(!TextUtils.isEmpty(bean.Alarm_Type_Oid)){
            switch (bean.Alarm_Type_Oid){
                case "AD":
                    viewHolder.AlarmTypeOidTv.setImageResource(R.drawable.caho);
                    break;
                case "AF":
                    viewHolder.AlarmTypeOidTv.setImageResource(R.drawable.dao);
                    break;
                case "AG":
                    viewHolder.AlarmTypeOidTv.setImageResource(R.drawable.li);
                    break;
                default:
                    viewHolder.AlarmTypeOidTv.setImageResource(R.drawable.li);
                    break;
            }
        }else{
            viewHolder.AlarmTypeOidTv.setImageResource(R.drawable.li);
        }
        if (bean != null) {
            SetViewValueUtil.setTextViewValue(viewHolder.CarCodeTv,bean.CarCode);
            SetViewValueUtil.setTextViewValue(viewHolder.AlarmDateTimeTv,bean.Alarm_DateTime);
            SetViewValueUtil.setTextViewValue(viewHolder.AlarmDescriptionTv,bean.CarCode+":"+bean.Alarm_Description);
            SetViewValueUtil.setTextViewValue(viewHolder.OverSpeedTimeTv,bean.OverSpeedTime);
        }


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.CarCode_tv)
        TextView CarCodeTv;
        @BindView(R.id.Alarm_DateTime_tv)
        TextView AlarmDateTimeTv;
        @BindView(R.id.Alarm_Description_tv)
        TextView AlarmDescriptionTv;
        @BindView(R.id.OverSpeedTime_tv)
        TextView OverSpeedTimeTv;
        @BindView(R.id.Alarm_Type_Oid_iv)
        ImageView AlarmTypeOidTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
