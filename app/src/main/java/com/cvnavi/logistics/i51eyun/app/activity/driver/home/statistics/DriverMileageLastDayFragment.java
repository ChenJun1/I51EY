package com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverCarTreeListActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarInfo;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetMileagesRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.location.LocationChooseCarCallback;
import com.cvnavi.logistics.i51eyun.app.callback.manager.LocationChooseCarCallBackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.DateTimeTwoPickDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/12.
 */
public class DriverMileageLastDayFragment extends BaseFragment implements LocationChooseCarCallback {

    @BindView(R.id.CarStartTime_et)
    EditText CarStartTimeEt;
    @BindView(R.id.CarEndTime_et)
    EditText CarEndTimeEt;
    @BindView(R.id.ChooseCar_et)
    EditText ChooseCarEt;
    @BindView(R.id.query_btn)
    Button queryBtn;
    private String carCodeKey;

    public static DriverMileageLastDayFragment getInstance() {
        return new DriverMileageLastDayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationChooseCarCallBackManager.newStance().add(this);
    }
    @Override
    public void onDestroy() {
        LocationChooseCarCallBackManager.newStance().remove(this);
        super.onDestroy();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mileage_statics_last_day, container, false);


        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private void initView() {

        CarStartTimeEt.setText(DateUtil.getBeforeOneDayTime());
        CarEndTimeEt.setText(DateUtil.getCurDateStr(DateUtil.FORMAT_YMDHM));
    }

    @OnClick({R.id.CarStartTime_et, R.id.CarEndTime_et, R.id.ChooseCar_et, R.id.query_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CarStartTime_et:
                DateTimeTwoPickDialog dateTimeTwoPickDialog = new DateTimeTwoPickDialog(CarStartTimeEt.getText().toString());
                dateTimeTwoPickDialog.dateTimePicKDialog(getActivity(), CarStartTimeEt, null);
                break;
            case R.id.CarEndTime_et:
                dateTimeTwoPickDialog = new DateTimeTwoPickDialog(CarEndTimeEt.getText().toString());
                dateTimeTwoPickDialog.dateTimePicKDialog(getActivity(), CarEndTimeEt, null);
                break;
            case R.id.ChooseCar_et:
                Intent intent = new Intent(getActivity(), DriverCarTreeListActivity.class);
                startActivity(intent);
                break;
            case R.id.query_btn:
                verification();

                break;
        }
    }


    private void verification() {
        if (TextUtils.isEmpty(CarStartTimeEt.getText())) {
            DialogUtils.showNormalToast("请选择开始时间！");
            return;
        }
        if (TextUtils.isEmpty(CarEndTimeEt.getText())) {
            DialogUtils.showNormalToast("请选择结束时间！");
            return;
        }

        if (DateUtil.compareDate(CarStartTimeEt.getText().toString(), CarEndTimeEt.getText().toString())) {

            DialogUtils.showNormalToast("开始时间不能大于结束时间");

            return;
        }

        if (DateUtil.compareDateNDays(CarStartTimeEt.getText().toString(), CarEndTimeEt.getText().toString(), DateUtil.FORMAT_YMDHM, 90) == 2) {
            DialogUtils.showNormalToast("只能查询最近90天的数据");
            return;
        }

        if (TextUtils.isEmpty(ChooseCarEt.getText())) {
            DialogUtils.showNormalToast("请选择车辆！");
            return;
        }
        DriverMileageSearchDeatilActivity.startActivity(getActivity(), CarStartTimeEt.getText().toString(), CarEndTimeEt.getText().toString(), carCodeKey);
    }

    @Override
    public void OnMonitorLoadCarCode(mCarInfo mCarInfo) {
        if (mCarInfo != null) {
            ChooseCarEt.setText(mCarInfo.CarCode);
            carCodeKey = mCarInfo.CarCode_Key;
        }
    }

    @Override
    public void OnHistorLoadCarCode(mCarInfo mCarInfo) {
        if (mCarInfo != null) {
            ChooseCarEt.setText(mCarInfo.CarCode);
            carCodeKey = mCarInfo.CarCode_Key;
        }
    }
}
