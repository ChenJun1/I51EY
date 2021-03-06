package com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.callback.manager.DriverChioceTimeCallbackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.DateTimePickDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/7.
 */
public class DriverCarSchedulingSearchActivity extends BaseActivity {

    public static final String BEGIN_DATA = "BEGIN_DATA";
    public static final String END_DATA = "END_DATA";
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.titlt_tv)
    TextView titltTv;
    @BindView(R.id.begin_et)
    TextView beginEt;
    @BindView(R.id.end_et)
    TextView endEt;
    @BindView(R.id.search_btn)
    Button searchBtn;

    private int tag;
    private DateTimePickDialog dateTimePicKDialog;
    private DateTimePickDialog dateTimePicKDialog2;

    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, DriverCarSchedulingSearchActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_schedulig_search);
        ButterKnife.bind(this);

        titltTv.setText("搜索");
//        beginEt.setInputType(InputType.TYPE_NULL);
//        endEt.setInputType(InputType.TYPE_NULL);
        beginEt.setText(DateUtil.getBeforeDayNsTime(2,DateUtil.FORMAT_YMD));
        endEt.setText(DateUtil.getNowTime(DateUtil.FORMAT_YMD));

        if (getIntent().getIntExtra(Constants.MyTASK_CHOICE_TIME, 10) != 10) {
            tag = getIntent().getIntExtra(Constants.MyTASK_CHOICE_TIME, 10);
        }
    }


    @OnClick({R.id.begin_et, R.id.end_et, R.id.back_llayout, R.id.search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.begin_et:
                showDia(beginEt);
                break;
            case R.id.end_et:
//                showDia(endEt);
                showDiaEnd(endEt);
                break;
            case R.id.back_llayout:
                finish();
                break;
            case R.id.search_btn:

                if (TextUtils.isEmpty(beginEt.getText().toString()) || TextUtils.isEmpty(endEt.getText().toString())) {
                    DialogUtils.showWarningToast(Utils.getResourcesString(R.string.search_data_not_allow_null));
                } else {
                    if ((!DateUtil.compareData(beginEt.getText().toString(), endEt.getText().toString(), DateUtil.FORMAT_YMD)) && Integer.parseInt(DateUtil.calcDiffDays(beginEt.getText().toString(), endEt.getText().toString())) <= 30) {
                        //只有开始日期小于结束日期并且在30范围内可以查询
                        Intent intent = new Intent();
                        intent.putExtra(BEGIN_DATA, beginEt.getText().toString());
                        intent.putExtra(END_DATA, endEt.getText().toString());
                        setResult(RESULT_OK, intent);
                        DriverChioceTimeCallbackManager.newInstance().fire(beginEt.getText().toString(), endEt.getText().toString(), tag);
                        finish();
                    } else {
                        if (Integer.parseInt(DateUtil.calcDiffDays(beginEt.getText().toString(), endEt.getText().toString())) > 30) {
                            DialogUtils.showMessageDialogOfDefaultSingleBtn(this, Utils.getResourcesString(R.string.car_scheduling_search_more_than_30));
                        } else {
                            DialogUtils.showMessageDialogOfDefaultSingleBtn(this, Utils.getResourcesString(R.string.car_scheduling_search_star_more_than_end));
                        }
                    }
                }
                LogUtil.d("-->> begin =" + beginEt.getText().toString() + "||end = " + endEt.getText().toString() + "||isBig = " + DateUtil.compareData(beginEt.getText().toString(), endEt.getText().toString(), DateUtil.FORMAT_YMD));


                break;
        }
    }

    private void showDia(TextView editText) {
        if (dateTimePicKDialog == null) {
            dateTimePicKDialog = new DateTimePickDialog();
        }
        dateTimePicKDialog.dateTimePicKDialog(this, editText, null);
    }


    private void showDiaEnd(TextView editText) {
        if (dateTimePicKDialog2 == null) {
            dateTimePicKDialog2 = new DateTimePickDialog();
        }
        dateTimePicKDialog2.dateTimePicKDialog(this, editText, null);
    }

}
