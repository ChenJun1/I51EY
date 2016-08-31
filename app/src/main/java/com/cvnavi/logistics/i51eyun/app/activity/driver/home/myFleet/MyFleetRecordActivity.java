package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.DriverMainViewPagerAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.SearchActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fragment.MyFleetLocusFragment;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fragment.MyFleetRecordFragment;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocusAnalysis;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by Chuzy on 2016/8/8.
 * 记录分析
 */
public class MyFleetRecordActivity extends BaseActivity {
    private static final int RECORD = 0;
    private static final int MAP = 1;
    private static final String KEY_CARKEY = "CARKEY";
    private final int Reques_Code = 89;
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.record_tv)
    TextView recordTv;
    @BindView(R.id.map_tv)
    TextView mapTv;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.search)
    LinearLayout search;
    private DriverMainViewPagerAdapter adapter;
    private ArrayList<Fragment> list = new ArrayList<Fragment>();
    private List<mCarHistoryLocusAnalysis> locusList;
    private String startTime = "";
    private String endTime = "";
    private String carKey = "";


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCarKey() {
        return carKey;
    }

    public void setCarKey(String carKey) {
        this.carKey = carKey;
    }

    public List<mCarHistoryLocusAnalysis> getLocusList() {
        return locusList;
    }

    public void setLocusList(List<mCarHistoryLocusAnalysis> locusList) {
        this.locusList = locusList;
    }

    public static void start(Context context, String Carkey) {
        Intent starter = new Intent(context, MyFleetRecordActivity.class);
        starter.putExtra(KEY_CARKEY, Carkey);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fleet_record);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra(KEY_CARKEY) != null) {
            setCarKey(getIntent().getStringExtra(KEY_CARKEY));
        } else {
            DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(this, Utils.getResourcesString(R.string.get_car_info_fill), new CustomDialogListener() {
                @Override
                public void onDialogClosed(int closeType) {
                    finish();
                }
            });
        }
        init();
    }

    private void init() {
        if (list == null) {
            list = new ArrayList<Fragment>();
        }

        list.add(MyFleetRecordFragment.getInstance());
        list.add(MyFleetLocusFragment.getInstance());
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
        if (position == RECORD) {
            recordTv.setTextColor(0xff3A95E7);
            recordTv.setBackgroundResource(R.drawable.map_unselect);
            mapTv.setTextColor(0xffffffff);
            mapTv.setBackgroundResource(R.drawable.record_select);
            recordTv.setText("记录分析");
            mapTv.setText("轨迹地图");
        } else {
            recordTv.setTextColor(0xffffffff);
            recordTv.setBackgroundResource(R.drawable.map_select);
            mapTv.setTextColor(0xff3A95E7);
            mapTv.setBackgroundResource(R.drawable.record_unselect);
            recordTv.setText("记录分析");
            mapTv.setText("轨迹地图");
        }

        vp.setCurrentItem(position);
    }


    @OnClick({R.id.record_tv, R.id.map_tv, R.id.back_llayout, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_tv:
                changeState(RECORD);
                break;
            case R.id.map_tv:
                changeState(MAP);
                break;
            case R.id.back_llayout:
                finish();
                break;
            case R.id.search:
//                Intent intent=new Intent(this, SearchActivity.class);
                SearchActivity.startActivity(this, Reques_Code, DateUtil.FORMAT_YMDHM, 3);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Reques_Code:
                    Bundle bundle = data.getExtras();
                    startTime = bundle.getString(SearchActivity.BEGIN_DATA);
                    endTime = bundle.getString(SearchActivity.END_DATA);
                    startTime = startTime + ":00";
                    endTime = endTime + ":00";
                    vp.setCurrentItem(0);
                    break;
            }
        }
    }
}
