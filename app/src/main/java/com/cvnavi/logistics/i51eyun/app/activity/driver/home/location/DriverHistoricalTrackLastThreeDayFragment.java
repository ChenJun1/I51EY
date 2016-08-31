package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarInfo;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.location.LocationChooseCarCallback;
import com.cvnavi.logistics.i51eyun.app.callback.manager.LocationChooseCarCallBackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.DateTimeTwoPickDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fan on 2016/7/8.
 */
public class DriverHistoricalTrackLastThreeDayFragment extends BaseFragment implements LocationChooseCarCallback {
    @BindView(R.id.CarStartTime_et)
    EditText CarStartTimeEt;
    @BindView(R.id.CarEndTime_et)
    EditText CarEndTimeEt;
    @BindView(R.id.ChooseCar_et)
    EditText ChooseCarEt;
    @BindView(R.id.query_btn)
    TextView queryBtn;

    private String carCodeKey;
    private String carCode;


    public static DriverHistoricalTrackLastThreeDayFragment getInstance() {
        return new DriverHistoricalTrackLastThreeDayFragment();
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationChooseCarCallBackManager.newStance().add(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_track_last_day, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;

    }

    private  void initView() {

        CarStartTimeEt.setText(DateUtil.getBeforeThreeDayTime());
        CarEndTimeEt.setText(DateUtil.getCurDateStr(DateUtil.FORMAT_YMDHM));

    }

    @OnClick({R.id.CarStartTime_et, R.id.CarEndTime_et, R.id.ChooseCar_et,R.id.query_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CarStartTime_et:
                DateTimeTwoPickDialog dateTimeTwoPickDialog = new DateTimeTwoPickDialog(CarStartTimeEt.getText().toString());
                dateTimeTwoPickDialog.dateTimePicKDialog(getActivity(),CarStartTimeEt,null);
                break;
            case R.id.CarEndTime_et:
                dateTimeTwoPickDialog= new DateTimeTwoPickDialog(CarEndTimeEt.getText().toString());
                dateTimeTwoPickDialog.dateTimePicKDialog(getActivity(),CarEndTimeEt,null);
                break;
            case R.id.ChooseCar_et:
                Intent intent = new Intent(getActivity(), DriverCarTreeListActivity.class);
                intent.putExtra(Constants.CHOOSE_CAR, Constants.CHOOSE_HISTOR_CAR);
                startActivity(intent);
                break;

            case R.id.query_btn:

                if (TextUtils.isEmpty(CarStartTimeEt.getText())) {
                    DialogUtils.showNormalToast("请选择开始时间！");
                    return;
                }
                if (TextUtils.isEmpty(CarEndTimeEt.getText())) {
                    DialogUtils.showNormalToast("请选择结束时间！");
                    return;
                }

                if (DateUtil.compareDate(CarStartTimeEt.getText().toString(),CarEndTimeEt.getText().toString())){

                    DialogUtils.showNormalToast("开始时间不能大于结束时间");

                    return;
                }

                if (DateUtil.compareDateThreeDay(CarStartTimeEt.getText().toString(), CarEndTimeEt.getText().toString()) == 2) {
                    DialogUtils.showNormalToast("最多查询3天数据！");
                    return;
                }

                if (TextUtils.isEmpty(ChooseCarEt.getText())) {
                    DialogUtils.showNormalToast("请选择车辆！");
                    return;
                }

                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), DriverRecordMainActivity.class);
                intent2.putExtra("startTime",CarStartTimeEt.getText().toString());
                intent2.putExtra("endTime",CarEndTimeEt.getText().toString());
                intent2.putExtra("carCodeKey",carCodeKey);
                intent2.putExtra("carCode", ChooseCarEt.getText().toString());
                intent2.putExtra(Constants.CAR_KEY,"F");
                startActivity(intent2);

                break;
        }
    }

    @Override
    public void OnMonitorLoadCarCode(mCarInfo mCarInfo) {
        if (mCarInfo != null) {
            ChooseCarEt.setText(mCarInfo.CarCode);
            carCodeKey=mCarInfo.CarCode_Key;
        }
    }

    @Override
    public void OnHistorLoadCarCode(mCarInfo mCarInfo) {
        if (mCarInfo != null) {
            ChooseCarEt.setText(mCarInfo.CarCode);
            carCodeKey=mCarInfo.CarCode_Key;
        }
    }

    @Override
    public void onDestroy() {
        LocationChooseCarCallBackManager.newStance().remove(this);
        super.onDestroy();
    }
}
