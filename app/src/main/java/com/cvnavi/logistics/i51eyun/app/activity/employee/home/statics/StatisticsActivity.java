package com.cvnavi.logistics.i51eyun.app.activity.employee.home.statics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location.DriverStaticAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location.DriverTransportationListAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics.DriverAccountsPayableMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics.DriverAccountsReceiveMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics.DriverOrderStatisticsActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics.DriverStowageStatisticsSummaryActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/25.
 */
public class StatisticsActivity extends BaseActivity {
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.lv)
    PullToRefreshListView lv;
    private GetAppLoginResponse data;

    private List<mMainService> list;
    private List<String> mList;
    private DriverStaticAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_transportation_list);
        ButterKnife.bind(this);
        data = MyApplication.getInstance().getLoginInfo();
        list = getBusinessList();
        titleTv.setText("统计");
        mAdapter = new DriverStaticAdapter(list, this);
        lv.setAdapter(mAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mMainService info = list.get(position - 1);
                if (info.ServiceID.equals("12")) {
                    Intent intent = new Intent(StatisticsActivity.this, DriverOrderStatisticsActivity.class);
                    startActivity(intent);
                } else if (info.ServiceID.equals("13")) {
                    Intent intent = new Intent(StatisticsActivity.this, DriverStowageStatisticsSummaryActivity.class);
                    startActivity(intent);
                } else if (info.ServiceID.equals("14")) {
                    Intent intent = new Intent(StatisticsActivity.this, DriverAccountsReceiveMainActivity.class);
                    startActivity(intent);
                } else if (info.ServiceID.equals("15")) {
                    Intent intent = new Intent(StatisticsActivity.this, DriverAccountsPayableMainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //获取业务list
    private ArrayList<mMainService> getBusinessList() {
        if (data == null) {
            return null;
        }

        List<mMainService> alllist = data.DataValue.MenuList;
        ArrayList<mMainService> dataList = new ArrayList<mMainService>();
        if (alllist != null) {
            //筛选
            for (int i = 0; i < alllist.size(); i++) {
                if ((alllist.get(i).ServiceType.equals("1") && alllist.get(i).ServiceID.equals("12")) || (alllist.get(i).ServiceType.equals("1") && alllist.get(i).ServiceID.equals("13")) || (alllist.get(i).ServiceType.equals("1") && alllist.get(i).ServiceID.equals("14")) || (alllist.get(i).ServiceType.equals("1") && alllist.get(i).ServiceID.equals("15"))) {
                    dataList.add(alllist.get(i));
                }


            }
        }

        return dataList;
    }


    @OnClick(R.id.back_llayout)
    public void onClick() {
        finish();
    }


}
