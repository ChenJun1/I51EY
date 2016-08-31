package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetalarminfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.SearchActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.myFleet.mAlarmBean;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetAlarmInfoRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAlarmInfoRsponse;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
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
 * Created by ${ChenJ} on 2016/8/8.
 */
public class MyFleetAlarmInfoActivity extends BaseActivity {
    private final String TAG = MyTaskDetailedActivity.class.getName();
    private static final String KEY_CARKEY = "CARKEY";
    private static final String KEY_CARCODE = "CARCODE";
    private final String STARTIME = " 00:00:00";
    private final String ENDTIME = " 23:59:59";

    private final int Reques_Code = 88;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.titlt_tv)
    TextView titltTv;
    @BindView(R.id.search_iv)
    ImageView searchIv;
    @BindView(R.id.search_ll)
    LinearLayout searchLl;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.lv)
    PullToRefreshListView lv;
    @BindView(R.id.add_ll)
    LinearLayout addLl;
    @BindView(R.id.right_ll)
    LinearLayout rightLl;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;

    private SweetAlertDialog lodingDialog;
    private DataRequestBase dataRequestBase;
    private GetAlarmInfoRequest requestBean;
    private MyFleetAlarmAdapter adapter;
    private List<mAlarmBean> list;
    private ListView myListView;
    private int pageInt = 1;

    private String startTime;
    private String endTime;
    private String carCodeKey;
    private String carCode;

    public static void start(Context context, String Carkey, String CarCode) {
        Intent starter = new Intent(context, MyFleetAlarmInfoActivity.class);
        starter.putExtra(KEY_CARKEY, Carkey);
        starter.putExtra(KEY_CARCODE, CarCode);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_give_an_alarm_info);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra(KEY_CARKEY) != null && getIntent().getStringExtra(KEY_CARCODE) != null) {
            carCodeKey = getIntent().getStringExtra(KEY_CARKEY);
            carCode = getIntent().getStringExtra(KEY_CARCODE);
        } else {
            DialogUtils.showNormalToast(Utils.getResourcesString(R.string.request_Fill));
        }
        initView();
        onRefreshs();
    }

    private void initView() {
        titltTv.setText(carCode);
        rightLl.setVisibility(View.VISIBLE);
        addLl.setVisibility(View.GONE);

        startTime = DateUtil.getNowTime(DateUtil.FORMAT_YMD) + STARTIME;
        endTime = DateUtil.getNowTime(DateUtil.FORMAT_YMD) + ENDTIME;
        lodingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dataRequestBase = new DataRequestBase();
        requestBean = new GetAlarmInfoRequest();
        list = new ArrayList<>();
        adapter = new MyFleetAlarmAdapter(list, this);

        lv.setMode(PullToRefreshBase.Mode.DISABLED);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                onRefreshs();
            }
        });
//        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                onRefreshs();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                onLoad();
//            }
//        });
        myListView = lv.getRefreshableView();
        myListView.setAdapter(adapter);

    }

    private void onRefreshs() {
//        pageInt = 1;
//        dataRequestBase.Page = pageInt;
        requestData(LPSService.GetAlarmInfo_Request_Url);
    }

//    private void onLoad() {
//        pageInt++;
//        dataRequestBase.Page = pageInt;
//        requestData(LPSService.GetAlarmInfo_Request_Url);
//    }

    private void requestData(String Url) {
        lodingDialog.show();
        requestBean.StartTime = startTime;
        requestBean.EndTime = endTime;
        requestBean.CarCode_Key = carCodeKey;
//        requestBean.CarCode_Key="5285F2C2-F2C2-4FC9-B321-67DDE9215B76";
//        requestBean.StartTime="2016-07-20 00:00:00";
//        requestBean.EndTime="2016-07-21 17:01:00";
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        dataRequestBase.Org_Name = MyApplication.getInstance().getLoginInfo().DataValue.Org_Name;
        dataRequestBase.DataValue = requestBean; //JsonUtils.toJsonData(getDriverListRequest);
        LogUtil.d("-->>" + dataRequestBase.toString());
        VolleyManager.newInstance().PostJsonRequest(TAG, Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>response" + response.toString());
                GetAlarmInfoRsponse response1 = JsonUtils.parseData(response.toString(), GetAlarmInfoRsponse.class);
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
                        List<mAlarmBean> dataList = (List<mAlarmBean>) msg.obj;
                        list.clear();
                        if (dataList != null) {
                            list.addAll(dataList);
                        }
                        if (list.size() != 0) {
                            emptyTv.setVisibility(View.GONE);
                        } else {
                            emptyTv.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;
                case Constants.DELETE_SUCC:
                    DialogUtils.showNormalToast(Utils.getResourcesString(R.string.dele_succ));
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(MyFleetAlarmInfoActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    @OnClick({R.id.back_llayout, R.id.search_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.search_ll:
//                Intent intent=new Intent(this, SearchActivity.class);
                SearchActivity.startActivity(this, Reques_Code, DateUtil.FORMAT_YMDHM, 30);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Reques_Code:
                    Bundle bundle = data.getExtras();
                    startTime = bundle.getString(SearchActivity.BEGIN_DATA);
                    endTime = bundle.getString(SearchActivity.END_DATA);
                    startTime = startTime + ":00";
                    endTime = endTime + ":00";
                    onRefreshs();
                    break;
            }
        }
    }
}
