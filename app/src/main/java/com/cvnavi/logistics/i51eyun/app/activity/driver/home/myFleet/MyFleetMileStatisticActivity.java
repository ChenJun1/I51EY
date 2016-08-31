package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.myFleet.MyFleetMileStaticstAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.SearchActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverCarSchedulingSearchActivity;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetMileagesRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetMileageResponse;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by Chuzy on 2016/8/8.
 * 我的车队的里程统计
 */
public class MyFleetMileStatisticActivity extends BaseActivity {
    public static final String Intent_INFO_CARCODE_KEY = "Intent_INFO_CARCODE_KEY";
    public static final String Intent_INFO_CARCODE = "Intent_INFO_CARCODE";
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.titlt_tv)
    TextView titltTv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.add_ll)
    LinearLayout addLl;
    @BindView(R.id.search_iv)
    ImageView searchIv;
    @BindView(R.id.search_ll)
    LinearLayout searchLl;
    @BindView(R.id.right_ll)
    LinearLayout rightLl;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.list)
    PullToRefreshListView list;
    @BindView(R.id.start_hm_tv)
    TextView startHmTv;
    @BindView(R.id.start_md_tv)
    TextView startMdTv;
    @BindView(R.id.end_hm_tv)
    TextView endHmTv;
    @BindView(R.id.end_md_tv)
    TextView endMdTv;
    @BindView(R.id.total_tv)
    TextView totalTv;
    @BindView(R.id.gj_ll)
    LinearLayout gjLl;
    private String startTime;
    private String endTime;
    private String carCodeKey;
    private String carCode;

    private MyFleetMileStaticstAdapter adapter;
    private SweetAlertDialog loading;
    private Context context;

    public static void startActivity(Activity activity, String carCode_Key, String carCode) {
        Intent intent = new Intent(activity, MyFleetMileStatisticActivity.class);
        intent.putExtra(Intent_INFO_CARCODE_KEY, carCode_Key);
        intent.putExtra(Intent_INFO_CARCODE, carCode);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fleet_mile_statics);
        ButterKnife.bind(this);
        rightLl.setVisibility(View.VISIBLE);
        addLl.setVisibility(View.INVISIBLE);
        searchLl.setVisibility(View.VISIBLE);
        context = this;
        startTime = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD) + " 00:00";
        endTime = DateUtil.getCurDateStr(DateUtil.FORMAT_YMDHM);
        setTime(startTime, endTime);
        carCodeKey = getIntent().getStringExtra(Intent_INFO_CARCODE_KEY);
        carCode = getIntent().getStringExtra(Intent_INFO_CARCODE);
        titltTv.setText(carCode);
    }

    /**
     * 设置时间
     *
     * @param startTime
     * @param endTime
     */
    private void setTime(String startTime, String endTime) {
        startMdTv.setText(DateUtil.strOldFormat2NewFormat(startTime, DateUtil.FORMAT_YMDHM, DateUtil.FORMAT_MD));
        startHmTv.setText(DateUtil.strOldFormat2NewFormat(startTime, DateUtil.FORMAT_YMDHM, DateUtil.FORMAT_HM));
        endMdTv.setText(DateUtil.strOldFormat2NewFormat(endTime, DateUtil.FORMAT_YMDHM, DateUtil.FORMAT_MD));
        endHmTv.setText(DateUtil.strOldFormat2NewFormat(endTime, DateUtil.FORMAT_YMDHM, DateUtil.FORMAT_HM));
    }


    private void getListInfo(String starTime, String endTime, String carCode_Key) {
        if (TextUtils.isEmpty(carCodeKey)) {
            return;
        }
        if (TextUtils.isEmpty(starTime) || TextUtils.isEmpty(endTime)) {
            return;
        }
        loading = new SweetAlertDialog(MyFleetMileStatisticActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        loading.show();
        GetAppLoginResponse loginInfo = MyApplication.getInstance().getLoginInfo();
        if (loginInfo != null) {
            GetMileagesRequest dataValue = new GetMileagesRequest();
            dataValue.CarCode_Key = carCode_Key;
            dataValue.StarTime = starTime;
            dataValue.EndTime = endTime;
            DataRequestBase dataRequestBase = new DataRequestBase();
            dataRequestBase.DataValue = dataValue;
            dataRequestBase.User_Key = loginInfo.DataValue.User_Key;
            dataRequestBase.UserType_Oid = loginInfo.DataValue.UserType_Oid;
            dataRequestBase.Token = loginInfo.DataValue.Token;
            dataRequestBase.Company_Oid = loginInfo.DataValue.Company_Oid;

            LogUtil.d("-->> request = " + new Gson().toJson(dataRequestBase));

            VolleyManager.newInstance().PostJsonRequest(LPSService.GetMileages_TAG, LPSService.GetMileages_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    LogUtil.d("-->> response = " + response.toString());
                    GetMileageResponse info = GsonUtil.newInstance().fromJson(response, GetMileageResponse.class);
                    Message msg = Message.obtain();

                    if (info != null) {
                        if (info.isSuccess()) {
                            msg.what = Constants.REQUEST_SUCC;
                            msg.obj = info;
                        } else {
                            msg.what = Constants.REQUEST_FAIL;
                        }

                    } else {
                        msg.what = Constants.REQUEST_FAIL;
                    }
                    handler.sendMessage(msg);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message msg = Message.obtain();
                    msg.what = Constants.REQUEST_ERROR;
                    handler.sendMessage(msg);

                }
            });


        }

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loading.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    GetMileageResponse info = (GetMileageResponse) msg.obj;
                    if (info != null) {
                        if (info.getDataValue().getListMileage().size() == 0) {
                            DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetMileStatisticActivity.this, Utils.getResourcesString(R.string.mile_list_null), new CustomDialogListener() {
                                @Override
                                public void onDialogClosed(int closeType) {
                                    if (closeType == CustomDialogListener.BUTTON_OK) {
//                                        MyFleetMileStatisticActivity.this.finish();
                                    }
                                }
                            });
                        } else {
                            totalTv.setText(info.getDataValue().getTotalMileage() + "公里");
                            adapter = new MyFleetMileStaticstAdapter(info.getDataValue().getListMileage(), context);
                            list.setAdapter(adapter);
                            setTime(startTime, endTime);
                        }
                    } else {
                        DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetMileStatisticActivity.this, Utils.getResourcesString(R.string.get_data_fail), new CustomDialogListener() {
                            @Override
                            public void onDialogClosed(int closeType) {
                                if (closeType == CustomDialogListener.BUTTON_OK) {
//                                    MyFleetMileStatisticActivity.this.finish();
                                }

                            }
                        });
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetMileStatisticActivity.this, Utils.getResourcesString(R.string.mile_list_error), new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            if (closeType == CustomDialogListener.BUTTON_OK) {
//                                MyFleetMileStatisticActivity.this.finish();
                            }

                        }
                    });
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetMileStatisticActivity.this, Utils.getResourcesString(R.string.request_error), new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            if (closeType == CustomDialogListener.BUTTON_OK) {
//                                MyFleetMileStatisticActivity.this.finish();
                            }

                        }
                    });
                    break;
            }


        }


    };

    @Override
    protected void onResume() {
        super.onResume();
        getListInfo(startTime, endTime, carCodeKey);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SearchActivity.REQUEST_CODE) {
            startTime = data.getStringExtra(DriverCarSchedulingSearchActivity.BEGIN_DATA);
            endTime = data.getStringExtra(DriverCarSchedulingSearchActivity.END_DATA);
        } else {
            startTime = null;
            endTime = null;
        }
    }

    @OnClick({R.id.back_llayout, R.id.search_ll, R.id.gj_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.search_ll:
                SearchActivity.startActivity(MyFleetMileStatisticActivity.this, SearchActivity.REQUEST_CODE, DateUtil.FORMAT_YMDHM, 30);
                break;
            case R.id.gj_ll:
                //轨迹
                MyFleetRecordActivity.start(this, carCodeKey);
                break;
        }
    }
}