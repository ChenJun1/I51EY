package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.Model_LetterTrace_Node;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil.setTextViewValue;

/**
 * Created by ${ChenJ} on 2016/7/25.
 */
public class DriverCarLineAdapter extends BaseAdapter {

    private List<Model_LetterTrace_Node> list;
    private LayoutInflater inflater;
    private TaskLineLookPicListener lineLookPicListener;

    public DriverCarLineAdapter(List<Model_LetterTrace_Node> list, Context context, TaskLineLookPicListener lineLookPicListener) {
        super();
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.lineLookPicListener=lineLookPicListener;

    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Model_LetterTrace_Node getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Model_LetterTrace_Node node = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_singin_monitor_line_follow, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //判断是当前节点


        if (node != null) {
            setTextViewValue(viewHolder.arriveAddressTv, node.Org_Name);
            setTextViewValue(viewHolder.date1ValueTv, node.Arrive_DateTime);
            setTextViewValue(viewHolder.date2ValueTv, node.Leave_DateTime);

            if (position == 0) {
                viewHolder.date2NameTv.setText(Utils.getResourcesString(R.string.send_car_time));
                viewHolder.date1NameTv.setVisibility(View.GONE);
                setTextViewValue(viewHolder.date2ValueTv, node.Leave_DateTime);
                viewHolder.date1ValueTv.setVisibility(View.GONE);
                viewHolder.date2ValueTv.setVisibility(View.VISIBLE);
                viewHolder.date2NameTv.setVisibility(View.VISIBLE);
            }
//            if (position == getCount() - 1) {
//                viewHolder.date1NameTv.setText(R.string.reach_car_time);
//                viewHolder.date2NameTv.setVisibility(View.GONE);
//            }
            viewHolder.picLl.setVisibility(View.GONE);
//            if(TextUtils.isEmpty(node.ArriveImgNo)||node.ArriveImgNo.equals("0")){
//            }else{
//                viewHolder.picLl.setVisibility(View.VISIBLE);
//                viewHolder.picTv.setText("查看 "+node.ArriveImgNo);
//                viewHolder.picLl.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        lineLookPicListener.onLookPic(node);
//                    }
//                });
//            }
            if (!TextUtils.isEmpty(node.Arrive_DateTime)) {
                viewHolder.date1NameTv.setText("到达  " + node.City);
                viewHolder.arriveAddressTv.setTextColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.date1ValueTv.setTextColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.date1NameTv.setTextColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.progressStateIv.setImageResource(R.drawable.arrived);
                if (position == 0) {
                    viewHolder.progressV.setBackgroundColor(Utils.getResourcesColor(R.color.color_71B2EE));
                }
            } else {
                viewHolder.date1NameTv.setVisibility(View.GONE);
                viewHolder.date1ValueTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(node.Leave_DateTime)) {
                viewHolder.arriveAddressTv.setTextColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.date2ValueTv.setTextColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.date2NameTv.setTextColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.progressV.setBackgroundColor(Utils.getResourcesColor(R.color.color_71B2EE));
                viewHolder.progressStateIv.setImageResource(R.drawable.arrived);
            } else {
                viewHolder.date2NameTv.setVisibility(View.GONE);
                viewHolder.date2ValueTv.setVisibility(View.GONE);
            }

            if(position>0&&position<list.size()-1){
                if(!TextUtils.isEmpty(node.Arrive_DateTime)&&TextUtils.isEmpty(node.Leave_DateTime)){
                    if(TextUtils.isEmpty(getItem(position+1).Arrive_DateTime))
                        viewHolder.progressStateIv.setImageResource(R.drawable.circle);
                }
            }
        }


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.progress_state_iv)
        ImageView progressStateIv;
        @BindView(R.id.progress_v)
        View progressV;
        @BindView(R.id.arrive_address_tv)
        TextView arriveAddressTv;
        @BindView(R.id.date1_name_tv)
        TextView date1NameTv;
        @BindView(R.id.date1_value_tv)
        TextView date1ValueTv;
        @BindView(R.id.date2_name_tv)
        TextView date2NameTv;
        @BindView(R.id.date2_value_tv)
        TextView date2ValueTv;
        @BindView(R.id.pic_tv)
        TextView picTv;
        @BindView(R.id.pic_ll)
        LinearLayout picLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface TaskLineLookPicListener{
        void onLookPic(Model_LetterTrace_Node node);
    }
}
