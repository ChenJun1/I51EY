package com.cvnavi.logistics.i51eyun.app.activity.cargo.home;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.adapter.CargoHomeFragmentListViewAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.home.myorder.MyOrderActivity;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.home.myorder.MyOrderDetailAcitivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.DriverHomeFragmentYeWuAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverCarTreeListActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverHistoricalTrackMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverSingleCarMonitorActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverHomeOrderDeatilActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverQueryOrderActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.scanning.MipcaActivityCapture;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverTransportationActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarShift;
import com.cvnavi.logistics.i51eyun.app.bean.model.mImageBanner;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;
import com.cvnavi.logistics.i51eyun.app.bean.model.mOrder;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetOrederListRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetOrederListResponse;
import com.cvnavi.logistics.i51eyun.app.callback.driver.home.trans.OnClickItemListener;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SharedPreferencesTool;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.BaseWebActivity;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.CircleFlowIndicator;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ImagePagerAdapter;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ViewFlow;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.gridview.MyGridView;
import com.cvnavi.logistics.i51eyun.app.widget.listview.MyListView;
import com.cvnavi.logistics.i51eyun.app.widget.myscrollview.MyScrollView;
import com.cvnavi.logistics.i51eyun.app.widget.myscrollview.ScrollViewExtend;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

public class CargoHomeFragment extends BaseFragment implements OnClickItemListener {

    @BindView(R.id.scan_QR_img)
    ImageView scanQRImg;
    @BindView(R.id.cargo_menu_gv)
    MyGridView cargoMenuGv;

    @BindView(R.id.pullToRefresh_sv)
    PullToRefreshScrollView pullToRefreshSv;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.scanning_et)
    EditText scanningEt;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.bg_ll)
    LinearLayout bgLl;
    @BindView(R.id.noinfo_tv)
    TextView noinfoTv;
    @BindView(R.id.unfinished_order_lv)
    MyListView unfinishedOrderLv;
    private DriverHomeFragmentYeWuAdapter yeWuAdapter;

    private GetAppLoginResponse loginData;
    private CargoHomeFragmentListViewAdapter adapter;
    private ArrayList<mMainService> businessList;
    private SweetAlertDialog loadingDialog = null;
    private DataRequestBase dataRequestBase = new DataRequestBase();
    private boolean isRefresh = false;
    private int pageInt = 1;
    private List<mOrder> dataList;
//    private MyOrderAdapter adapter = null;
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

    private View headView;


    public static CargoHomeFragment instantiation() {
        return new CargoHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginData = MyApplication.getInstance().getLoginInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargo_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        initBannerView(view);
        initData();
        return view;
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


//    private ArrayList<String> getMyList() {
//        ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < 25; i++) {
//            list.add("1605111277");
//
//        }
//        return list;
//    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        String length = SharedPreferencesTool.getString(SharedPreferencesTool.QUERY_LENGTH, "6");
        scanningEt.setHint(String.format("请输入%1$s位查询码", length));
        pullToRefreshSv.setMode(PullToRefreshBase.Mode.BOTH);
        dataList = new ArrayList<>();
        adapter = new CargoHomeFragmentListViewAdapter(getActivity(), dataList, this);
        unfinishedOrderLv.setAdapter(adapter);

        pullToRefreshSv.setPullToRefreshOverScrollEnabled(false);
        pullToRefreshSv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {


            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                isRefresh = true;
                pageInt = 1;
                dataRequestBase.Page = pageInt;

                //执行刷新
                getOrderList(DateUtil.getLastNDays(-7), DateUtil.getLastNDays(1), false, "1");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageInt++;
                dataRequestBase.Page = pageInt;
                //执行刷新
                getOrderList(DateUtil.getLastNDays(-7), DateUtil.getLastNDays(1), false, "1");
            }
        });

        businessList = getBusinessList();

        yeWuAdapter = new DriverHomeFragmentYeWuAdapter(getBusinessList(), getActivity());

        cargoMenuGv.setAdapter(yeWuAdapter);

        cargoMenuGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpActivity(businessList.get(position).ServiceType, businessList.get(position).ServiceID);
            }
        });


    }

    private void jumpActivity(String serviceType, String serviceId) {
        LogUtil.d("-->> serviceTyp = " + serviceType + "||id=" + serviceId);
        if (Integer.parseInt(serviceType) == Constants.HOME_SERVICE_TYPE_BUSINISS) {
            switch (Integer.parseInt(serviceId)) {
                case Constants.HOME_BUSINISS_HD:
                    startActivity(new Intent(getActivity(), DriverQueryOrderActivity.class));
                    break;
                case Constants.HOME_BUSINISS_HDE:
                    startActivity(new Intent(getActivity(), MyOrderActivity.class));
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


            }

        }


    }


    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CODE_SEARCH = 0x11;

    @OnClick({R.id.scanning_et, R.id.scan_QR_img})
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


    private void getOrderList(String beginDate, String endDate, boolean showDia, String type) {
        showDia(showDia);
        GetAppLoginResponse info = MyApplication.getInstance().getLoginInfo();
        GetOrederListRequest datavalue = new GetOrederListRequest();
        datavalue.BeginDate = beginDate;
        datavalue.EndDate = endDate;
        datavalue.DeliverStatus = type;

        if (info.DataValue.UserType_Oid.equals("E")) {
            //货主查询货主的
            datavalue.SendMan_Tel = info.DataValue.User_Tel;
        }

        if (info.DataValue.UserType_Oid.equals("G")) {
            //司机查询司机的
            datavalue.Driver_Tel = info.DataValue.User_Tel;

        }

        dataRequestBase.DataValue = datavalue;
        dataRequestBase.User_Key = info.DataValue.User_Key;
        dataRequestBase.UserType_Oid = info.DataValue.UserType_Oid;
        dataRequestBase.Token = info.DataValue.Token;
        dataRequestBase.Company_Oid = info.DataValue.Company_Oid;

        LogUtil.d("-->>request=" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(TMSService.GetOrederList_TAG, TMSService.GetOrederList_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->> respon=" + response.toString());

                GetOrederListResponse getShiftListResponse = GsonUtil.newInstance().fromJson(response, GetOrederListResponse.class);
                Message msg = Message.obtain();
                if (getShiftListResponse != null && getShiftListResponse.Success && getShiftListResponse.DataValue != null) {
                    msg.what = Constants.REQUEST_SUCC;
                    msg.obj = getShiftListResponse.DataValue;
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dimiss();
            pullToRefreshSv.onRefreshComplete();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    if (msg.obj != null) {
                        if (isRefresh) {
                            isRefresh = false;
                            dataList.clear();
                        }
                        List<mOrder> list = (List<mOrder>) msg.obj;
                        dataList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                    noinfoTv.setVisibility(View.GONE);
                    unfinishedOrderLv.setVisibility(View.VISIBLE);

                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.get_data_fail));
                    noinfoTv.setVisibility(View.VISIBLE);
                    unfinishedOrderLv.setVisibility(View.GONE);
                    break;
            }

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        isRefresh = true;
        getOrderList(DateUtil.getLastNDays(-7), DateUtil.getLastNDays(1), true, "1");

    }

    @Override
    public void onClick(int position) {
        LogUtil.d("-->> onClick");
        if (dataList != null && dataList.get(position) != null) {
            Intent intent = new Intent(getActivity(), MyOrderDetailAcitivity.class);
            intent.putExtra(MyOrderDetailAcitivity.ORDER_ID, dataList.get(position).All_Ticket_No);
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(mCarShift info, int position) {

    }

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
        } else {
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
