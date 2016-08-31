package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.DriverMainViewPagerAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarQuery;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/1.
 */
public class DriverRecordMainActivity extends BaseActivity {

    private static final int ANALYSIS = 0;
    private static final int DETAIL = 1;

    @BindView(R.id.back_llayout)
    LinearLayout backLinearLayout;
    @BindView(R.id.title_tv)
    TextView titltTextView;
    @BindView(R.id.operation_btn)
    Button operationBtn;
    @BindView(R.id.operation_llayout)
    LinearLayout operationLlayout;

    private DriverMainViewPagerAdapter adapter;
    private ArrayList<Fragment> list = new ArrayList<Fragment>();

    @BindView(R.id.analysis_tv)
    TextView analysisTv;
    @BindView(R.id.analysis_view)
    View analysisView;
    @BindView(R.id.analysis_rl)
    RelativeLayout analysisRl;
    @BindView(R.id.detail_tv)
    TextView detailTv;
    @BindView(R.id.detail_view)
    View detailView;
    @BindView(R.id.detail_rl)
    RelativeLayout detailRl;
    @BindView(R.id.vp)
    ViewPager vp;

    public String startTime;
    public String endTime;
    public String carCodeKey;

    private String carCode;


    public mCarQuery carQuery;

    public double BLng;//百度经度
    public double BLat;//百度维度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_record_main);
        ButterKnife.bind(this);
        initView();
        init();
    }

    public void initView() {
        carQuery = new mCarQuery();
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        carCodeKey = getIntent().getStringExtra("carCodeKey");
        carCode = getIntent().getStringExtra("carCode");
        titltTextView.setText(carCode);

    }

    private void init() {

        if (operationBtn.getVisibility() == View.GONE) {
            operationBtn.setVisibility(View.VISIBLE);

        }
        operationBtn.setText("地图");

        if (list == null) {
            list = new ArrayList<Fragment>();
        }

        list.add(DriverRecordAnalysisFragment.getInstance());
        list.add(DriverRecordDetailFragment.getInstance());
        if (adapter == null) {
            adapter = new DriverMainViewPagerAdapter(getSupportFragmentManager(), list);
        }

        vp.setAdapter(adapter);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(0);
    }


    private void changeState(int position) {
        if (position == ANALYSIS) {
            analysisTv.setTextColor(0xff1f89d4);
            analysisView.setVisibility(View.VISIBLE);
            detailTv.setTextColor(0xff5b5b5b);
            detailView.setVisibility(View.INVISIBLE);
        } else {
            analysisTv.setTextColor(0xff5b5b5b);
            analysisView.setVisibility(View.INVISIBLE);
            detailTv.setTextColor(0xff1f89d4);
            detailView.setVisibility(View.VISIBLE);
        }
        vp.setCurrentItem(position);
    }


    @OnClick({R.id.analysis_rl, R.id.detail_rl, R.id.back_llayout, R.id.operation_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.operation_btn:

                Intent intent = new Intent(this, DriverTrackMapActivity.class);
                intent.putExtra("ActivityName", "DriverRecordMainActivity");
                startActivity(intent);

                break;

            case R.id.back_llayout:
                finish();

                break;
            case R.id.analysis_rl:
                changeState(ANALYSIS);


                break;
            case R.id.detail_rl:
                changeState(DETAIL);
                break;
        }
    }


}
