package com.cvnavi.logistics.i51eyun.app.activity.employee.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.home.myorder.MyOrderDetailAcitivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.DriverHomeFragmentYeWuAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverCarTreeListActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverCarsStatisActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverHistoricalTrackMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverSingleCarMonitorActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.location.DriverTransportationListActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.MyFleetActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverHomeOrderDeatilActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder.DriverQueryOrderActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.scanning.MipcaActivityCapture;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverManagerActivity;
import com.cvnavi.logistics.i51eyun.app.activity.employee.home.statics.StatisticsActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mImageBanner;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SharedPreferencesTool;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.BaseWebActivity;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.CircleFlowIndicator;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ImagePagerAdapter;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ViewFlow;
import com.cvnavi.logistics.i51eyun.app.widget.gridview.MyGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeHomeFragment extends BaseFragment {

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CODE_SEARCH = 0x11;
    @BindView(R.id.yewu_gv)
    MyGridView yewuGv;
    @BindView(R.id.yewu_ll)
    LinearLayout yewuLl;
    @BindView(R.id.pullToRefresh_sv)
    PullToRefreshScrollView pullToRefreshSv;
    @BindView(R.id.scanning_et)
    EditText scanningEt;
    @BindView(R.id.scan_QR_img)
    ImageView scanQRImg;
    @BindView(R.id.bg_ll)
    LinearLayout bgLl;

    private DriverHomeFragmentYeWuAdapter yeWuAdapter;
    private DriverHomeFragmentYeWuAdapter dingweiAdapter;
    private GetAppLoginResponse loginData;

    private ArrayList<mMainService> locationList;
    private ArrayList<mMainService> businessList;

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


    public static EmployeeHomeFragment instantiation() {
        return new EmployeeHomeFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginData = MyApplication.getInstance().getLoginInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        initBannerView(view);
        initData();
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    private void initView() {
//        titleTv.setText(R.string.cargo_home_name);
        String length = SharedPreferencesTool.getString(SharedPreferencesTool.QUERY_LENGTH, "6");
        scanningEt.setHint(String.format("请输入%1$s位查询码", length));

        pullToRefreshSv.setPullToRefreshOverScrollEnabled(false);
        pullToRefreshSv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 请求网络
                new GetDataTask().execute();
            }
        });


        locationList = getLocationList();

        yewuLl.setVisibility(View.VISIBLE);
        yeWuAdapter = new DriverHomeFragmentYeWuAdapter(locationList, getActivity());

        yewuGv.setAdapter(yeWuAdapter);

        yewuGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpActivity(locationList.get(position).ServiceType, locationList.get(position).ServiceID);
            }
        });

    }


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

    /**
     * 获取新数据
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            Toast.makeText(getActivity(), "PulltoReresh", Toast.LENGTH_LONG).show();
            pullToRefreshSv.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private void jumpActivity(String serviceType, String serviceId) {
        LogUtil.d("-->> serviceTyp = " + serviceType + "id=" + serviceId);
        if (Integer.parseInt(serviceType) == Constants.HOME_SERVICE_TYPE_BUSINISS) {
            switch (Integer.parseInt(serviceId)) {
                case Constants.HOME_BUSINISS_HD:
                    startActivity(new Intent(getActivity(), DriverQueryOrderActivity.class));
                    break;
                case Constants.HOME_BUSINISS_SJG:
                    showActivity(getActivity(), DriverManagerActivity.class);
                    break;
                case Constants.HOME_BUSINISS_TJ:
                    showActivity(getActivity(), StatisticsActivity.class);
                    break;
                case Constants.HOME_BUSINISS_CD:
                    showActivity(getActivity(), MyFleetActivity.class);
                    break;
            }

        } else {
            switch (Integer.parseInt(serviceId)) {
                case Constants.HOME_LOCATION_YS:
                    startActivity(new Intent(getActivity(), DriverTransportationListActivity.class));
                    break;
                case Constants.HOME_LOCATION_CL:
                    Intent intent = new Intent(getActivity(), DriverCarTreeListActivity.class);
                    intent.putExtra(Constants.HOME, Constants.HOME);
                    startActivity(intent);
                    break;
                case Constants.HOME_LOCATION_JK:
                    startActivity(new Intent(getActivity(), DriverSingleCarMonitorActivity.class));
                    break;
                case Constants.HOME_LOCATION_GJ:
                    startActivity(new Intent(getActivity(), DriverHistoricalTrackMainActivity.class));
                    break;
                case Constants.HOME_LOCATION_CLT:
                    startActivity(new Intent(getActivity(), DriverCarsStatisActivity.class));
                    break;

            }

        }


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
            }
        }

        return loginDataList;
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

    /*******/
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

}
