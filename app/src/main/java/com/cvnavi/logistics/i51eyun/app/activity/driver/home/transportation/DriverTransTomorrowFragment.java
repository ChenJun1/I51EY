package com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans.DriverTransListViewAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarShift;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.DelCarShiftRequest;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetShiftListRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.DelCarShiftResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetShiftListResponse;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.trans.OnClickItemListener;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.LoadingDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.google.gson.Gson;
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
 * Created by ${chuzy} on 2016/6/24.
 */
public class DriverTransTomorrowFragment extends BaseFragment implements OnClickItemListener {


    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;
    private DriverTransListViewAdapter adapter = null;
    private List<mCarShift> dataList;
    private SweetAlertDialog loadingDialog = null;
    private mCarShift carInfo;
    private boolean isRefresh = false;
    private boolean isDelect = false;
    DataRequestBase dataRequestBase;
    private int pageInt = 1;

    public static DriverTransTomorrowFragment getInstance() {
        return new DriverTransTomorrowFragment();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_home_trans_last_day, container, false);
        ButterKnife.bind(this, view);
        dataRequestBase = new DataRequestBase();

        pullRefreshList.setMode(PullToRefreshBase.Mode.BOTH);
        dataList = new ArrayList<>();
        adapter = new DriverTransListViewAdapter(dataList, getActivity(), DriverTransTomorrowFragment.this);
        pullRefreshList.setAdapter(adapter);

        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                isRefresh = true;
                pageInt = 1;
                dataRequestBase.Page = pageInt;

                //执行刷新
                getSchedulingList(DateUtil.getLastNDays(1), DateUtil.getLastNDays(1), false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                pageInt++;
                dataRequestBase.Page = pageInt;
                //执行刷新
                getSchedulingList(DateUtil.getLastNDays(1), DateUtil.getLastNDays(1), false);
            }
        });

        return view;

    }

    private void delDia(boolean showDia) {
        if (loadingDialog == null) {
            loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }
        if (showDia) {
            loadingDialog.show();

        }
    }

    private void showDia(boolean showDia) {
        if (loadingDialog == null) {
            loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }
        if (showDia) {
            loadingDialog.show();

        }
    }

    private void dimiss() {
        loadingDialog.dismiss();
    }


    @Override
    public void onClick(int position) {

        if (Utils.checkOperate(Constants.OPERATE_CD_MODIF)) {
            LogUtil.d("-->> onClick");
            if (dataList != null && dataList.get(position) != null) {
                Intent intent = new Intent(getActivity(), DriverEditCarSchedulingActivity.class);
                intent.putExtra(Constants.EDIT_CAR_SCHEDULING, dataList.get(position));
                startActivity(intent);
            }
        } else {
            DialogUtils.showWarningToast("您没有该权限");
        }


    }

    @Override
    public void onLongClick(mCarShift info, final int position) {
        if (Utils.checkOperate(Constants.OPERATE_CD_DELE)) {
            carInfo = info;
            DialogUtils.showMessageDialog(getActivity(), "删除数据", "是否删除该条数据？", "确定", "取消", new CustomDialogListener() {
                @Override
                public void onDialogClosed(int closeType) {
                    if (closeType == CustomDialogListener.BUTTON_OK) {
                        delDia(true);
                        GetAppLoginResponse loginInfo = MyApplication.getInstance().getLoginInfo();
                        DelCarShiftRequest datavalue = new DelCarShiftRequest();

                        datavalue.Serial_Oid = carInfo.Serial_Oid;
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
                                        msg.obj = position;
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

                    } else {

                    }

                }
            });
        } else {
            DialogUtils.showWarningToast("您没有该权限");
        }


    }


    private Handler deletHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dimiss();
            pullRefreshList.onRefreshComplete();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    isDelect = true;

                    getSchedulingList(DateUtil.getLastNDays(1), DateUtil.getLastNDays(1), true);
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


    private void getSchedulingList(String beginDate, String endDate, boolean showDia) {

        showDia(showDia);

        GetAppLoginResponse info = MyApplication.getInstance().getLoginInfo();
        final GetShiftListRequest getShiftListRequest = new GetShiftListRequest();
        getShiftListRequest.BeginDate = beginDate;
        getShiftListRequest.EndDate = endDate;

        dataRequestBase.DataValue = getShiftListRequest;
        dataRequestBase.User_Key = info.DataValue.User_Key;
        dataRequestBase.UserType_Oid = info.DataValue.UserType_Oid;
        dataRequestBase.Token = info.DataValue.Token;
        dataRequestBase.Company_Oid = info.DataValue.Company_Oid;

        LogUtil.d("-->>Torrmow request=" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(TMSService.GetShiftList_TAG, TMSService.GetShiftList_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>Torrmow respon=" + response.toString());
                GetShiftListResponse getShiftListResponse = GsonUtil.newInstance().fromJson(response, GetShiftListResponse.class);
                Message msg = Message.obtain();
                if (getShiftListResponse != null) {
                    if (getShiftListResponse.Success && getShiftListResponse.DataValue != null) {
                        msg.what = Constants.REQUEST_SUCC;
                        msg.obj = getShiftListResponse.DataValue;
                    } else {
                        if (getShiftListResponse.DataValue == null) {
                            msg.what = Constants.REQUEST_FAIL;
                            msg.obj = Utils.getResourcesString(R.string.list_null);
                        } else {
                            msg.what = Constants.REQUEST_FAIL;
                            msg.obj = getShiftListResponse.ErrorText;
                        }
                    }
                } else {
                    msg.what = Constants.REQUEST_FAIL;
                }
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = Message.obtain();
                message.what = Constants.REQUEST_FAIL;
                message.obj = error.toString();
                handler.sendMessage(message);
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dimiss();
            pullRefreshList.onRefreshComplete();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
//                    dataList = (List<mCarShift>) msg.obj;
//                    adapter = new DriverTransListViewAdapter(dataList, getActivity(), DriverTransLastDayFragment.this);
//                    pullRefreshList.setAdapter(adapter);
                    if (msg.obj != null) {
                        if (isRefresh) {
                            isRefresh = false;
                            dataList.clear();
                        }
                        if (isDelect) {
                            isDelect = false;
                            dataList.clear();
                        }
                        List<mCarShift> list = (List<mCarShift>) msg.obj;
                        dataList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }

                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.get_data_fail));
                    break;
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        isRefresh = true;
        getSchedulingList(DateUtil.getLastNDays(1), DateUtil.getLastNDays(1), true);
    }
}
