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
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverTransportationActivity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fan on 2016/7/11.
 */
public class DriverTransportationListActivity extends BaseActivity {

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
                Intent intent = new Intent(DriverTransportationListActivity.this, DriverTransportationActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initView() {

        titleTv.setText("调度运输");

        mList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            mList.add("排班列表");
        }
        mAdapter = new DriverTransportationListAdapter(mList, this);
        lv.setAdapter(mAdapter);
    }


    @OnClick(R.id.back_llayout)
    public void onClick() {
        finish();
    }
}
