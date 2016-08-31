package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverCarMonitorMapActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetalarminfo.MyFleetAlarmInfoActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.myFleetLocation.MyFleetAddSheduing;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskCarryDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskLineFollowActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.GetCarGPSAggregate;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.TaskBean;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarControl;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.DelCarShiftRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.DelCarShiftResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetCarGPSAggregateResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetMyCarFleetResponse;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.cvnavi.logistics.i51eyun.app.widget.markview.MyMarkView;
import com.cvnavi.logistics.i51eyun.app.widget.myscrollview.MyScrollView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by Chuzy on 2016/8/8.
 * 定位信息(显示和未显示)
 */
public class MyFleetLocationInfoActivity extends BaseActivity {
    private static final int REQUEST_CODE = 0X12;
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
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.mile_tv)
    TextView mileTv;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.location_iv)
    ImageView locationIv;
    @BindView(R.id.location_info_tv)
    TextView locationInfoTv;
    @BindView(R.id.doc_iv)
    ImageView docIv;
    @BindView(R.id.doc_info_tv)
    TextView docInfoTv;
    @BindView(R.id.flag_iv)
    ImageView flagIv;
    @BindView(R.id.flag_tv)
    TextView flagTv;
    @BindView(R.id.main_iv)
    ImageView mainIv;
    @BindView(R.id.main_tv)
    TextView mainTv;
    @BindView(R.id.other_iv)
    ImageView otherIv;
    @BindView(R.id.other_tv)
    TextView otherTv;
    @BindView(R.id.empty_ll)
    LinearLayout emptyLl;
    @BindView(R.id.schedu_time_tv)
    TextView scheduTimeTv;
    @BindView(R.id.place_tv)
    TextView placeTv;
    @BindView(R.id.schedu_ll)
    LinearLayout scheduLl;
    @BindView(R.id.all_sl)
    MyScrollView allSl;
    @BindView(R.id.main_tel_tv)
    TextView mainTelTv;
    @BindView(R.id.main_rl)
    RelativeLayout mainRl;
    @BindView(R.id.other_tel_tv)
    TextView otherTelTv;
    @BindView(R.id.other_rl)
    RelativeLayout otherRl;
    @BindView(R.id.bj_ll)
    LinearLayout bjLl;
    @BindView(R.id.lc_ll)
    LinearLayout lcLl;
    @BindView(R.id.gj_ll)
    LinearLayout gjLl;
    @BindView(R.id.location_rl)
    RelativeLayout locationRl;
    @BindView(R.id.letter_oid_rl)
    RelativeLayout letterOidRl;
    @BindView(R.id.line_rl)
    RelativeLayout lineRl;
    @BindView(R.id.check_tv)
    TextView checkTv;
    @BindView(R.id.add)
    LinearLayout add;
    @BindView(R.id.custom_ll)
    LinearLayout customLl;
    @BindView(R.id.BoxCarCode_tv)
    TextView BoxCarCodeTv;
    private SweetAlertDialog loading;
    //    private String carCode = "CarCode";
//    private String carCodeKey = "EE5AF185-E5F5-4F45-BEC0-D5A96B877A07";
    private mCarControl carControlBean = new mCarControl();
    public static final String DATA_INFO = "DATA_INFO";
    private TaskBean liInfo;

    private GetMyCarFleetResponse.DataValueBean.MCarInfoListBean datInfo;


    public static void startActivity(Activity activity, GetMyCarFleetResponse.DataValueBean.MCarInfoListBean info) {
        Intent intent = new Intent(activity, MyFleetLocationInfoActivity.class);
        intent.putExtra(DATA_INFO, info);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fleet_location_info);
        ButterKnife.bind(this);
        datInfo = (GetMyCarFleetResponse.DataValueBean.MCarInfoListBean) getIntent().getSerializableExtra(DATA_INFO);
        if (datInfo != null) {
            titltTv.setText(datInfo.getCarCode());
        }

//        getInfo();
    }


    private void getInfo() {
        if (datInfo == null) {
            return;
        }
        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.show();
        GetCarGPSAggregate request = new GetCarGPSAggregate();
        request.CarCode_Key = datInfo.getCarCode_Key();
        request.Line_Oid = datInfo.getLine_Oid();
        request.Driver = datInfo.getDriver();
        request.Driver_Tel = datInfo.getDriver_Tel();
        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.DataValue = request;
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        dataRequestBase.User_Name = MyApplication.getInstance().getLoginInfo().DataValue.User_Name;
        LogUtil.d("-->> MyFleetLocationInfoActivity request =" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(LPSService.GetCarGPSAggregate_TAG, LPSService.GetCarGPSAggregate_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->> MyFleetLocationInfoActivity response =" + response.toString());
                GetCarGPSAggregateResponse resInfo = GsonUtil.newInstance().fromJson(response, GetCarGPSAggregateResponse.class);
                Message message = Message.obtain();
                if (resInfo != null && resInfo.Success) {
                    message.obj = resInfo;
                    message.what = Constants.REQUEST_SUCC;
                } else {
                    message.what = Constants.REQUEST_FAIL;
                }

                myHangler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = Message.obtain();
                msg.what = Constants.REQUEST_ERROR;
                myHangler.sendMessage(msg);

            }
        });
    }

    private Handler myHangler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loading.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    if (msg.obj != null) {
                        setData((GetCarGPSAggregateResponse) msg.obj);
                        allSl.setVisibility(View.VISIBLE);
                    } else {
                        allSl.setVisibility(View.GONE);
                        DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetLocationInfoActivity.this, Utils.getResourcesString(R.string.get_data_fail), null);
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    allSl.setVisibility(View.GONE);
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetLocationInfoActivity.this, Utils.getResourcesString(R.string.get_data_fail), null);
                    break;
                case Constants.REQUEST_ERROR:
                    allSl.setVisibility(View.GONE);
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetLocationInfoActivity.this, Utils.getResourcesString(R.string.get_data_fail), null);
                    break;
            }


        }
    };

    private void setData(GetCarGPSAggregateResponse data) {
        if (data.getDataValue().getCarMinleages() != null && data.getDataValue().getCarMinleages().getListMileage() != null) {
            mileTv.setText(data.getDataValue().getCarMinleages().getTotalMileage());
//            carCode = data.getDataValue().getCarMinleages().getCarCode();
            initChart(data.getDataValue().getCarMinleages().getListMileage());
        }
        initLocation(data);
        initDriver();
        initSchedule(data);
    }

    private void initChart(List<GetCarGPSAggregateResponse.DataValueBean.CarMinleagesBean.ListMileageBean> ListMileage) {
        lineChart.animateX(0);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDescription("日期");
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getXAxis().setGridColor(getResources().getColor(R.color.transparent));//隐藏Y轴线

        //从X轴进入的动画
        lineChart.animateX(1000);
        lineChart.animateY(1000);   //从Y轴进入的动画
        lineChart.animateXY(1000, 1000);    //从XY轴一起进入的动画
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setAxisLineColor(Color.parseColor("#ffffff"));//设置轴线颜色
        lineChart.getAxisLeft().setDrawAxisLine(false);//隐藏轴线只显示数字标签

        MyMarkView myMarkView = new MyMarkView(this, R.layout.custom_marker_view, mileTv);
        myMarkView.getXOffset(-myMarkView.getMeasuredWidth() / 2);
        myMarkView.getYOffset(-myMarkView.getMeasuredHeight());
        lineChart.setMarkerView(myMarkView);

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> yValue = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(yValue, "里程");
        if (ListMileage != null && ListMileage.size() > 0) {
            for (int i = 0; i < ListMileage.size(); i++) {
                GetCarGPSAggregateResponse.DataValueBean.CarMinleagesBean.ListMileageBean list = ListMileage.get(i);
                xValues.add(list.getSummary_Date());
                yValue.add(new Entry(Float.parseFloat(list.getSummary_Mileage()), i));
                if (i == ListMileage.size() - 1) {
                    mileTv.setText(list.getSummary_Mileage());
                }
            }
        }
        dataSet.setCircleSize(4f);
        dataSet.setLineWidth(1.75f); // 线宽
        dataSet.setDrawFilled(true);// 填充
        dataSet.setDrawCubic(true);  //设置曲线为圆滑的线
        dataSet.setValueTextSize(7f);//设置标注数据显示的大小
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(xValues, dataSets);
        lineChart.setData(lineData);

    }


    private void initLocation(GetCarGPSAggregateResponse data) {

        final GetCarGPSAggregateResponse.DataValueBean.CarGPSInfoBean infoBean = data.getDataValue().getCarGPSInfo();

        if (infoBean != null) {
            locationInfoTv.setText(infoBean.getCHS_Address());
            locationRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyFleetLocationInfoActivity.this, DriverCarMonitorMapActivity.class);
                    carControlBean.CarCodeKey = datInfo.getCarCode_Key();
                    carControlBean.CarCode = datInfo.getCarCode();
                    carControlBean.CHS_Address = datInfo.getCHS_Address();
                    carControlBean.Driver = datInfo.getDriver();
                    carControlBean.Driver_Tel = datInfo.getDriver_Tel();
                    carControlBean.BLat = datInfo.getBLat();
                    carControlBean.BLng = datInfo.getBLng();
//                    carControlBean.CarCodeKey = infoBean.getCarCode_Key();
//                    carControlBean.CarCode = infoBean.getCarCode();
//                    carControlBean.CHS_Address = infoBean.getCHS_Address();
//                    carControlBean.Driver = infoBean.getDriver();
//                    carControlBean.Driver_Tel = infoBean.getDriver_Tel();
//                    carControlBean.BLat = infoBean.getBLat();
//                    carControlBean.BLng = infoBean.getBLng();
                    if (liInfo != null && liInfo.Letter_Oid != null) {
                        carControlBean.Letter_Oid = liInfo.Letter_Oid;
                    }
                    intent.putExtra(Constants.TODRIVERMONITORMAPACTIVITY, carControlBean);
                    startActivity(intent);
                }
            });
        }

        liInfo = data.getDataValue().getLetter_List();
//        carCode = liInfo.CarCode;
        if (liInfo != null && liInfo.Letter_Oid != null) {
            docInfoTv.setText("在途中");
            letterOidRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1;
                    //跳转
                    if (!TextUtils.isEmpty(liInfo.Letter_Oid) && liInfo.Letter_Type_Oid.equals("A")) {
                        intent1 = new Intent(MyFleetLocationInfoActivity.this, MyTaskCarryDetailedActivity.class);
                    } else {
                        intent1 = new Intent(MyFleetLocationInfoActivity.this, MyTaskDetailedActivity.class);
                    }

                    intent1.putExtra(Constants.TASKINFO, liInfo);
                    startActivity(intent1);


                }
            });
        } else {
            docInfoTv.setText("空闲中");
            docInfoTv.setOnClickListener(null);
        }

        if (liInfo != null && !TextUtils.isEmpty(liInfo.Letter_Type_Oid) && liInfo.Letter_Type_Oid.equals("A")) {
            flagTv.setText(liInfo.Line_Name);
            lineRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(MyFleetLocationInfoActivity.this, MyTaskLineFollowActivity.class);
                    if (liInfo != null) {
                        intent.putExtra(Constants.TASKINFO, liInfo);
                    }
                    startActivity(intent);

                }
            });
        } else {
            lineRl.setVisibility(View.GONE);
            flagTv.setText("空闲中");
            flagTv.setOnClickListener(null);
        }


    }

    private void initDriver() {
        mainTv.setText(datInfo.getDriver());
        mainTelTv.setText(datInfo.getDriver_Tel());
    }

    private void initSchedule(GetCarGPSAggregateResponse data) {

        final GetCarGPSAggregateResponse.DataValueBean.CarCodeScheduleBean scheduleBean = data.getDataValue().getCarCode_Schedule();
        if (scheduleBean != null) {
            scheduLl.setVisibility(View.VISIBLE);
            scheduTimeTv.setText("预计" + scheduleBean.getForecast_Leave_DateTime() + "发车");
            placeTv.setText(scheduleBean.getLine_Name());


            String BoxCarCode = "";
            if (TextUtils.isEmpty(scheduleBean.getBoxCarCode())) {
                BoxCarCode = "挂车：" + "无";
            } else {
                BoxCarCode = "挂车：" + scheduleBean.getBoxCarCode();
            }
            BoxCarCodeTv.setText(BoxCarCode);
            emptyLl.setVisibility(View.GONE);
            contentLl.setVisibility(View.GONE);
            scheduLl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    DialogUtils.showMessageDialog(MyFleetLocationInfoActivity.this, "删除数据", "是否删除该条数据？", "确定", "取消", new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            if (closeType == CustomDialogListener.BUTTON_OK) {
                                loading.show();
                                GetAppLoginResponse loginInfo = MyApplication.getInstance().getLoginInfo();
                                DelCarShiftRequest datavalue = new DelCarShiftRequest();
                                datavalue.Serial_Oid = scheduleBean.getSerial_Oid();

                                DataRequestBase dataRequestBase = new DataRequestBase();
                                dataRequestBase.DataValue = datavalue;
                                dataRequestBase.User_Key = loginInfo.DataValue.User_Key;
                                dataRequestBase.UserType_Oid = loginInfo.DataValue.UserType_Oid;
                                dataRequestBase.Token = loginInfo.DataValue.Token;
                                dataRequestBase.Company_Oid = loginInfo.DataValue.Company_Oid;

                                LogUtil.d("-->> delect request= " + new Gson().toJson(dataRequestBase));
                                VolleyManager.newInstance().PostJsonRequest(TMSService.DelCarShift_TAG, TMSService.DelCarShift_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        LogUtil.d("-->> dele = " + response.toString());
                                        DelCarShiftResponse dataResponseBase = GsonUtil.newInstance().fromJson(response, DelCarShiftResponse.class);
                                        Message msg = Message.obtain();

                                        if (dataResponseBase != null) {
                                            if (dataResponseBase.Success) {
                                                msg.what = Constants.REQUEST_SUCC;
                                            } else {
                                                msg.what = Constants.REQUEST_FAIL;
                                            }
                                        } else {
                                            msg.what = Constants.REQUEST_FAIL;
                                        }
                                        deletHandler.sendMessage(msg);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Message message = Message.obtain();
                                        message.what = Constants.REQUEST_ERROR;
                                        deletHandler.sendMessage(message);
                                    }
                                });

                            }

                        }
                    });

                    return false;
                }
            });
        } else {
            LogUtil.d("-->> 345");
            scheduLl.setVisibility(View.GONE);
            emptyLl.setVisibility(View.VISIBLE);
            rightTv.setText("添加排班");
            contentLl.setVisibility(View.VISIBLE);
        }


    }

    private Handler deletHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loading.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    getInfo();
                    scheduLl.setVisibility(View.GONE);
                    emptyLl.setVisibility(View.VISIBLE);
                    contentLl.setVisibility(View.VISIBLE);
                    rightTv.setText("添加排班");
                    DialogUtils.showSuccToast(Utils.getResourcesString(R.string.dele_succ));
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.request_Fill));
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.request_error));
                    break;
            }

        }
    };


    @OnClick({R.id.bj_ll, R.id.lc_ll, R.id.gj_ll, R.id.content_ll, R.id.back_llayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bj_ll:
                //报警
                MyFleetAlarmInfoActivity.start(this, datInfo.getCarCode_Key(), datInfo.getCarCode());
                break;
            case R.id.lc_ll:
                //里程统计
                MyFleetMileStatisticActivity.startActivity(MyFleetLocationInfoActivity.this, datInfo.getCarCode_Key(), datInfo.getCarCode());
                break;
            case R.id.gj_ll:
                //轨迹
                MyFleetRecordActivity.start(this, datInfo.getCarCode_Key());
                break;
            case R.id.content_ll:
                //添加排班
                MyFleetAddSheduing.startActivity(MyFleetLocationInfoActivity.this, datInfo.getCarCode(), datInfo.getCarCode_Key(), REQUEST_CODE);
                break;
            case R.id.back_llayout:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }
}
