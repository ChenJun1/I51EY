package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetmonitor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.MyFleetCarTreeListActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.MyFleetMileStatisticActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.MyFleetRecordActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetalarminfo.MyFleetAlarmInfoActivity;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetMyCarFleetResponse;
import com.cvnavi.logistics.i51eyun.app.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${ChenJ} on 2016/8/8.
 */
public class MyFleetMonitorMapActivity extends BaseActivity implements BaiduMap.OnMapLoadedCallback {
    private static final String TAG = "MyFleetMonitorMapActivity";
    private static final String KEY_ORGLIST = "ORGLIST";
    private static final int KEY_Reques = 1000;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.monitoring_map)
    MapView monitoringMap;
    @BindView(R.id.CarCode_tv)
    TextView CarCodeTv;
    @BindView(R.id.car_status_tv)
    TextView carStatusTv;
    @BindView(R.id.addr_tv)
    TextView addrTv;
    @BindView(R.id.driver)
    TextView driver;
    @BindView(R.id.driver_name_tv)
    TextView driverNameTv;
    @BindView(R.id.driver_tel_tv)
    TextView driverTelTv;
    @BindView(R.id.alarm_ll)
    LinearLayout alarmLl;
    @BindView(R.id.mileage_ll)
    LinearLayout mileageLl;
    @BindView(R.id.locus_ll)
    LinearLayout locusLl;
    @BindView(R.id.operation_btn)
    Button operationBtn;
    @BindView(R.id.layout_ll)
    LinearLayout layoutLl;

    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem> mClusterManager;
    private SweetAlertDialog lodingDialog;


    private List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean> list;
    private List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean> dataList;
    private GetMyCarFleetResponse.DataValueBean.MCarInfoListBean bean;

    public static void start(Context context, List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean> list) {
        Intent starter = new Intent(context, MyFleetMonitorMapActivity.class);
        starter.putExtra(KEY_ORGLIST, (Serializable) list);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_monitor);
        ButterKnife.bind(this);

        list = new ArrayList<>();
        dataList = new ArrayList<>();
        if (getIntent().getSerializableExtra(KEY_ORGLIST) != null) {
            List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean> data = (List<GetMyCarFleetResponse.DataValueBean.MCarInfoListBean>) getIntent().getSerializableExtra(KEY_ORGLIST);
            if (data != null) {
                list.addAll(data);
                dataList.addAll(data);
            }
        }
        init();


    }

    private void init() {
        titleTv.setText(Utils.getResourcesString(R.string.fleet_monitor));
        operationBtn.setVisibility(View.VISIBLE);
        operationBtn.setText(Utils.getResourcesString(R.string.fleet_chooce_list));
        lodingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        if (dataList != null && dataList.size() > 0) {
            for (int i = dataList.size() - 1; i >= 0; i--) {
                if (!TextUtils.isEmpty(dataList.get(i).getBLat()) && !TextUtils.isEmpty(dataList.get(i).getBLng())) {
                    ms = new MapStatus.Builder().target(new LatLng(Double.valueOf(dataList.get(i).getBLat()), Double.valueOf(dataList.get(i).getBLng()))).zoom(8).build();
                    break;
                }
            }
        } else {
            DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetMonitorMapActivity.this, Utils.getResourcesString(R.string.not_monitor_info), new CustomDialogListener() {

                @Override
                public void onDialogClosed(int closeType) {
                    if (closeType == CustomDialogListener.BUTTON_OK) {
                       MyFleetMonitorMapActivity.this.finish();
                    }
                }
            });

        }
        initMap();
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                String lat = String.valueOf(marker.getPosition().latitude);
//                String lng = String.valueOf(marker.getPosition().longitude);
//                for (GetMyCarFleetResponse.DataValueBean.MCarInfoListBean mCarInfoListBean : list) {
//                    if (!TextUtils.isEmpty(mCarInfoListBean.getBLat()) && !TextUtils.isEmpty(mCarInfoListBean.getBLng())) {
//                        if (lat.equals(mCarInfoListBean.getBLat()) && lng.equals(mCarInfoListBean.getBLng())) {
//                            bean = mCarInfoListBean;
//                            SetViewData(mCarInfoListBean);
//                            layoutLl.setVisibility(View.VISIBLE);
//                            showStop(marker.getPosition(), mCarInfoListBean.getCarCode());
//                        }
//                    }
//                }
//
//                return false;
//            }
//        });
    }

    /**
     * 0=行驶中
     * 1=停止
     * 2=信号中断
     *
     * @param mCarInfoListBean
     */
    private void SetViewData(GetMyCarFleetResponse.DataValueBean.MCarInfoListBean mCarInfoListBean) {
        String CarStatus = "";
        if (!TextUtils.isEmpty(mCarInfoListBean.getCarStatus())) {
            switch (mCarInfoListBean.getCarStatus()) {
                case "0":
                    CarStatus = "行驶中";
                    break;
                case "1":
                    CarStatus = "停止";
                    break;
                case "2":
                    CarStatus = "信号中断";
                    break;

            }
        }
        SetViewValueUtil.setTextViewValue(CarCodeTv, mCarInfoListBean.getCarCode());
        SetViewValueUtil.setTextViewValue(carStatusTv, CarStatus);
        SetViewValueUtil.setTextViewValue(addrTv, mCarInfoListBean.getCHS_Address());
        SetViewValueUtil.setTextViewValue(driverNameTv, mCarInfoListBean.getDriver());
        SetViewValueUtil.setTextViewValue(driverTelTv, mCarInfoListBean.getDriver_Tel());

    }

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        List<MyItem> items = new ArrayList<MyItem>();
        for (GetMyCarFleetResponse.DataValueBean.MCarInfoListBean mCarInfoListBean : list) {
            if (!TextUtils.isEmpty(mCarInfoListBean.getBLat()) && !TextUtils.isEmpty(mCarInfoListBean.getBLng())) {
                LatLng llA = new LatLng(Double.valueOf(mCarInfoListBean.getBLat()), Double.valueOf(mCarInfoListBean.getBLng()));
                items.add(new MyItem(llA));
            }
        }
//        // 添加Marker点
//        LatLng llA = new LatLng(39.963175, 116.400244);
//        LatLng llB = new LatLng(39.942821, 116.369199);
//        LatLng llC = new LatLng(39.939723, 116.425541);
//        LatLng llD = new LatLng(39.906965, 116.401394);
//        LatLng llE = new LatLng(39.956965, 116.331394);
//        LatLng llF = new LatLng(39.886965, 116.441394);
//        LatLng llG = new LatLng(39.986965, 116.411394);
//        LatLng llh = new LatLng(39.9796965, 116.380394);
//        LatLng llj = new LatLng(39.966965, 116.370394);
//        LatLng llk = new LatLng(39.906965, 116.360394);
//
//
//        items.add(new MyItem(llA));
//        items.add(new MyItem(llB));
//        items.add(new MyItem(llC));
//        items.add(new MyItem(llD));
//        items.add(new MyItem(llE));
//        items.add(new MyItem(llF));
//        items.add(new MyItem(llG));
//        items.add(new MyItem(llh));
//        items.add(new MyItem(llj));
//        items.add(new MyItem(llk));

        mClusterManager.addItems(items);

    }


    private void showStop(LatLng mLatLng, String Carcode) {

        View popu = View.inflate(this, R.layout.pop_myfleet_monitor, null);
        TextView title = (TextView) popu.findViewById(R.id.showpop_title_text);


        title.setText(Carcode.replaceAll("；", "；\n　　　"));
        title.setTextColor(Color.BLACK);

        InfoWindow.OnInfoWindowClickListener mListener = new InfoWindow.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick() {
                mBaiduMap.hideInfoWindow();

            }
        };

        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(popu), mLatLng, -80, mListener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    @OnClick({R.id.back_llayout, R.id.driver_tel_tv, R.id.alarm_ll, R.id.mileage_ll, R.id.locus_ll, R.id.operation_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.driver_tel_tv:
                ContextUtil.callAlertDialog(driverTelTv.getText().toString(), MyFleetMonitorMapActivity.this);
                break;
            case R.id.alarm_ll:
                if (bean != null) {
                    MyFleetAlarmInfoActivity.start(this, bean.getCarCode_Key(), bean.getCarCode());
                }
                break;
            case R.id.mileage_ll:
                if (bean != null) {
                    MyFleetMileStatisticActivity.startActivity(this, bean.getCarCode_Key(), bean.getCarCode());
                }
                break;
            case R.id.locus_ll:
                if (bean != null) {
                    MyFleetRecordActivity.start(this, bean.getCarCode_Key());
                }
                break;
            case R.id.operation_btn:
                MyFleetCarTreeListActivity.startActivity(this, KEY_Reques);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case KEY_Reques:
                    Bundle bundle = data.getExtras();
                    String OrgCode = bundle.getString("Org_Code");
                    referceDataList(OrgCode);
                    break;
            }
        }
    }

    private void referceDataList(String orgCode) {
        list.clear();
        mBaiduMap.clear();
        layoutLl.setVisibility(View.GONE);
        for (GetMyCarFleetResponse.DataValueBean.MCarInfoListBean mCarInfoListBean : dataList) {
            if (!TextUtils.isEmpty(mCarInfoListBean.getOrg_Code()) && mCarInfoListBean.getOrg_Code().equals(orgCode)) {
                list.add(mCarInfoListBean);
            }
        }
        if (list.size() == 0) {
            DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyFleetMonitorMapActivity.this, Utils.getResourcesString(R.string.not_monitor_info), new CustomDialogListener() {

                @Override
                public void onDialogClosed(int closeType) {
                    if (closeType == CustomDialogListener.BUTTON_OK) {
//                        MyFleetMonitorMapActivity.this.finish();
                    }
                }
            });
        }else{
            for (int i = list.size() - 1; i >= 0; i--) {
                if (!TextUtils.isEmpty(list.get(i).getBLat()) && !TextUtils.isEmpty(list.get(i).getBLng())) {
                    ms = new MapStatus.Builder().target(new LatLng(Double.valueOf(list.get(i).getBLat()), Double.valueOf(list.get(i).getBLng()))).zoom(8).build();
                    break;
                }
            }
        }
        initMap();
    }

    private void initMap() {
        mBaiduMap = monitoringMap.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.getMarkerCollection().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String lat = String.valueOf(marker.getPosition().latitude);
                String lng = String.valueOf(marker.getPosition().longitude);
                for (GetMyCarFleetResponse.DataValueBean.MCarInfoListBean mCarInfoListBean : list) {
                    if (!TextUtils.isEmpty(mCarInfoListBean.getBLat()) && !TextUtils.isEmpty(mCarInfoListBean.getBLng())) {
                        if (lat.equals(mCarInfoListBean.getBLat()) && lng.equals(mCarInfoListBean.getBLng())) {
                            bean = mCarInfoListBean;
                            SetViewData(mCarInfoListBean);
                            layoutLl.setVisibility(View.VISIBLE);
                            showStop(marker.getPosition(), mCarInfoListBean.getCarCode());
                        }
                    }
                }

                return false;
            }
        });
        mClusterManager.getClusterMarkerCollection().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

//                DialogUtils.showNormalToast(mClusterManager.getClusterMarkerCollection().getMarkers().size()+"");
                return false;
            }
        });

    }


    @Override
    protected void onPause() {
        monitoringMap.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        monitoringMap.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        monitoringMap.onDestroy();
        super.onDestroy();
    }


    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(LatLng latLng) {
            mPosition = latLng;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory.fromResource(R.drawable.point_location);
        }


    }
}
