package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location.DriverRecordAnalysisAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocusAnalysis;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetHistoryLocusAnalysisRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetHistoryLocusAnalysisResponse;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.LoadingDialog;
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
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/1.  记录分析
 */
public class DriverRecordAnalysisFragment extends BaseFragment {
    private final String TAG = DriverRecordAnalysisFragment.class.getName();
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.pull_lv)
    PullToRefreshListView pullLv;

    private DriverRecordAnalysisAdapter adapter;

    public static ArrayList<mCarHistoryLocusAnalysis> mList = new ArrayList<>();

    public static DriverRecordAnalysisFragment getInstance() {
        return new DriverRecordAnalysisFragment();
    }

    private String startTime;
    private String endTime;
    private String carCodeKey;

    private String ID;
    private boolean isLoadFail = false;
    private SweetAlertDialog loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_record_analysis, container, false);
        ButterKnife.bind(this, view);
        initView();
        if (getUserVisibleHint()) {
            requestData();
        }
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.d("-->>1 isVisibleToUser = " + isVisibleToUser);
        if (isVisibleToUser && isLoadFail) {
            requestData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {
        DriverRecordMainActivity activity = (DriverRecordMainActivity) getActivity();

        ID = getActivity().getIntent().getStringExtra(Constants.CAR_KEY);
        if (!ID.equals("F")) {
            carCodeKey = ID;
            startTime = DateUtil.getBeforeOneDayTime();
            endTime = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
        } else {
            carCodeKey = activity.carCodeKey;
            startTime = activity.startTime;
            endTime = activity.endTime;
// carCodeKey = getActivity().getIntent().getStringExtra("carCodeKey");
//            startTime = getActivity().getIntent().getStringExtra("startTime");
//            endTime = getActivity().getIntent().getStringExtra("endTime");
        }
        adapter = new DriverRecordAnalysisAdapter(mList, getActivity());
        pullLv.setMode(PullToRefreshBase.Mode.DISABLED);
        pullLv.setAdapter(adapter);
        pullLv.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mList != null) {

                mCarHistoryLocusAnalysis Bean = mList.get(position - 1);

                Intent intent = new Intent(getActivity(), DriverTrackMapActivity.class);
                intent.putExtra("Data", Bean);
                intent.putExtra("ActivityName", "DriverRecordAnalysisFragment");
                intent.putExtra("carCodeKey", carCodeKey);
                startActivity(intent);
            }

        }
    };


    private void requestData() {
//        initView();
        loading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loading.show();
        DataRequestBase requestBase = new DataRequestBase();

        final GetHistoryLocusAnalysisRequest request = new GetHistoryLocusAnalysisRequest();


        request.StarTime = startTime;
        request.EndTime = endTime;
        request.KeyList = carCodeKey;

//        request.KeyList="293af69a-7925-4b90-8428-acbb6bca0291";
//        request.StarTime="2016-06-8";
//        request.EndTime="2016-06-9 17: 01";

        requestBase.DataValue = JsonUtils.toJsonData(request);
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);
        LogUtil.d("-->>request = " + jsonObject.toString());
        VolleyManager.newInstance().PostJsonRequest(TAG, LPSService.GetHistoryLocusAnalysis_Request_Url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                LogUtil.d("-->>onResponse============>>" + response.toString());
                List<mCarHistoryLocusAnalysis> carLocusAnaly = null;

                GetHistoryLocusAnalysisResponse analysisRequest = JsonUtils.parseData(response.toString(), GetHistoryLocusAnalysisResponse.class);

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
                Log.i(TAG, "ErrorListener============>>" + error.getMessage(), error);
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
            loading.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    List<mCarHistoryLocusAnalysis> datalist = (List<mCarHistoryLocusAnalysis>) msg.obj;
                    if (datalist != null) {
                        if (datalist.size() > 0) {
                            isLoadFail = false;
                            emptyTv.setVisibility(View.GONE);
                        } else {
                            emptyTv.setVisibility(View.VISIBLE);
                        }
                        mList.clear();
                        mList.addAll(datalist);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    isLoadFail = true;
                    DialogUtils.showNormalToast("暂未查询到任何信息!");
                    break;

                case Constants.REQUEST_ERROR:
                    isLoadFail = true;
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(getActivity(), Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
    }
}
