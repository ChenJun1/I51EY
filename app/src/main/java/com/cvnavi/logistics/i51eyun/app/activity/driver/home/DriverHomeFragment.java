package com.cvnavi.logistics.i51eyun.app.activity.driver.home;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.home.myorder.MyOrderDetailAcitivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.DriverHomeFragmentListViewAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.DriverHomeFragmentYeWuAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverCarTreeListActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverHistoricalTrackMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverSingleCarMonitorActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myorder.DriverCarExceptionUpLoadActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskCarryDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskDetailedActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverHomeOrderDeatilActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverQueryOrderActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.scanning.MipcaActivityCapture;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverTransportationActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.GetDriverNoStartTask;
import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.TaskBean;
import com.cvnavi.logistics.i51eyun.app.bean.model.mImageBanner;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetDriverNoStartTaskRequest;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetTaskListRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetDriverNoStartTaskResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetTaskListResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SharedPreferencesTool;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.BaseWebActivity;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.CircleFlowIndicator;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ImagePagerAdapter;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ViewFlow;
import com.cvnavi.logistics.i51eyun.app.widget.gridview.MyGridView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 司机端主页信息
 */
public class DriverHomeFragment extends BaseFragment {
    @BindView(R.id.pullToRefresh_sv)
    PullToRefreshScrollView pullToRefreshSv;
    @BindView(R.id.yewu_gv)
    GridView yewuGv;
    @BindView(R.id.scanning_et)
    EditText scanningEt;
    @BindView(R.id.scan_QR_img)
    ImageView scanQRImg;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.bg_ll)
    LinearLayout bgLl;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.nc_car_code_tv)
    TextView ncCarCodeTv;
    @BindView(R.id.nc_peizai_btn)
    TextView ncPeizaiBtn;
    @BindView(R.id.order_num_ll)
    LinearLayout orderNumLl;
    @BindView(R.id.nc_gua)
    TextView ncGua;
    @BindView(R.id.gua_ll)
    LinearLayout guaLl;
    @BindView(R.id.nc_piaoshu_tv)
    TextView ncPiaoshuTv;
    @BindView(R.id.nc_jianshu_tv)
    TextView ncJianshuTv;
    @BindView(R.id.ll_place)
    LinearLayout llPlace;
    @BindView(R.id.nc_fee_tv)
    TextView ncFeeTv;
    @BindView(R.id.pin_ll)
    LinearLayout pinLl;
    @BindView(R.id.nc_reconmend_tv)
    TextView ncReconmendTv;
    @BindView(R.id.time_ll)
    LinearLayout timeLl;
    @BindView(R.id.root_ll)
    LinearLayout rootLl;
    @BindView(R.id.ns_type_btn)
    Button nsTypeBtn;
    @BindView(R.id.ns_license_tv)
    TextView nsLicenseTv;
    @BindView(R.id.ns_line_btn)
    Button nsLineBtn;
    @BindView(R.id.ns_route_tv)
    TextView nsRouteTv;
    @BindView(R.id.ns_gua)
    TextView nsGua;
    @BindView(R.id.ns_scheduleDate_tv)
    TextView nsScheduleDateTv;
    @BindView(R.id.layout_ll)
    LinearLayout layoutLl;
    @BindView(R.id.yewu_ll)
    LinearLayout yewuLl;
    @BindView(R.id.nc_zhengche_btn)
    TextView ncZhengcheBtn;
    @BindView(R.id.nc_empty)
    TextView ncEmpty;
    @BindView(R.id.layout_nc)
    LinearLayout layoutNc;
    @BindView(R.id.ns_ll)
    LinearLayout nsLl;
    @BindView(R.id.ns_empty)
    TextView nsEmpty;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;

    private DriverHomeFragmentYeWuAdapter yeWuAdapter;
    private DriverHomeFragmentYeWuAdapter dingweiAdapter;
    private DriverHomeFragmentListViewAdapter listViewAdapter;

    private GetAppLoginResponse loginData;

    private ArrayList<mMainService> locationList;
    private ArrayList<mMainService> businessList;
    private String bengTime;
    private String endsTime;
    private TaskBean bean;

    /*********************
     * 广告字段以及变量
     *************************/

    private ViewFlow mViewFlow;
    private CircleFlowIndicator mFlowIndicator;
    private ArrayList<String> imageUrlList = new ArrayList<String>();
    private ArrayList<String> linkUrlArray = new ArrayList<String>();
    private ArrayList<String> titleList = new ArrayList<String>();
    private LinearLayout notice_parent_ll;
    private LinearLayout notice_ll;
    private ViewFlipper notice_vf;
    private int mCurrPos;

    /*************
     * END
     *************/

    public static DriverHomeFragment getInstance() {
        return new DriverHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginData = MyApplication.getInstance().getLoginInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_home, container, false);
        ButterKnife.bind(this, view);

        initView();
        loadDataRequest();
        getSchedulingList(DateUtil.getLastNDays(-1), DateUtil.getLastNDays(-1), false);
        initBannerView(view);
        initData();
        return view;
    }

    private void initView() {
        String length = SharedPreferencesTool.getString(SharedPreferencesTool.QUERY_LENGTH, "6");
        scanningEt.setHint(String.format("请输入%1$s位查询码", length));

        pullToRefreshSv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                loadDataRequest();
                getSchedulingList(DateUtil.getLastNDays(-1), DateUtil.getLastNDays(-1), false);
//                new GetDataTask().execute();


            }
        });

        locationList = getLocationList();
//        businessList = getBusinessList();

        if (locationList == null && businessList == null) {
            return;
        }

//        if (businessList != null && businessList.size() > 0) {
//            yewuLl.setVisibility(View.VISIBLE);
//        } else {
//            yewuLl.setVisibility(View.GONE);
//        }
//
//
//        if (locationList != null && locationList.size() > 0) {
//            locationLl.setVisibility(View.VISIBLE);
//        } else {
//            locationLl.setVisibility(View.GONE);
//        }

        yewuLl.setVisibility(View.VISIBLE);
        yeWuAdapter = new DriverHomeFragmentYeWuAdapter(locationList, getActivity());

        yewuGv.setAdapter(yeWuAdapter);
        yewuGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpActivity(locationList.get(position).ServiceType, locationList.get(position).ServiceID);
            }
        });


//        dingweiAdapter = new DriverHomeFragmentYeWuAdapter(getLocationList(), getActivity());
//
//        dingweiGv.setAdapter(dingweiAdapter);
//
//        dingweiGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                jumpActivity(locationList.get(position).ServiceType, locationList.get(position).ServiceID);
//            }
//        });

//        if (getMyList() != null && getMyList().size() > 0) {
//            contentLl.setVisibility(View.VISIBLE);
//            listViewAdapter = new DriverHomeFragmentListViewAdapter(getActivity(), getMyList());
//            renwuLv.setAdapter(listViewAdapter);
//        } else {
//            contentLl.setVisibility(View.GONE);
//        }


    }


    //获取业务list
    private ArrayList<mMainService> getBusinessList() {
        if (loginData == null) {
            return null;
        }

        List<mMainService> list = loginData.DataValue.MenuList;
        ArrayList<mMainService> loginDataList = new ArrayList<mMainService>();
        if (list != null) {
            //筛选
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).ServiceType.equals("1") && (!list.get(i).ServiceID.equals("12") && !list.get(i).ServiceID.equals("13") && !list.get(i).ServiceID.equals("14") && !list.get(i).ServiceID.equals("15"))) {
                    loginDataList.add(list.get(i));
                }
            }
        }

        return loginDataList;
    }

    //获取定位List
    private ArrayList<mMainService> getLocationList() {
        if (loginData == null) {
            return null;
        }

        List<mMainService> list = loginData.DataValue.MenuList;
        ArrayList<mMainService> loginDataList = new ArrayList<mMainService>();
        if (list != null) {
            //筛选
            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).ServiceType.equals("2")) {
                if ((!list.get(i).ServiceID.equals("12") && !list.get(i).ServiceID.equals("13") && !list.get(i).ServiceID.equals("14") && !list.get(i).ServiceID.equals("15"))) {
                    loginDataList.add(list.get(i));
                }

//                }
            }
        }

        return loginDataList;
    }


    private void jumpActivity(String serviceType, String serviceId) {
        LogUtil.d("-->> serviceTyp = " + serviceType + "||id=" + serviceId);
        if (Integer.parseInt(serviceType) == Constants.HOME_SERVICE_TYPE_BUSINISS) {
            switch (Integer.parseInt(serviceId)) {
                case Constants.HOME_BUSINISS_HD:
                    startActivity(new Intent(getActivity(), DriverQueryOrderActivity.class));
                    break;
                case Constants.HOME_BUSINISS_RW:
                    startActivity(new Intent(getActivity(), MyTaskActivity.class));
                    break;
            }


        } else {
            switch (Integer.parseInt(serviceId)) {
                case Constants.HOME_LOCATION_YS:
                    DriverTransportationActivity.startActivity(getActivity());
                    break;
                case Constants.HOME_LOCATION_CL:
                    startActivity(new Intent(getActivity(), DriverCarTreeListActivity.class));
                    break;
                case Constants.HOME_LOCATION_JK:
                    startActivity(new Intent(getActivity(), DriverSingleCarMonitorActivity.class));
                    break;
                case Constants.HOME_LOCATION_GJ:
                    startActivity(new Intent(getActivity(), DriverHistoricalTrackMainActivity.class));
                    break;
                case Constants.HOME_LOCATION_YC:
                    startActivity(new Intent(getActivity(), DriverCarExceptionUpLoadActivity.class));
                    break;


            }

        }


    }


//    private ArrayList<String> getMyList() {
//        ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < 25; i++) {
//            list.add("1605111277");
//
//        }
//        return null;
//    }

    /**
     * 获取新数据
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Toast.makeText(getActivity(), "PulltoReresh", Toast.LENGTH_LONG).show();
            pullToRefreshSv.onRefreshComplete();

            super.onPostExecute(result);
        }
    }


    //此方法需要添加 否则会在DriverMainActivity来回切换时出现重影
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CODE_SEARCH = 0x11;

    @OnClick({R.id.scanning_et, R.id.scan_QR_img, R.id.root_ll, R.id.layout_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanning_et:
                startActivity(new Intent(getActivity(), DriverQueryOrderActivity.class));
                break;
            case R.id.scan_QR_img:
//                startActivity(new Intent(getActivity(), ScanningActivity.class));
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.root_ll:
                Intent intent1;
                if (bean != null) {
                    if (!TextUtils.isEmpty(bean.Letter_Type_Oid) && bean.Letter_Type_Oid.equals("A")) {
                        intent1 = new Intent(getActivity(), MyTaskCarryDetailedActivity.class);
                    } else {
                        intent1 = new Intent(getActivity(), MyTaskDetailedActivity.class);
                    }

                    intent1.putExtra(Constants.TASKINFO, bean);
                    startActivity(intent1);
                }

                break;
            case R.id.layout_ll:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    if (loginData.DataValue.UserType_Oid.equals("E")) {
                        //货主跳到货单明细
                        Intent intent = new Intent(getActivity(), MyOrderDetailAcitivity.class);
                        intent.putExtra(MyOrderDetailAcitivity.ORDER_ID, Utils.createAllTicketNo(bundle.getString("result")));
                        startActivity(intent);
                    } else {
                        //司机和员工跳到货单详情
                        DriverHomeOrderDeatilActivity.startActivity(getActivity(), REQUEST_CODE_SEARCH, Utils.createAllTicketNo(bundle.getString("result")), null);
                    }
                }
                break;
        }
    }


    /**
     * 获取最近未完成的任务
     */

    private void loadDataRequest() {
        bengTime = DateUtil.strOldFormat2NewFormat(DateUtil.getLastNDays(-70), DateUtil.FORMAT_YMD, DateUtil.FORMAT_YMD_SN);
        endsTime = DateUtil.strOldFormat2NewFormat(DateUtil.getLastNDays(0), DateUtil.FORMAT_YMD, DateUtil.FORMAT_YMD_SN);

        DataRequestBase dataRequestBase = new DataRequestBase();
        GetTaskListRequest taskReqeustBean = new GetTaskListRequest();
//        taskReqeustBean.BeginDate = bengTime;
        taskReqeustBean.DeliverStatus = "2";
        taskReqeustBean.Driver_Tel = MyApplication.getInstance().getLoginInfo().DataValue.User_Tel;
//        taskReqeustBean.Driver_Tel = "66666";
//        taskReqeustBean.EndDate = endsTime;
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        dataRequestBase.Org_Name = MyApplication.getInstance().getLoginInfo().DataValue.Org_Name;
        dataRequestBase.Limit = 1;
        dataRequestBase.Page = 1;
        dataRequestBase.DataValue = taskReqeustBean; //JsonUtils.toJsonData(getDriverListRequest);
        LogUtil.d("-->>" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(TMSService.GetDetailedList_TAG, TMSService.GetDetailedList_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>" + response.toString());

                GetTaskListResponse response1 = JsonUtils.parseData(response.toString(), GetTaskListResponse.class);
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
                LogUtil.d("-->>" + error.toString());
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
            pullToRefreshSv.onRefreshComplete();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    contentLl.setVisibility(View.VISIBLE);
                    List<TaskBean> dataList = (List<TaskBean>) msg.obj;
                    if (dataList != null && dataList.size() > 0) {
                        layoutNc.setVisibility(View.VISIBLE);
                        ncEmpty.setVisibility(View.GONE);
                        bean = dataList.get(0);
                        ncCarCodeTv.setText(bean.CarCode);
                        if (!TextUtils.isEmpty(bean.Letter_Type_Oid) && bean.Letter_Type_Oid.equals("A")) {
                            ncZhengcheBtn.setVisibility(View.GONE);
                            ncPeizaiBtn.setVisibility(View.VISIBLE);
                        } else {
                            ncZhengcheBtn.setVisibility(View.VISIBLE);
                            ncPeizaiBtn.setVisibility(View.GONE);
                        }
                        ncGua.setText(bean.BoxCarCode);
                        ncPiaoshuTv.setText(bean.Ticket_Count);
                        ncJianshuTv.setText(bean.Goods_Num);
//                        ncCountTv.setText(bean.AllReceivableAccount);
                        ncFeeTv.setText(bean.Shuttle_Fee);
                        ncReconmendTv.setText(bean.Letter_Date);
                    } else {
                        layoutNc.setVisibility(View.GONE);
                        ncEmpty.setVisibility(View.VISIBLE);
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    contentLl.setVisibility(View.GONE);
                    ncEmpty.setVisibility(View.VISIBLE);
//                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;
                case Constants.REQUEST_ERROR:
                    contentLl.setVisibility(View.GONE);
                    ncEmpty.setVisibility(View.VISIBLE);
//                    DialogUtils.showMessageDialogOfDefaultSingleBtn(getActivity(), Utils.getResourcesString(R.string.request_error));
                    break;


            }
        }
    };

    /**
     * 获取未开始的任务
     *
     * @param beginDate
     * @param endDate
     * @param showDia
     */
    private void getSchedulingList(String beginDate, String endDate, boolean showDia) {

        DataRequestBase dataRequestBase = new DataRequestBase();
        GetDriverNoStartTaskRequest request = new GetDriverNoStartTaskRequest();

        request.Driver_Tel = MyApplication.getInstance().getLoginInfo().DataValue.User_Tel;
//        request.Driver_Tel = "66666";

        dataRequestBase.DataValue = request;
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        dataRequestBase.Org_Name = MyApplication.getInstance().getLoginInfo().DataValue.Org_Name;
        dataRequestBase.Limit = 1;
        dataRequestBase.Page = 1;

        LogUtil.d("-->>Last request=" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(TMSService.GetDriverNoStartTask_TAG, TMSService.GetDriverNoStartTask_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>Last respon=" + response.toString());
                GetDriverNoStartTaskResponse getShiftListResponse = GsonUtil.newInstance().fromJson(response, GetDriverNoStartTaskResponse.class);
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
            pullToRefreshSv.onRefreshComplete();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    List<GetDriverNoStartTask> list = (List<GetDriverNoStartTask>) msg.obj;
                    if (list != null && list.size() > 0) {
                        GetDriverNoStartTask info = list.get(0);
                        nsLicenseTv.setText(info.CarCode);
                        if (info.Line_Type.equals("干线")) {
                            nsLineBtn.setBackgroundResource(R.drawable.shape_rounded_btn_f56164);
                            nsLineBtn.setText("干");
                        } else {
                            nsLineBtn.setBackgroundResource(R.drawable.shape_rounded_btn_8e61d2);
                            nsLineBtn.setText("支");
                        }

                        nsRouteTv.setText(info.Line_Name);
                        nsGua.setText(info.CarCode);
                        nsScheduleDateTv.setText(info.Forecast_Leave_DateTime);

                        nsLl.setVisibility(View.VISIBLE);
                        nsEmpty.setVisibility(View.GONE);

                    } else {
                        nsLl.setVisibility(View.GONE);
                        nsEmpty.setVisibility(View.VISIBLE);
                    }

                    break;
                case Constants.REQUEST_FAIL:
//                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.get_data_fail));
                    nsLl.setVisibility(View.GONE);
                    nsEmpty.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    /**************
     * 轮播图广告
     *****************/
    private void initBannerView(View view) {
        mViewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        mFlowIndicator = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);

    }

    private void initData() {
        List<mImageBanner> list = loginData.DataValue.AdvertImgList;
        if (list != null) {
            for (mImageBanner imageBanner : list) {
                imageUrlList.add(imageBanner.ImgUrl);
                linkUrlArray.add(imageBanner.ImgAdvert);
            }
        }else {
            imageUrlList.add("http://www.cvnavi.com/");
            imageUrlList.add("http://www.cvnavi.com/");
            imageUrlList.add("http://www.cvnavi.com/");

            linkUrlArray.add("http://www.cvnavi.com/");
            linkUrlArray.add("http://www.cvnavi.com/");
            linkUrlArray.add("http://www.cvnavi.com/");
        }
        initBanner(imageUrlList);
//        initRollNotice();
    }

//    private void initRollNotice() {
//        FrameLayout main_notice = (FrameLayout) findViewById(R.id.main_notice);
//        notice_parent_ll = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_notice, null);
//        notice_ll = ((LinearLayout) this.notice_parent_ll.findViewById(R.id.homepage_notice_ll));
//        notice_vf = ((ViewFlipper) this.notice_parent_ll.findViewById(R.id.homepage_notice_vf));
//        main_notice.addView(notice_parent_ll);
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        moveNext();
//                        Log.d("Task", "下一个");
//                    }
//                });
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 0, 4000);
//    }

    private void moveNext() {
        setView(this.mCurrPos, this.mCurrPos + 1);
        this.notice_vf.setInAnimation(getActivity(), R.anim.in_bottomtop);
        this.notice_vf.setOutAnimation(getActivity(), R.anim.out_bottomtop);
        this.notice_vf.showNext();
    }

    private void setView(int curr, int next) {

        View noticeView = getActivity().getLayoutInflater().inflate(R.layout.notice_item, null);
        TextView notice_tv = (TextView) noticeView.findViewById(R.id.notice_tv);
        if ((curr < next) && (next > (titleList.size() - 1))) {
            next = 0;
        } else if ((curr > next) && (next < 0)) {
            next = titleList.size() - 1;
        }
        notice_tv.setText(titleList.get(next));
        notice_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putString("url", linkUrlArray.get(mCurrPos));
                bundle.putString("title", titleList.get(mCurrPos));
                Intent intent = new Intent(getActivity(), BaseWebActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (notice_vf.getChildCount() > 1) {
            notice_vf.removeViewAt(0);
        }
        notice_vf.addView(noticeView, notice_vf.getChildCount());
        mCurrPos = next;

    }

    private void initBanner(ArrayList<String> imageUrlList) {

        mViewFlow.setAdapter(new ImagePagerAdapter(getActivity(), imageUrlList, linkUrlArray, titleList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
        // 我的ImageAdapter实际图片张数为3

        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(Constants.BANNER_TIME);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }
    /**************轮播图广告END*****************/

}
