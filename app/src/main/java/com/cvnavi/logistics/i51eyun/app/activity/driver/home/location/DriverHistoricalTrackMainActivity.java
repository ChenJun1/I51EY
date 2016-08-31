package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
 * Created by ${chuzy} on 2016/7/1.
 */
public class DriverHistoricalTrackMainActivity extends BaseActivity {

    private static final int LAST_DAY = 0;
    private static final int LAST_THREE_DAY = 1;
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
    @BindView(R.id.last_day_view)
    View lastDayView;
    @BindView(R.id.last_day_rl)
    RelativeLayout lastDayRl;
    @BindView(R.id.last_three_day_tv)
    TextView lastThreeDayTv;
    @BindView(R.id.last_three_day_view)
    View lastThreeDayView;
    @BindView(R.id.last_three_day_rl)
    RelativeLayout lastThreeDayRl;
    @BindView(R.id.vp)
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_historical_track_main);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        titltTv.setText("历史轨迹");
        lastDayTv.setTextColor(0xff1f89d4);
        if (list == null) {
            list = new ArrayList<Fragment>();
        }

        list.add(DriverHistoricalTrackLastDayFragment.getInstance());
        list.add(DriverHistoricalTrackLastThreeDayFragment.getInstance());
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
        if (position == LAST_DAY) {
            lastDayTv.setTextColor(0xff1f89d4);
            lastDayView.setVisibility(View.VISIBLE);
            lastThreeDayTv.setTextColor(0xff5b5b5b);
            lastThreeDayView.setVisibility(View.INVISIBLE);

        } else {
            lastDayTv.setTextColor(0xff5b5b5b);
            lastDayView.setVisibility(View.INVISIBLE);
            lastThreeDayTv.setTextColor(0xff1f89d4);
            lastThreeDayView.setVisibility(View.VISIBLE);

        }
        vp.setCurrentItem(position);

    }


    @OnClick({R.id.last_day_rl, R.id.last_three_day_rl,R.id.back_llayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;

            case R.id.last_day_rl:
                changeState(LAST_DAY);
                break;
            case R.id.last_three_day_rl:
                changeState(LAST_THREE_DAY);
                break;
        }
    }
}
