package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.utils.ContextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${ChenJ} on 2016/8/8.
 */
public class MyFleetCarStatusExplainActivity extends BaseActivity {
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.operation_btn)
    Button operationBtn;
    @BindView(R.id.operation_llayout)
    LinearLayout operationLlayout;
    @BindView(R.id.contact_us)
    TextView contactUs;
    @BindView(R.id.i_know_btn)
    Button iKnowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_explain);
        ButterKnife.bind(this);
        titleTv.setText("车辆状态说明");
    }

    @OnClick({R.id.back_llayout, R.id.contact_us, R.id.i_know_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.contact_us:
                ContextUtil.callAlertDialog("", MyFleetCarStatusExplainActivity.this);
                break;
            case R.id.i_know_btn:
                finish();
                break;
        }
    }
}
