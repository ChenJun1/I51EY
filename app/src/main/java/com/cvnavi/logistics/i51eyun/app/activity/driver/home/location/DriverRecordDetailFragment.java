package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

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
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.location.DriverRecordDetailAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocus;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetHistoryLocusRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetHistoryLocusResponse;
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
 * Created by ${chuzy} on 2016/7/1. 详情记录
 */
public class DriverRecordDetailFragment extends BaseFragment {
    private final String TAG = DriverRecordDetailFragment.class.getName();

    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.pull_lv)
    PullToRefreshListView pullLv;

    private DriverRecordDetailAdapter adapter;

    public static ArrayList<mCarHistoryLocus> mList = new ArrayList<>();

    public static DriverRecordDetailFragment getInstance() {
        return new DriverRecordDetailFragment();
    }


    private SweetAlertDialog loadingDialog = null;

    private String startTime;
    private String endTime;
    private String carCodeKey;

    private String ID;
    private boolean isLoadFail = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_record_analysis, container, false);
        ButterKnife.bind(this, view);
        loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        initView();
        requestData();
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isLoadFail) {
            requestData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {


        ID = getActivity().getIntent().getStringExtra(Constants.CAR_KEY);
        if (!ID.equals("F")) {
            carCodeKey = ID;
            startTime = DateUtil.getBeforeOneDayTime();
            endTime = DateUtil.getCurDateStr(DateUtil.FORMAT_YMDHM);
        } else {
            carCodeKey = getActivity().getIntent().getStringExtra("carCodeKey");
            startTime = getActivity().getIntent().getStringExtra("startTime");
            endTime = getActivity().getIntent().getStringExtra("endTime");
        }

        adapter = new DriverRecordDetailAdapter(mList, getActivity());

        pullLv.setMode(PullToRefreshBase.Mode.DISABLED);
        pullLv.setAdapter(adapter);
        pullLv.setOnItemClickListener(itemClickListener);
        pullLv.setShowIndicator(true);
//        listview=pullLv.getRefreshableView();
//
//        btToTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listview.smoothScrollToPosition(0);//平滑的返回顶部
//            }
//        });

    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mList != null) {

                mCarHistoryLocus Bean = mList.get(position - 1);

                Intent intent = new Intent(getActivity(), DriverTrackMapActivity.class);
                intent.putExtra("Data", Bean);
                intent.putExtra("ActivityName", "DriverRecordDetailFragment");
                startActivity(intent);
            }

        }
    };

    private void requestData() {

        if (loadingDialog == null) {
            loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }
        loadingDialog.show();
        DataRequestBase requestBase = new DataRequestBase();

        final GetHistoryLocusRequest request = new GetHistoryLocusRequest();
        request.StarTime = startTime;
        request.EndTime = endTime;
        request.KeyList = carCodeKey;

        requestBase.DataValue = JsonUtils.toJsonData(request);
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;

        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);
        VolleyManager.newInstance().PostJsonRequest(TAG, LPSService.GetHistoryLocus_Request_Url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                LogUtil.d("-->>onResponse============>>" + response.toString());
                List<mCarHistoryLocus> carLocusAnaly = null;

                GetHistoryLocusResponse analysisRequest = JsonUtils.parseData(response.toString(), GetHistoryLocusResponse.class);
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
            loadingDialog.dismiss();
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    List<mCarHistoryLocus> datalist = (List<mCarHistoryLocus>) msg.obj;
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
}
