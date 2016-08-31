package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location.DriverTransportationListAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics.DriverMileageStatisticsActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverTransportationActivity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/12.
 */
public class DriverCarsStatisActivity extends BaseActivity {

    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.lv)
    PullToRefreshListView lv;

    private List<String> mList;
    private DriverTransportationListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_transportation_list);
        ButterKnife.bind(this);

        initView();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DriverCarsStatisActivity.this, DriverMileageStatisticsActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initView() {

        titleTv.setText("车辆统计");

        mList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            mList.add("里程统计");
        }
        mAdapter = new DriverTransportationListAdapter(mList, this);
        lv.setAdapter(mAdapter);
    }


    @OnClick(R.id.back_llayout)
    public void onClick() {
        finish();
    }
}
