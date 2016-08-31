package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location.DriverCarLineAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.LineFollowBean;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.Model_LetterTrace_Node;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarControl;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarInfo;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetSingleCarMonitorRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetGPSInfoResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetTaskLineFollowResponse;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.location.LocationChooseCarCallback;
import com.cvnavi.logistics.i51eyun.app.callback.manager.LocationChooseCarCallBackManager;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * Created by Cjun on 2016/7/6.
 */
public class DriverSingleCarMonitorActivity extends BaseActivity implements LocationChooseCarCallback,DriverCarLineAdapter.TaskLineLookPicListener {

    private final String TAG = DriverSingleCarMonitorActivity.class.getName();
    private final int loadList =2000;

    @BindView(R.id.back_llayout)
    LinearLayout backLinearLayout;
    @BindView(R.id.title_tv)
    TextView titltTextView;
    @BindView(R.id.CarCode_tv)
    TextView carNumTv;
    @BindView(R.id.car_state_tv)
    TextView carStateTv;
    @BindView(R.id.history_locus_btn)
    Button historyLocusBtn;
    @BindView(R.id.address_detail_tv)
    TextView addressDetailTv;
    @BindView(R.id.cur_task_tv)
    TextView curTaskTv;
    @BindView(R.id.cur_driver_tv)
    TextView curDriverTv;
    @BindView(R.id.ReceiveMan_Tel_tv)
    TextView phoneNumTv;
    @BindView(R.id.cur_speed_tv)
    TextView curSpeedTv;
    @BindView(R.id.mileage_tv)
    TextView mileageTv;
    @BindView(R.id.task_monitor_lv)
    ListView taskMonitorLv;
    @BindView(R.id.inquiry_car_et)
    EditText inquiryCarEt;
    @BindView(R.id.this_linearLayout)
    LinearLayout thisLinearLayout;
    @BindView(R.id.operation_btn)
    Button operationBtn;


    private DriverCarLineAdapter driverCarLineAdapter;

    private mCarControl carControlBean;

    private mCarInfo carInfoBean;
//    private List<mCarLineTrend> carLineTrends;
    private List<Model_LetterTrace_Node> list;
    private SweetAlertDialog lodingDialog;
    private  LineFollowBean LineFollowbean=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_car_monitor);
        ButterKnife.bind(this);
        LocationChooseCarCallBackManager.newStance().add(this);
        init();
    }

    private void init() {
        titltTextView.setText(R.string.car_control);
        operationBtn.setVisibility(View.VISIBLE);
        lodingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        operationBtn.setText(R.string.map);
        operationBtn.setVisibility(View.GONE);
        list = new ArrayList<>();
        driverCarLineAdapter = new DriverCarLineAdapter(list, this,this);
        taskMonitorLv.setAdapter(driverCarLineAdapter);
    }

    @OnClick({R.id.back_llayout, R.id.history_locus_btn, R.id.inquiry_car_et, R.id.operation_btn, R.id.ReceiveMan_Tel_tv})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_llayout:
                this.finish();
                break;
            case R.id.inquiry_car_et:
                showActivity(this, DriverCarTreeListActivity.class);
                break;
            case R.id.operation_btn:
                if (carControlBean == null) {

                    DialogUtils.showNormalToast(Utils.getResourcesString(R.string.toast_tis));
                    return;
                }
                intent = new Intent(this, DriverCarMonitorMapActivity.class);
                carControlBean.CarCodeKey = carInfoBean.CarCode_Key;
                if(LineFollowbean!=null) {
                    carControlBean.Letter_Oid = LineFollowbean.Letter_Oid;
                }
                intent.putExtra(Constants.TODRIVERMONITORMAPACTIVITY, carControlBean);
                startActivity(intent);
                break;
            case R.id.history_locus_btn:
                intent = new Intent(this, DriverRecordMainActivity.class);
                intent.putExtra(Constants.CAR_KEY, carInfoBean.CarCode_Key);
                startActivity(intent);
                break;
            case R.id.ReceiveMan_Tel_tv:
                ContextUtil.callAlertDialog(phoneNumTv.getText().toString(), DriverSingleCarMonitorActivity.this);
                break;
            default:
                break;
        }
    }

    private void requestData(final String URL) {
        lodingDialog.show();
        DataRequestBase requestBase = new DataRequestBase();
        GetSingleCarMonitorRequest singleCarMonitorRequest = new GetSingleCarMonitorRequest();
        singleCarMonitorRequest.CarCode_Key = carInfoBean.CarCode_Key;
        singleCarMonitorRequest.Driver = carInfoBean.Driver;
        singleCarMonitorRequest.Driver_Tel = carInfoBean.Driver_Tel;
        singleCarMonitorRequest.Line_Oid = carInfoBean.Line_Oid;
        requestBase.DataValue = singleCarMonitorRequest;
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);

        VolleyManager.newInstance().PostJsonRequest(TAG, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.v(TAG, "-->>post json对象: " + jsonObject.toString());
                        GetGPSInfoResponse getGPSInfoResponse = JsonUtils.parseData(jsonObject.toString(), GetGPSInfoResponse.class);
                        Message msg = Message.obtain();
                        if (getGPSInfoResponse.Success) {
                            msg.what = Constants.REQUEST_SUCC;
                            msg.obj = getGPSInfoResponse.DataValue;
                        } else {
                            msg.obj = getGPSInfoResponse.ErrorText;
                            msg.what = Constants.REQUEST_FAIL;
                        }
                        mHandler.sendMessage(msg);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "-->>" + error.getMessage(), error);
                        Message msg = Message.obtain();
                        msg.what = Constants.REQUEST_ERROR;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void requestLoadLinNote(final String URL) {
        lodingDialog.show();
        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        dataRequestBase.Org_Name = MyApplication.getInstance().getLoginInfo().DataValue.Org_Name;
        dataRequestBase.DataValue = carInfoBean; //JsonUtils.toJsonData(getDriverListRequest);
        LogUtil.d("-->>" + dataRequestBase.toString());

        VolleyManager.newInstance().PostJsonRequest(TAG, URL, GsonUtil.newInstance().toJson(dataRequestBase),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.v(TAG, "-->>post json对象: " + jsonObject.toString());
                        GetTaskLineFollowResponse response1 = JsonUtils.parseData(jsonObject.toString(), GetTaskLineFollowResponse.class);
                        Message msg = Message.obtain();
                        if (response1.Success) {
                            msg.obj = response1.DataValue;
                            msg.what = loadList;
                            mHandler.sendMessage(msg);
                        } else {
                            msg.obj = response1.ErrorText;
                            msg.what = Constants.REQUEST_FAIL;
                            mHandler.sendMessage(msg);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "-->>" + error.getMessage(), error);
                        Message msg = Message.obtain();
                        msg.what = Constants.REQUEST_ERROR;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(lodingDialog!=null){
                lodingDialog.dismiss();
            }
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    carControlBean = (mCarControl) msg.obj;
                    if (carControlBean != null) {
                        setData();
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverSingleCarMonitorActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
                case loadList:
                   LineFollowbean= (LineFollowBean) msg.obj;
                    if(LineFollowbean!=null) {
                        SetViewValueUtil.setTextViewValue(curTaskTv, LineFollowbean.Letter_Oid);
                        if (LineFollowbean.Line_Nodes != null) {
                            list.clear();
                            list.addAll(LineFollowbean.Line_Nodes);
                        }
                    }
                    driverCarLineAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }
    };



    private void setData() {

        thisLinearLayout.setVisibility(View.VISIBLE);

//        list.clear();
//        if(carControlBean.CarLineList!=null){
//            list.addAll(carControlBean.CarLineList);
//        }
//        driverCarLineAdapter.notifyDataSetChanged();


        SetViewValueUtil.setTextViewValue(carNumTv, carControlBean.CarCode);
        if (!TextUtils.isEmpty(carInfoBean.Device_Status_Oid) && carInfoBean.Device_Status_Oid.equals("0")) {
            carStateTv.setTextColor(Utils.getResourcesColor(R.color.text_grayness));
            SetViewValueUtil.setTextViewValue(carStateTv, Utils.getResourcesString(R.string.off_line));

        } else if (!TextUtils.isEmpty(carInfoBean.Device_Status_Oid) && carInfoBean.Device_Status_Oid.equals("1")) {
            SetViewValueUtil.setTextViewValue(carStateTv, Utils.getResourcesString(R.string.on_line));
            carStateTv.setTextColor(Utils.getResourcesColor(R.color.color_FC9B27));
        }
        SetViewValueUtil.setTextViewValue(addressDetailTv, carControlBean.CHS_Address);
//        SetViewValueUtil.setTextViewValue(curTaskTv, carControlBean.Serial_Oid);
        SetViewValueUtil.setTextViewValue(curDriverTv, carControlBean.Driver);
        SetViewValueUtil.setTextViewValue(phoneNumTv, carControlBean.Driver_Tel);
        SetViewValueUtil.setTextViewValue(curSpeedTv, carControlBean.Speed, "km/h");

        SetViewValueUtil.setTextViewValue(mileageTv, ContextUtil.getDouble(carControlBean.Mileage));

    }

    @Override
    public void OnMonitorLoadCarCode(mCarInfo mCarInfo) {
        if (mCarInfo != null) {
            carInfoBean = mCarInfo;
            if (carInfoBean == null) {
                operationBtn.setVisibility(View.GONE);
            } else {
                operationBtn.setVisibility(View.VISIBLE);
            }

            requestData(LPSService.GetGPSInfo_Request_Url);
            requestLoadLinNote(TMSService.GetCarLineNode_Request_Url);
        }
    }

    @Override
    public void OnHistorLoadCarCode(mCarInfo mCarInfo) {

    }

    @Override
    public void onLookPic(Model_LetterTrace_Node node) {

    }
}
