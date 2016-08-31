package com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics.DriverAccountsReceiveableMainAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.ReceivableAccount.GatherFeeStatistics;
import com.cvnavi.logistics.i51eyun.app.bean.model.ReceivableAccount.mReceivableAccount;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetReceivableAccountRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetRecrivableAccountResponse;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.mytask.DriverChioceTimeCallback;
import com.cvnavi.logistics.i51eyun.app.callback.manager.DriverChioceTimeCallbackManager;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import volley.VolleyManager;

/**
 * Created by fan on 2016/7/19.
 * 应收款最近三天
 */
public class DriverAccountReceiveThreeDayFragment extends BaseFragment implements DriverChioceTimeCallback {
    @BindView(R.id.pull_lv)
    PullToRefreshListView pullLv;
    @BindView(R.id.AllTotalFee_text)
    TextView AllTotalFeeText;
    @BindView(R.id.empty_tv)
    TextView emptyTv;

    private ListView actualListView;
    private DriverAccountsReceiveableMainAdapter adapter;
    private mReceivableAccount mList;
    private ArrayList<GatherFeeStatistics> mGatherFeeStatistics;
    private SweetAlertDialog sweetAlertDialog = null;
    DataRequestBase requestBase;
    private boolean isRefresh = false;//是否刷新
    private int PageNum = 1;

    private String bengTime;
    private String endsTime;

    public static DriverAccountReceiveThreeDayFragment getInstance() {
        return new DriverAccountReceiveThreeDayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_accounts_pay_main, container, false);
        ButterKnife.bind(this, view);

        initView();
        DriverChioceTimeCallbackManager.newInstance().add(this);
        return view;

    }


    private void initView() {
        mList = new mReceivableAccount();
        mGatherFeeStatistics = new ArrayList<>();
        requestBase = new DataRequestBase();
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);

        bengTime = DateUtil.strOldFormat2NewFormat(DateUtil.getLastNDays(-3), DateUtil.FORMAT_YMD, DateUtil.FORMAT_YMDHMS);
        endsTime = DateUtil.strOldFormat2NewFormat(DateUtil.getLastNDays(0), DateUtil.FORMAT_YMD, DateUtil.FORMAT_YMDHMS);

        pullLv.setMode(PullToRefreshBase.Mode.BOTH);
        pullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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

        actualListView = pullLv.getRefreshableView();
        adapter = new DriverAccountsReceiveableMainAdapter(mGatherFeeStatistics, getActivity());
        actualListView.setAdapter(adapter);

        pullLv.setOnItemClickListener(itemClickListener);
        onRefresh();
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mGatherFeeStatistics != null) {
                GatherFeeStatistics Bean = mGatherFeeStatistics.get(position - 1);
                Intent intent = new Intent(getActivity(), DriverAccountsReceiveListActivity.class);
                intent.putExtra("Data", Bean);
                intent.putExtra("bengTime", bengTime);
                intent.putExtra("endsTime", endsTime);
                intent.putExtra("ActivityName", "DriverAccountReceiveThreeDayFragment");
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
        final GetReceivableAccountRequest request = new GetReceivableAccountRequest();
        request.BeginDate = bengTime;
        request.EndDate = endsTime;

        requestBase.DataValue = request;
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        requestBase.Org_Name = MyApplication.getInstance().getLoginInfo().DataValue.Org_Name;
        requestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);


        VolleyManager.newInstance().PostJsonRequest("Tag", TMSService.GetReceivableAccount_Request_Url, jsonObject,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        LogUtil.d("-->>onResponse============>>" + response.toString());
                        mReceivableAccount carLocusAnaly = null;

                        GetRecrivableAccountResponse analysisRequest = JsonUtils.parseData(response.toString(), GetRecrivableAccountResponse.class);

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
            super.handleMessage(msg);
            sweetAlertDialog.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:

                    mReceivableAccount datalist = (mReceivableAccount) msg.obj;
                    SetViewValueUtil.setTextViewValue(AllTotalFeeText, datalist.AllTotalFee);
                    List<GatherFeeStatistics> mGather = datalist.GatherFeeStatistics;
                    if (mGather.size() > 0) {
                        if (isRefresh) {
                            mGatherFeeStatistics.clear();
                            isRefresh = false;
                        }
                        mGatherFeeStatistics.addAll(mGather);
                        adapter.notifyDataSetChanged();
                    }

                    if (msg.obj!=null){
                        emptyTv.setVisibility(View.GONE);
                    }else {
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                    pullLv.onRefreshComplete();

                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;

                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(getActivity(), Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    @Override
    public void OnTimeChioce(String strTime, String endTime, int tag) {
        if (tag == 1) {
            bengTime = DateUtil.strOldFormat2NewFormat(strTime, DateUtil.FORMAT_YMD, DateUtil.FORMAT_YMD_SN);
            endsTime = DateUtil.strOldFormat2NewFormat(endTime, DateUtil.FORMAT_YMD, DateUtil.FORMAT_YMD_SN);
            onRefresh();
        }
    }
}
