package com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics.DriverOrderListAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverHomeOrderDeatilActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mGetOrederList;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetOrederStatisticsListRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetOrderStatisticsListResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.popupmenu.DriverOrderListPopu;
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
 * Created by fan on 2016/7/20.
 */
public class DriverOrderListActivity extends BaseActivity {


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

    private ArrayList<mGetOrederList> mList;
    private DriverOrderListAdapter mAdapter;

    private ListView actualListView;
    DataRequestBase requestBase;
    private boolean isRefresh = false;//是否刷新
    private int PageNum = 1;

    SweetAlertDialog sweetAlertDialog = null;
    private String mStarTime;
    private String mEendTime;

    private Intent intent;

    GetOrederStatisticsListRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ist);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        titleTv.setText("货单明细");
        operationBtn.setVisibility(View.VISIBLE);
        operationBtn.setText("全部");
        intent = getIntent();
        mStarTime = intent.getStringExtra("mStarTime");
        mEendTime = intent.getStringExtra("mEendTime");
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        mList = new ArrayList<>();
        requestBase = new DataRequestBase();
        request = new GetOrederStatisticsListRequest();
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(DriverOrderListActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PageNum++;
                requestBase.Page = PageNum;
                requestHttp();
            }
        });
        mAdapter = new DriverOrderListAdapter(mList, this);
        actualListView = lv.getRefreshableView();
        actualListView.setAdapter(mAdapter);

        lv.setOnItemClickListener(itemClickListener);

        onRefresh();
    }


    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mList != null) {
                mGetOrederList Bean = mList.get(position - 1);
                Intent intent = new Intent(DriverOrderListActivity.this, DriverHomeOrderDeatilActivity.class);
                intent.putExtra(DriverHomeOrderDeatilActivity.ORDER_ID, Bean.All_Ticket_No);
                intent.putExtra(DriverHomeOrderDeatilActivity.FROM_TAG, DriverHomeOrderDeatilActivity.FROM_TAG_STATIC);

                startActivity(intent);
            }

        }
    };

    private void onRefresh() {
        isRefresh = true;
        PageNum = 1;
        requestBase.Page = PageNum;
        sweetAlertDialog.show();
        requestHttp();
    }

    private void requestHttp() {
//        final GetOrederStatisticsListRequest request = new GetOrederStatisticsListRequest();

//        if (MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid.equals("E")) {
//            //货主查询货主的
//            request.SendMan_Tel = MyApplication.getInstance().getLoginInfo().DataValue.User_Tel;
//        }
//
//        if (MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid.equals("G")) {
//            //司机查询司机的
//            request.Driver_Tel = MyApplication.getInstance().getLoginInfo().DataValue.User_Tel;
//
//        }
        request.BeginDate = mStarTime;
        request.EndDate = mEendTime;

        requestBase.DataValue = request;
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);

        VolleyManager.newInstance().PostJsonRequest("Tag", TMSService.GetOrederList_Request_Url, jsonObject,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        LogUtil.d("-->>onResponse============>>" + response.toString());
                        List<mGetOrederList> carLocusAnaly = null;

                        GetOrderStatisticsListResponse analysisRequest = JsonUtils.parseData(response.toString(), GetOrderStatisticsListResponse.class);

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

                    List<mGetOrederList> mGather = (List<mGetOrederList>) msg.obj;
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
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverOrderListActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    @OnClick({R.id.back_llayout, R.id.operation_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.operation_btn:
                DriverOrderListPopu popu = new DriverOrderListPopu(DriverOrderListActivity.this);
                popu.showLocation(R.id.operation_btn);
                popu.setOnItemClickListener(new DriverOrderListPopu.OnItemClickListener() {
                    @Override
                    public void onClick(DriverOrderListPopu.MENUITEM item, String str) {

                        if (!str.equals("")) {
//                            if (str.equals("3")||str.equals("1")||str.equals("2")){
//                                request.DeliverStatus=str;
//                                LogUtil.d("---DeliverStatus="+str);
//                            }else if(str.equals("作废")) {
//                                request.Ticket_Status=str;
//                                LogUtil.d("---Ticket_Status="+str);
//                            }
                            switch (str) {
                                case "3":
                                case "1":
                                case "2":
                                    request.DeliverStatus = str;
                                    request.Ticket_Status = null;
                                    LogUtil.d("---DeliverStatus=" + str);
                                    break;
                                case "作废":
                                    request.Ticket_Status = str;
                                    request.DeliverStatus = null;
                                    LogUtil.d("---Ticket_Status=" + str);
                                    break;
                            }

                            mList.clear();
                            mAdapter.notifyDataSetChanged();
                            onRefresh();
                        } else {

                            request.BeginDate = intent.getStringExtra("mStarTime");
                            request.EndDate = intent.getStringExtra("mEendTime");
                            request.Ticket_Status = "";
                            request.DeliverStatus = "";
                            mList.clear();
                            mAdapter.notifyDataSetChanged();
                            onRefresh();
                        }
                    }
                });
                break;

        }
    }
}
