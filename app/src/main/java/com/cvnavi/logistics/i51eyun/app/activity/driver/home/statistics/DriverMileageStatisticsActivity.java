package com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.DriverMainViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/12.
 * 里程统计Activity
 */
public class DriverMileageStatisticsActivity extends BaseActivity {
    private static final int LAST_DAY = 0;
    private static final int LAST_THREE_DAYS = 1;
    private static final int LAST_MONTHS = 2;
    @BindView(R.id.id_tab_line_iv)
    View idTabLineIv;
    private DriverMainViewPagerAdapter adapter;
    private ArrayList<Fragment> list = new ArrayList<Fragment>();

    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.titlt_tv)
    TextView titltTv;
    @BindView(R.id.search_iv)
    ImageView searchIv;
    @BindView(R.id.last_day_tv)
    TextView lastDayTv;
    @BindView(R.id.last_day_rl)
    RelativeLayout lastDayRl;
    @BindView(R.id.last_three_days_tv)
    TextView lastThreeDaysTv;
    @BindView(R.id.last_three_days_rl)
    RelativeLayout lastThreeDaysRl;
    @BindView(R.id.last_month_tv)
    TextView lastMonthTv;
    @BindView(R.id.last_month_rl)
    RelativeLayout lastMonthRl;
    @BindView(R.id.vp)
    ViewPager vp;

    private int currentIndex;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_payable_main);
        ButterKnife.bind(this);
        init();
        initTabLineWidth();
    }


    private void init() {

        titltTv.setText("里程统计");
        if (list == null) {
            list = new ArrayList<Fragment>();
        }

        list.add(DriverMileageLastDayFragment.getInstance());
        list.add(DriverMileageLastThreeDayFragment.getInstance());
        list.add(DriverMileageLastMonthFragment.getInstance());
        if (adapter == null) {
            adapter = new DriverMainViewPagerAdapter(getSupportFragmentManager(), list);
        }

        vp.setAdapter(adapter);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idTabLineIv.getLayoutParams();

                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来 设置mTabLineIv的左边距
                 * 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));
                }
                idTabLineIv.setLayoutParams(lp);

            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(0);
    }

    private void changeState(int position) {
        if (position == LAST_DAY) {
            lastDayTv.setTextColor(0xff1f89d4);

            lastThreeDaysTv.setTextColor(0xff5b5b5b);

            lastMonthTv.setTextColor(0xff5b5b5b);
        } else if (position == LAST_THREE_DAYS) {

            lastDayTv.setTextColor(0xff5b5b5b);

            lastThreeDaysTv.setTextColor(0xff1f89d4);

            lastMonthTv.setTextColor(0xff5b5b5b);

        } else {

            lastDayTv.setTextColor(0xff5b5b5b);

            lastThreeDaysTv.setTextColor(0xff5b5b5b);

            lastMonthTv.setTextColor(0xff1f89d4);

        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idTabLineIv.getLayoutParams();
        lp.width = screenWidth / 3;
        idTabLineIv.setLayoutParams(lp);
    }
    @OnClick({R.id.last_day_rl, R.id.last_three_days_rl, R.id.last_month_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.last_day_rl:
                changeState(LAST_DAY);
                break;
            case R.id.last_three_days_rl:
                changeState(LAST_THREE_DAYS);
                break;
            case R.id.last_month_rl:
                changeState(LAST_MONTHS);
                break;
        }
    }


    @OnClick(R.id.back_llayout)
    public void onClick() {
        finish();
    }
}
