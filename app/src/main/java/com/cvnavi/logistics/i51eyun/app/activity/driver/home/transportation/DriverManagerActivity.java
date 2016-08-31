package com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans.DriverManagerListViewAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarSchedulingDriver;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetDriverListRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.DataResponseBase;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetDriverListResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by ${ChenJ} on 2016/7/25.
 */
public class DriverManagerActivity extends BaseActivity {
    private static String TAG = DriverManagerActivity.class.getName();


    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.back_llayout)
    LinearLayout back_llayout;
    @BindView(R.id.add_ll)
    LinearLayout add_ll;
    @BindView(R.id.driver_manager_lv)
    PullToRefreshListView driverManagerLv;


    private ListView actualListView;
    private DriverManagerListViewAdapter adapter;
    private List<mCarSchedulingDriver> dataList;

    private Context mContext;
    private boolean isRefresh = false;
    DataRequestBase dataRequestBase;
    private int pageInt = 1;

    private SweetAlertDialog lodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_manager);
        ButterKnife.bind(this);
        dataRequestBase = new DataRequestBase();
        mContext = this;
        titleTv.setText(R.string.driver_manager);
        if (Utils.checkOperate(Constants.OPERATE_CD_ADD_DRIVER)) {
            add_ll.setVisibility(View.VISIBLE);
        } else {
            add_ll.setVisibility(View.GONE);
        }
        dataList = new ArrayList<>();
        adapter = new DriverManagerListViewAdapter(dataList, this);
        lodingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        init();


    }

    private void init() {
        driverManagerLv.setMode(PullToRefreshBase.Mode.BOTH);

        driverManagerLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageInt++;
                dataRequestBase.Page = pageInt;
                loadData();
            }
        });
        actualListView = driverManagerLv.getRefreshableView();
        actualListView.setAdapter(adapter);
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Utils.checkOperate(Constants.OPERATE_CD_EDIT_DRIVER)) {
                    mCarSchedulingDriver driverInfo = dataList.get(position - 1);
                    if (driverInfo != null) {
                        Intent intent = new Intent(DriverManagerActivity.this, DriverEditActivity.class);
                        intent.putExtra(Constants.DriverInfo, driverInfo);
                        startActivity(intent);
                    }
                } else {
                    DialogUtils.showWarningToast("您没有编辑权限！");
                }


            }
        });
        actualListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final mCarSchedulingDriver carSchedulingDriver = (mCarSchedulingDriver) adapter.getItem(position - 1);

                DialogUtils.showMessageDialog(DriverManagerActivity.this, "提示", "确定删除司机" + carSchedulingDriver.Driver, "确定", "取消", new CustomDialogListener() {
                    @Override
                    public void onDialogClosed(int closeType) {
                        if (CustomDialogListener.BUTTON_NO == closeType) {
                        } else if (CustomDialogListener.BUTTON_OK == closeType) {
                            if (Utils.checkOperate(Constants.OPERATE_CD_DELEC_DRIVER)) {
                                deleteDriver(TMSService.DelDriver_Request_Url, carSchedulingDriver);
                            } else {
                                DialogUtils.showWarningToast("您没有删除权限！");
                            }

                        }
                    }
                });

                return true;
            }
        });
    }

    private void deleteDriver(final String Url, mCarSchedulingDriver carSchedulingDriver) {

        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.DataValue = carSchedulingDriver.Serial_Oid;

        VolleyManager.newInstance().PostJsonRequest(TAG, Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DataResponseBase dataResponseBase = JsonUtils.parseData(response.toString(), DataResponseBase.class);

                Message msg = Message.obtain();
                if (dataResponseBase.Success) {
                    msg.what = Constants.DELETE_SUCC;
                    myHandler.sendMessage(msg);
                } else {
                    msg.obj = dataResponseBase.ErrorText;
                    msg.what = Constants.REQUEST_FAIL;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = Message.obtain();
                msg.obj = error.getMessage();
                msg.what = Constants.REQUEST_ERROR;
                myHandler.sendMessage(msg);
            }
        });
    }

    private void onRefresh() {
        lodingDialog.show();
        isRefresh = true;
        pageInt = 1;
        dataRequestBase.Page = pageInt;
        loadData();
    }

    private void loadData() {
        GetDriverListRequest getDriverListRequest = new GetDriverListRequest();
//        getDriverListRequest.Operate_Org = "";
//        getDriverListRequest.Driver = "";
//        getDriverListRequest.Search_JM = "";
//        getDriverListRequest.Driver_Tel = "";
//        getDriverListRequest.Driver_License_Type_Oid = "";

        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.DataValue = getDriverListRequest; //JsonUtils.toJsonData(getDriverListRequest);

        VolleyManager.newInstance().PostJsonRequest(TAG, TMSService.GetDriverList_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GetDriverListResponse responseBase = JsonUtils.parseData(response.toString(), GetDriverListResponse.class);

                Message msg = Message.obtain();
                if (responseBase.Success) {
                    msg.obj = responseBase.DataValue;
                    msg.what = Constants.REQUEST_SUCC;
                    myHandler.sendMessage(msg);
                } else {
                    msg.obj = responseBase.ErrorText;
                    msg.what = Constants.REQUEST_FAIL;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = Message.obtain();
                msg.obj = error.getMessage();
                msg.what = Constants.REQUEST_FAIL;
                myHandler.sendMessage(msg);
            }
        });
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (lodingDialog != null) {
                lodingDialog.dismiss();
            }
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    if (msg.obj != null) {
                        if (isRefresh) {
                            isRefresh = false;
                            dataList.clear();
                        }
                        List<mCarSchedulingDriver> list = (List<mCarSchedulingDriver>) msg.obj;
                        dataList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                    driverManagerLv.onRefreshComplete();
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;
                case Constants.DELETE_SUCC:
                    DialogUtils.showNormalToast(Utils.getResourcesString(R.string.dele_succ));
                    onRefresh();
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverManagerActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;


            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @OnClick({R.id.add_ll, R.id.back_llayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_ll:
                showActivity(DriverManagerActivity.this, DriverAddActivity.class);
                break;
            case R.id.back_llayout:
                finish();
                break;
        }


    }
}
