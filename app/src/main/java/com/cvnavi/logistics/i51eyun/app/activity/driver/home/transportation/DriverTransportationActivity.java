package com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.DriverMainViewPagerAdapter;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/24.
 * 司机调度运输界面
 */
public class DriverTransportationActivity extends BaseActivity {
    private static final int LAST_DAY = 0;
    private static final int TO_DAY = 1;
    private static final int TOMORROW = 2;
    public static final int REQUEST_CODE_SEARCH = 0x12;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.titlt_tv)
    TextView titltTv;
    @BindView(R.id.search_iv)
    ImageView searchIv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.right_ll)
    LinearLayout rightLl;
    @BindView(R.id.add_ll)
    LinearLayout addLl;
    @BindView(R.id.search_ll)
    LinearLayout searchLl;
    private DriverMainViewPagerAdapter adapter;
    private ArrayList<Fragment> list = new ArrayList<Fragment>();

    @BindView(R.id.last_day_tv)
    TextView lastDayTv;
    @BindView(R.id.last_day_view)
    View lastDayView;
    @BindView(R.id.last_day_rl)
    RelativeLayout lastDayRl;
    @BindView(R.id.todaytv)
    TextView todaytv;
    @BindView(R.id.today_view)
    View todayView;
    @BindView(R.id.today_rl)
    RelativeLayout todayRl;
    @BindView(R.id.tomo_day_tv)
    TextView tomoDayTv;
    @BindView(R.id.tomorrow_view)
    View tomorrowView;
    @BindView(R.id.tomorrow_rl)
    RelativeLayout tomorrowRl;
    @BindView(R.id.vp)
    ViewPager vp;
    private DriverTransToDayFragment driverTransToDayFragment;
    public String beginDate = null;
    public String endDate = null;
    public boolean isSearchstate = false;


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DriverTransportationActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver_transportation);
        ButterKnife.bind(this);
        init();
    }


    private void init() {

        if (list == null) {
            list = new ArrayList<Fragment>();
        }

        list.add(DriverTransLastDayFragment.getInstance());
        driverTransToDayFragment = DriverTransToDayFragment.getInstance();
        list.add(driverTransToDayFragment);
        list.add(DriverTransTomorrowFragment.getInstance());
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
        vp.setCurrentItem(1);

        titltTv.setText(Utils.getResourcesString(R.string.scheduling_list));
        rightLl.setVisibility(View.VISIBLE);
    }

    private void changeState(int position) {
        if (position == LAST_DAY) {
            lastDayTv.setTextColor(0xff1f89d4);
            lastDayView.setVisibility(View.VISIBLE);

            todaytv.setTextColor(0xff5b5b5b);
            todayView.setVisibility(View.INVISIBLE);

            tomoDayTv.setTextColor(0xff5b5b5b);
            tomorrowView.setVisibility(View.INVISIBLE);
        } else if (position == TO_DAY) {

            lastDayTv.setTextColor(0xff5b5b5b);
            lastDayView.setVisibility(View.INVISIBLE);

            todaytv.setTextColor(0xff1f89d4);
            todayView.setVisibility(View.VISIBLE);

            tomoDayTv.setTextColor(0xff5b5b5b);
            tomorrowView.setVisibility(View.INVISIBLE);

        } else {

            lastDayTv.setTextColor(0xff5b5b5b);
            lastDayView.setVisibility(View.INVISIBLE);
            todaytv.setTextColor(0xff5b5b5b);
            todayView.setVisibility(View.INVISIBLE);
            tomoDayTv.setTextColor(0xff1f89d4);
            tomorrowView.setVisibility(View.VISIBLE);
        }

        vp.setCurrentItem(position);
    }


    @OnClick({R.id.last_day_rl, R.id.today_rl, R.id.tomorrow_rl, R.id.add_ll, R.id.search_ll, R.id.right_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.last_day_rl:
                changeState(LAST_DAY);
                break;
            case R.id.today_rl:
                changeState(TO_DAY);
                break;
            case R.id.tomorrow_rl:
                changeState(TOMORROW);
                break;
            case R.id.add_ll:
                if (Utils.checkOperate(Constants.OPERATE_CD_ADD)) {
                    startActivity(new Intent(DriverTransportationActivity.this, DriverAddCarSchedulingActivity.class));
                } else {
                    DialogUtils.showWarningToast("您没有该权限！");
                }

                break;
            case R.id.search_ll:
                DriverCarSchedulingSearchActivity.startActivity(this, REQUEST_CODE_SEARCH);
                break;
            case R.id.right_ll:
                break;
        }
    }


    @OnClick(R.id.back_llayout)
    public void onClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SEARCH) {
            beginDate = data.getStringExtra(DriverCarSchedulingSearchActivity.BEGIN_DATA);
            endDate = data.getStringExtra(DriverCarSchedulingSearchActivity.END_DATA);
            changeState(TO_DAY);
        }

    }

}
