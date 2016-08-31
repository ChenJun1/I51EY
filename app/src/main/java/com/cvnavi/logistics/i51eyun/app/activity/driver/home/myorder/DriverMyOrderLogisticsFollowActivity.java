package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyOrder.LogisticsFollowBean;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyOrder.LogisticsFollowNoteBean;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetLogisticsFollowRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetLogisicsFollowResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
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
 * Created by Administrator on 2016/7/22.
 */
public class DriverMyOrderLogisticsFollowActivity extends BaseActivity {

    private final String TAG = MyTaskDetailedActivity.class.getName();
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.Letter_Status_tv)
    TextView LetterStatusTv;
    @BindView(R.id.Letter_Oid_tv)
    TextView LetterOidTv;
    @BindView(R.id.logistics_follow_lv)
    ListView logisticsFollowLv;

    private List<LogisticsFollowNoteBean> list;

    private LogisticsFollowBean bean = null;

    private SweetAlertDialog lodingDialog;
    private DataRequestBase dataRequestBase;

    private DriverMyOrderLogisticsFollowAdapter adapter;
    private static final String AllTicket = "AllTicket";
    private String allTicket;

    public static void startActivity(Activity activity, String allTcketNo) {
        Intent intent = new Intent(activity, DriverMyOrderLogisticsFollowActivity.class);
        intent.putExtra(AllTicket, allTcketNo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_follow);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        titleTv.setText(Utils.getResourcesString(R.string.logistics_follow));
        lodingDialog = lodingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        list = new ArrayList<>();
        adapter = new DriverMyOrderLogisticsFollowAdapter(list, this);
        logisticsFollowLv.setAdapter(adapter);
        lodingDialog.show();
        loadDataRequest(TMSService.Selectticket_Request_Url);

    }

    private void loadDataRequest(final String Url) {
        dataRequestBase = new DataRequestBase();
        allTicket = getIntent().getStringExtra(AllTicket);
        GetLogisticsFollowRequest reqest = new GetLogisticsFollowRequest();
        reqest.All_Ticket_No = allTicket;
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;

        dataRequestBase.DataValue = reqest; //JsonUtils.toJsonData(getDriverListRequest);
        LogUtil.d("-->>" + dataRequestBase.toString());
        VolleyManager.newInstance().PostJsonRequest(TAG, Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>response" + response.toString());
                GetLogisicsFollowResponse response1 = JsonUtils.parseData(response.toString(), GetLogisicsFollowResponse.class);
                Message msg = Message.obtain();
                if (response1.Success) {
                    msg.obj = response1.DataValue;
                    msg.what = Constants.REQUEST_SUCC;
                    myHandler.sendMessage(msg);
                } else {
                    msg.obj = response1.ErrorText;
                    msg.what = Constants.REQUEST_FAIL;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                Message msg = Message.obtain();
                msg.obj = error.getMessage();
                msg.what = Constants.REQUEST_ERROR;
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
                        bean = (LogisticsFollowBean) msg.obj;
                        setDateValue();
                        if (bean.TrackTicket != null) {
                            list.clear();
                            list.addAll(bean.TrackTicket);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;
                case Constants.DELETE_SUCC:
                    DialogUtils.showNormalToast(Utils.getResourcesString(R.string.dele_succ));
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverMyOrderLogisticsFollowActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    private void setDateValue() {
        if (bean != null) {
            SetViewValueUtil.setTextViewValue(LetterStatusTv, bean.Transport_Status);
            SetViewValueUtil.setTextViewValue(LetterOidTv, bean.Ticket_No);
        }
    }

    @OnClick(R.id.back_llayout)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_llayout:
                finish();
                break;
        }
    }

}
