package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myorder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskLookPicActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyOrder.LogisticsFollowNoteBean;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DriverMyOrderLogisticsFollowAdapter extends BaseAdapter {
    private List<LogisticsFollowNoteBean> list;
    private LayoutInflater inflater;
    private Context mContext;

    public DriverMyOrderLogisticsFollowAdapter(List<LogisticsFollowNoteBean> list, Context context) {
        super();
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.mContext=context;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public LogisticsFollowNoteBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LogisticsFollowNoteBean bean = getItem(position);
        final DriverMyOrderLogisticsFollowActivity activity= (DriverMyOrderLogisticsFollowActivity) mContext;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_logistics_follow, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (bean != null) {
            SetViewValueUtil.setTextViewValue(viewHolder.GoodsDateTimeTv, bean.Goods_DateTime);
            SetViewValueUtil.setTextViewValue(viewHolder.OperateDescriptionTv, bean.Operate_Description);

            if(!TextUtils.isEmpty(bean.IMGCopies)&&!bean.IMGCopies.equals("0")){
                viewHolder.picTv.setVisibility(View.VISIBLE);
                viewHolder.picTv.setText("查看 "+bean.IMGCopies);
            }else{
                viewHolder.picTv.setVisibility(View.GONE);
            }
        }

        viewHolder.picTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, MyTaskLookPicActivity.class);
                intent.putExtra(Constants.LogisticsFollowNoteBean,bean);
                activity.startActivity(intent);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.Goods_DateTime_tv)
        TextView GoodsDateTimeTv;
        @BindView(R.id.Operate_Description_tv)
        TextView OperateDescriptionTv;
        @BindView(R.id.pic_tv)
        TextView picTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
