package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.myFleet.MyFleetAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetmonitor.MyFleetMonitorMapActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverAddCarSchedulingActivity;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetMyCarFleetResponse;
import com.cvnavi.logistics.i51eyun.app.callback.MyOnClickItemListener;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.order.MyOrderListener;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.popupwindow.OrderDetailPopWindow;
import com.cvnavi.logistics.i51eyun.app.widget.listview.MyListView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/8/3.
 * 我的车队
 */
public class MyFleetActivity extends BaseActivity implements MyOnClickItemListener, MyOrderListener {

    @BindView(R.id.zaitu_num_tv)
    TextView zaituNumTv;
    @BindView(R.id.kongxian_num_tv)
    TextView kongxianNumTv;
    @BindView(R.id.total_num_tv)
    TextView totalNumTv;
    @BindView(R.id.list)
    PullToRefreshListView lv;
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
    @BindView(R.id.check_tv)
    TextView checkTv;
    @BindView(R.id.add)
    LinearLayout add;
    @BindView(R.id.custom_ll)
    LinearLayout customLl;
    @BindView(R.id.ctrol_ll)
    LinearLayout ctrolLl;
    private MyFleetAdapter adapter;
    private String Org_Code = "";
    private SweetAlertDialog loading;
    private GetMyCarFleetResponse allInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fleet);
        ButterKnife.bind(this);
        titltTv.setText("我的车队");
        customLl.setVisibility(View.VISIBLE);

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MyFleetActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getMyCarListInfo();
            }
        });


    }


    private void getMyCarListInfo() {
        loading = new SweetAlertDialog(MyFleetActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        loading.show();
        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Org_Code = Org_Code;
        LogUtil.d("-->>requese=" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(LPSService.GetMyCarFleet_TAG, LPSService.GetMyCarFleet_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                LogUtil.d("-->>response=" + response.toString());
                GetMyCarFleetResponse dataInfo = GsonUtil.newInstance().fromJson(response, GetMyCarFleetResponse.class);
                Message msg = Message.obtain();
                if (dataInfo != null && dataInfo.Success) {
                    msg.what = Constants.REQUEST_SUCC;
                    msg.obj = dataInfo;
                } else {
                    msg.what = Constants.REQUEST_FAIL;
                    msg.obj = dataInfo;
                }
                myHangler.sendMessage(msg);
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
            lv.onRefreshComplete();
            loading.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    if (msg.obj != null) {
                        allInfo = (GetMyCarFleetResponse) msg.obj;
                        setView(allInfo);
                    } else {
                        DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetActivity.this, Utils.getResourcesString(R.string.get_data_fail), new CustomDialogListener() {
                            @Override
                            public void onDialogClosed(int closeType) {
                                finish();
                            }
                        });
                    }
//                    pullToRefreshSv.setVisibility(View.VISIBLE);
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetActivity.this, Utils.getResourcesString(R.string.get_data_fail), new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            finish();
                        }
                    });
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetActivity.this, Utils.getResourcesString(R.string.get_data_fail), new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            finish();
                        }
                    });
                    break;
            }


        }
    };


    private void setView(GetMyCarFleetResponse info) {
        zaituNumTv.setText(info.getDataValue().getMoveCarSum());
        kongxianNumTv.setText(info.getDataValue().getFreeCarSum());
        totalNumTv.setText(info.getDataValue().getTotalCarSum());
        adapter = new MyFleetAdapter(MyFleetActivity.this, info.getDataValue().getMCarInfoList(), this, this);
        lv.setAdapter(adapter);
//        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCarListInfo();
    }

    @Override
    public void myOnClickItem(int position, Object data) {
        GetMyCarFleetResponse.DataValueBean.MCarInfoListBean info = (GetMyCarFleetResponse.DataValueBean.MCarInfoListBean) data;
        if (info != null) {
            MyFleetLocationInfoActivity.startActivity(MyFleetActivity.this, info);
        }
    }

    @OnClick({R.id.check_tv, R.id.add, R.id.ctrol_ll, R.id.back_llayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.check_tv:
                MyFleetCarTreeListActivity.startActivity(MyFleetActivity.this, 0x12);
                break;
            case R.id.ctrol_ll:
                //车辆监控
                if (allInfo != null && allInfo.getDataValue() != null && allInfo.getDataValue().getMCarInfoList() != null) {
                    MyFleetMonitorMapActivity.start(MyFleetActivity.this, allInfo.getDataValue().getMCarInfoList());
                }
                break;
            case R.id.add:
                OrderDetailPopWindow popWindow = new OrderDetailPopWindow(MyFleetActivity.this, "添加排班", "");
                popWindow.showLocation(R.id.add);
                popWindow.setOnItemClickListener(new OrderDetailPopWindow.OnItemClickListener() {
                    @Override
                    public void onClick(OrderDetailPopWindow.MENUITEM item, String str) {
                        if (str.equals("1")) {
                            startActivity(new Intent(MyFleetActivity.this, DriverAddCarSchedulingActivity.class));
                        } else {

                        }
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0x12) {
            Org_Code = data.getStringExtra("Org_Code").trim();
        }

    }

    @Override
    public void onClickOrder(int position) {
        //跳转到车辆预警
        startActivity(new Intent(this, MyFleetCarStatusExplainActivity.class));
    }
}
