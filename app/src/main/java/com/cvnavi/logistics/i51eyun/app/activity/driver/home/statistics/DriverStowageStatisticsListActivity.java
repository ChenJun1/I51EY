package com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics.DriverStowageStatisticsListAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mGetStowageStatisticsList;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetStowageStatisticsListRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetStowageStatisticsListResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.popupmenu.DriverStowageStatisticsListPopuMenu;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * Created by fan on 2016/7/21.
 * 配载统计数据列表
 */
public class DriverStowageStatisticsListActivity extends BaseActivity {
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
    @BindView(R.id.lv)
    PullToRefreshListView lv;
    @BindView(R.id.empty_tv)
    TextView emptyTv;

    private ArrayList<mGetStowageStatisticsList> mList;
    private DriverStowageStatisticsListAdapter mAdapter;
    private GetStowageStatisticsListRequest request =null;

    DataRequestBase requestBase;
    private boolean isRefresh = false;//是否刷新
    private String mStarTime;
    private String mEendTime;
    private Intent intent;

    private SweetAlertDialog sweetAlertDialog = null;
    private DriverStowageStatisticsListPopuMenu popuMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stowage_statistics_list);
        ButterKnife.bind(this);

        popuMenu = new DriverStowageStatisticsListPopuMenu(this);

        init();
    }

    private void init() {

        titleTv.setText("配载明细");
        operationBtn.setVisibility(View.VISIBLE);
        operationBtn.setText("全部");
        sweetAlertDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);

        intent = getIntent();
        mStarTime = intent.getStringExtra("mStarTime");
        mEendTime = intent.getStringExtra("mEendTime");
        mList = new ArrayList<>();

        requestBase = new DataRequestBase();
        request = new GetStowageStatisticsListRequest();

        mAdapter = new DriverStowageStatisticsListAdapter(mList, this);
        lv.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sweetAlertDialog.show();
        mList.clear();
        mAdapter.notifyDataSetChanged();
        requestHttp();
    }

    private void requestHttp() {

        request.BeginDate = mStarTime;
        request.EndDate = mEendTime;

        requestBase.DataValue = request;
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        requestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);


        VolleyManager.newInstance().PostJsonRequest("Tag", TMSService.GetStowageStatisticsList_Request_Url, jsonObject,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        LogUtil.d("-->>onResponse============>>" + response.toString());
                        List<mGetStowageStatisticsList> carLocusAnaly = null;

                        GetStowageStatisticsListResponse analysisRequest = JsonUtils.parseData(response.toString(), GetStowageStatisticsListResponse.class);

                        Message msg = Message.obtain();
                        if (analysisRequest.Success) {
                            carLocusAnaly = analysisRequest.DataValue;
                            msg.what = Constants.REQUEST_SUCC;
                            msg.obj = carLocusAnaly;
                            mHandler.sendMessage(msg);
                        } else {
                            msg.what = Constants.REQUEST_FAIL;
                            mHandler.sendMessage(msg);

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        LogUtil.i("ErrorListener============>>" + TMSService.GetReceivableAccount_Request_Url);
                        Message msg = Message.obtain();
                        msg.what = Constants.REQUEST_ERROR;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sweetAlertDialog.dismiss();
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.REQUEST_SUCC:

                    List<mGetStowageStatisticsList> mGather = (List<mGetStowageStatisticsList>) msg.obj;
                    if (mGather.size() > 0) {
                        if (isRefresh) {
                            mList.clear();
                            isRefresh = false;
                        }
                        emptyTv.setVisibility(View.GONE);
                        mList.addAll(mGather);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                    lv.onRefreshComplete();
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverStowageStatisticsListActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    @OnClick({R.id.back_llayout,R.id.operation_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.operation_btn:
                popuMenu.showLocation(R.id.operation_btn);// 设置弹出菜单弹出的位置
                // 设置回调监听，获取点击事件
                popuMenu.setOnItemClickListener(new DriverStowageStatisticsListPopuMenu.OnItemClickListener() {

                    @Override
                    public void onClick(DriverStowageStatisticsListPopuMenu.MENUITEM item, String str) {
//                        DialogUtils.showNormalToast(str);
                        sweetAlertDialog.show();
                        if (!str.equals("")) {
                            request.Traffic_Mode = str;
                            mList.clear();
                            mAdapter.notifyDataSetChanged();
                            requestHttp();

                        } else {
                            request.BeginDate = intent.getStringExtra("mStarTime");//
                            request.EndDate = intent.getStringExtra("mEendTime");//
                            request.Traffic_Mode="";
                            mList.clear();
                            mAdapter.notifyDataSetChanged();
                            requestHttp();
                        }
                    }
                });

                break;

        }
    }
}
