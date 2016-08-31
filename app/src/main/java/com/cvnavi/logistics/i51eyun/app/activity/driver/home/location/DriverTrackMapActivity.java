package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocus;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocusAnalysis;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocusTwo;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetHistoryLocusRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetHistoryLocusResponseTwo;
import com.cvnavi.logistics.i51eyun.app.config.LPSService;
import com.cvnavi.logistics.i51eyun.app.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * Created by fan on 2016/7/8.
 */
public class DriverTrackMapActivity extends BaseActivity {

    private final String TAG = DriverTrackMapActivity.class.getName();

    @BindView(R.id.back_llayout)
    LinearLayout backLinearLayout;
    @BindView(R.id.title_tv)
    TextView titltTextView;
    @BindView(R.id.monitoring_map)
    MapView monitoringMap;
    @BindView(R.id.car_code_tx)
    TextView carCodeTx;
    @BindView(R.id.car_address_tx)
    TextView carAddressTx;
    @BindView(R.id.driver_name_tx)
    TextView driverNameTx;
    @BindView(R.id.driver_tel_tx)
    TextView driverTelTx;

    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    // 经纬度
    private Double longitude = 0.0;
    private Double latitude = 0.0;

    private Boolean isStart = true;
    private Boolean isStartTwo = true;

    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    boolean mapBool = false;

    //地图缩放比例
    private float zoom=12.f;

    // 地图大头针
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_nav_start);
    BitmapDescriptor azure = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_nav_end);

    BitmapDescriptor stop = BitmapDescriptorFactory
            .fromResource(R.drawable.stopcar);

    BitmapDescriptor startcar = BitmapDescriptorFactory
            .fromResource(R.drawable.startcar);

    // 覆盖物 坐标
    private List<MarkerOptions> listMarker;

    ArrayList<mCarHistoryLocus> locyList;
    public static ArrayList<mCarHistoryLocusTwo> locyListTwo;
    ArrayList<mCarHistoryLocusAnalysis> locyAnalysisList;

    private mCarHistoryLocus carControlBean;
    private mCarHistoryLocusAnalysis analysis;
    private mCarHistoryLocusAnalysis carHistoryLocusBean;

    private Intent mIntent;
    private String carCodeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_track_map);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        titltTextView.setText("轨迹地图");
        locyListTwo = new ArrayList<>();
        locyList = DriverRecordDetailFragment.mList;
        locyAnalysisList = DriverRecordAnalysisFragment.mList;

        initMap();

        mIntent = getIntent();
        if (mIntent.getStringExtra("ActivityName").equals("DriverRecordAnalysisFragment")) {
            requestData();
            initOverlay(Color.RED, 9);
        } else if (mIntent.getStringExtra("ActivityName").equals("DriverRecordDetailFragment")) {
            zoom=15.f;
            requestDetaileLocation();
            initOverlay(Color.RED, 9);
        } else {
            stop(9);
            initOverlay(Color.RED, 9);
        }

    }


    //详情重新定位
    private void requestDetaileLocation() {
        carControlBean = new mCarHistoryLocus();
        carControlBean = (mCarHistoryLocus) mIntent.getSerializableExtra("Data");

        latitude=Double.valueOf(carControlBean.BLat);
        longitude=Double.valueOf(carControlBean.BLng);

        intiLocation();

        mBaiduMap.clear();// 先清理图层
        // 定义Maker坐标点
        LatLng point = new LatLng(Double.valueOf(carControlBean.BLat), Double.valueOf(carControlBean.BLng));
        // 构建MarkerOption，用于在地图上添加Marker
        MarkerOptions op = new MarkerOptions().position(point).icon(stop).title("carDetail");
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(op);

        ShowDetail(op.getPosition(), carControlBean.CarCode,
                carControlBean.Position_DateTime,
                carControlBean.CHS_Address,
                carControlBean.Mileage,
                carControlBean.Status_Oid);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.getTitle().equals("end")) {
                    showOne(marker.getPosition(),
                            locyList.get(locyList.size() - 1).Position_DateTime,
                            locyList.get(locyList.size() - 1).CHS_Address
                    );
                } else if (marker.getTitle().equals("isStart")) {
                    showOne(marker.getPosition(),
                            locyList.get(0).Position_DateTime,
                            locyList.get(0).CHS_Address
                    );
                } else if (marker.getTitle().equals("carDetail")) {
                    ShowDetail(marker.getPosition(), carControlBean.CarCode,
                            carControlBean.Position_DateTime,
                            carControlBean.CHS_Address,
                            carControlBean.Mileage,
                            carControlBean.Status_Oid);
                }

                return false;
            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }


    /**
     * 记录分析重新定位
     */
    private void requestLocation() {
        carHistoryLocusBean = new mCarHistoryLocusAnalysis();
        carHistoryLocusBean = (mCarHistoryLocusAnalysis) mIntent.getSerializableExtra("Data");

        if (carHistoryLocusBean != null) {
            if (carHistoryLocusBean.BLng != null && carHistoryLocusBean.BLat != null) {
                //        // 重新定位并以此坐标为中心点
                LatLng ll = new LatLng(Double.valueOf(carHistoryLocusBean.BLng), Double.valueOf(carHistoryLocusBean.BLat));
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory
//                .newMapStatus(builder.build()));
//        mLocClient.requestLocation();

//        mBaiduMap.clear();// 先清理图层
                // 定义Maker坐标点
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                if (carHistoryLocusBean.Status_Oid.equals("0")) {//行驶
                    MarkerOptions op = new MarkerOptions().position(point).icon(startcar).title("carLocation");
                    // 在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(op);
                } else if (carHistoryLocusBean.Status_Oid.equals("1")) {//停车
                    MarkerOptions op = new MarkerOptions().position(point).icon(stop).title("carLocation");
                    // 在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(op);
                }

                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {


                        if (marker.getTitle().equals("stopStart")) {
                            showOne(marker.getPosition(),
                                    locyListTwo.get(0).Position_DateTime,
                                    locyListTwo.get(0).CHS_Address
                            );
                        } else if (marker.getTitle().equals("stopEnd")) {
                            showOne(marker.getPosition(),
                                    locyListTwo.get(locyListTwo.size() - 1).Position_DateTime,
                                    locyListTwo.get(locyListTwo.size() - 1).CHS_Address
                            );
                        }
                        if (marker.getTitle().equals("end")) {
                            showOne(marker.getPosition(),
                                    locyList.get(locyList.size() - 1).Position_DateTime,
                                    locyList.get(locyList.size() - 1).CHS_Address
                            );
                        } else if (marker.getTitle().equals("isStart")) {
                            showOne(marker.getPosition(),
                                    locyList.get(0).Position_DateTime,
                                    locyList.get(0).CHS_Address
                            );
                        } else if (marker.getTitle().equals("carLocation")) {
                            showOne(marker.getPosition(), carHistoryLocusBean.Time_Description,
                                    carHistoryLocusBean.CHS_Address);
                        }

                        return false;
                    }
                });
            } else {
                DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(DriverTrackMapActivity.this, "未获取到数据", new CustomDialogListener() {
                    @Override
                    public void onDialogClosed(int closeType) {
                        if (closeType == CustomDialogListener.BUTTON_OK) {
                            finish();
                        }
                    }
                });
            }

        } else {
//            Toast.makeText(DriverTrackMapActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
        }

    }


    //详情信息展示
    private void ShowDetail(LatLng mLatLng, String CarCode, String Position_DateTime, String CHS_Address,
                            String Mileage, String Status_Oid) {

        View popu = View.inflate(this, R.layout.activity_fragment_2_showpop,
                null);
        TextView title = (TextView) popu.findViewById(R.id.showpop_title_text);

        if (Status_Oid != null) {
            if (Status_Oid.equals("1")) {

                Status_Oid = "停车";
            } else {
                Status_Oid = "行驶";
            }
        } else {
            Status_Oid = "暂无数据";
        }


        title.setText("当前时间：" + Position_DateTime + "\n"
                + "地址：" + CHS_Address.replaceAll("；", "；\n　　　")
        );

        InfoWindow.OnInfoWindowClickListener mListener = new InfoWindow.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick() {

                mBaiduMap.hideInfoWindow();

            }
        };

        title.setTextColor(Color.BLACK);
        InfoWindow mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(popu), mLatLng, -80, mListener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    //起始点终点信息展示
    private void showOne(LatLng mLatLng, String Position_DateTime, String CHS_Address) {

        View popu = View.inflate(this, R.layout.activity_fragment_2_showpop,
                null);

        TextView title = (TextView) popu.findViewById(R.id.showpop_title_text);


        title.setText("当前时间：" + Position_DateTime + "\n" + "地址：" + CHS_Address.replaceAll("；", "；\n　　　"));

        InfoWindow.OnInfoWindowClickListener mListener = new InfoWindow.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick() {

                mBaiduMap.hideInfoWindow();
            }
        };

        title.setTextColor(Color.BLACK);
        InfoWindow mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(popu), mLatLng, -80, mListener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    //停车点展示信息
    private void showStop(LatLng mLatLng, String CarCode, String Begin_DateTime, String End_DateTime, String Time_Description, String CHS_Address) {

        View popu = View.inflate(this, R.layout.activity_fragment_2_showpop,
                null);
        TextView title = (TextView) popu.findViewById(R.id.showpop_title_text);


        title.setText("停车时间：" + Time_Description + "\n" + "地址：" + CHS_Address.replaceAll("；", "；\n　　　"));
        title.setTextColor(Color.BLACK);

        InfoWindow.OnInfoWindowClickListener mListener = new InfoWindow.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick() {

                mBaiduMap.hideInfoWindow();
            }
        };

        InfoWindow mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(popu), mLatLng, -80, mListener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    /**
     * 初始化地图
     */
    private void initMap() {
        mBaiduMap = monitoringMap.getMap();
        listMarker = new ArrayList<>();
        /*
         * MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		 * mBaiduMap.setMapStatus(msu);
		 */

        int childCount = monitoringMap.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = monitoringMap.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        if (zoom != null) {
            zoom.setVisibility(View.GONE);
        }
        monitoringMap.removeViewAt(1);


    }


    //停车点标注
    private void stop(int zIndex) {


        listMarker.clear();
        if (locyAnalysisList.size() > 1) {
            List<LatLng> pointss = new ArrayList<>();
            List<LatLng> locatPoints = new ArrayList<>();
            for (int i = 0; i < locyAnalysisList.size(); i++) {
                analysis = locyAnalysisList.get(i);
                if (!TextUtils.isEmpty(analysis.Status_Oid)) {

                    if (analysis.BLat != null && analysis.BLng != null) {
                        double BLat = Double.valueOf(analysis.BLat);
                        double BLng = Double.valueOf(analysis.BLng);
                        LatLng llA = new LatLng(BLat, BLng);
                        pointss.add(llA);

                        if (analysis.Status_Oid.equals("1")) {

                            double BLat2 = Double.valueOf(analysis.BLat);
                            double BLng2 = Double.valueOf(analysis.BLng);

                            LatLng llA2 = new LatLng(BLat2, BLng2);
                            locatPoints.add(llA2);
                            MarkerOptions moo = new MarkerOptions().title(String.valueOf(i)).position(llA2).icon(stop).zIndex(zIndex);
                            listMarker.add(moo);


                        }
                    } else {
                        DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(DriverTrackMapActivity.this, "未获取到数据", new CustomDialogListener() {
                            @Override
                            public void onDialogClosed(int closeType) {
                                if (closeType == CustomDialogListener.BUTTON_OK) {
                                    finish();
                                }
                            }
                        });
                    }

                }


            }

            for (int i = 0; i < listMarker.size(); i++) {
                mBaiduMap.addOverlay(listMarker.get(i));
            }

            if (!TextUtils.isEmpty(locyAnalysisList.get(0).BLng) && !TextUtils.isEmpty(locyAnalysisList.get(0).BLat)) {

                longitude = Double.valueOf(locyAnalysisList.get(0).BLng);
                latitude = Double.valueOf(locyAnalysisList.get(0).BLat);
                intiLocation();
            }
                mBaiduMap.setMyLocationEnabled(false);

        }else if (locyAnalysisList.size() == 1) {

            if (!TextUtils.isEmpty(locyAnalysisList.get(0).BLng) && !TextUtils.isEmpty(locyAnalysisList.get(0).BLat)) {

                longitude = Double.valueOf(locyAnalysisList.get(0).BLng);
                latitude = Double.valueOf(locyAnalysisList.get(0).BLat);
                intiLocation();
            }
        }


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.getTitle().equals("end")) {
                    showOne(marker.getPosition(),
                            locyList.get(locyList.size() - 1).Position_DateTime,
                            locyList.get(locyList.size() - 1).CHS_Address
                    );
                } else if (marker.getTitle().equals("isStart")) {
                    showOne(marker.getPosition(),
                            locyList.get(0).Position_DateTime,
                            locyList.get(0).CHS_Address
                    );
                } else {
                    showStop(marker.getPosition(), locyAnalysisList.get(Integer.valueOf(marker.getTitle())).CarCode,
                            locyAnalysisList.get(Integer.valueOf(marker.getTitle())).Begin_DateTime,
                            locyAnalysisList.get(Integer.valueOf(marker.getTitle())).End_DateTime,
                            locyAnalysisList.get(Integer.valueOf(marker.getTitle())).Time_Description,
                            locyAnalysisList.get(Integer.valueOf(marker.getTitle())).CHS_Address
                    );
                }
                return false;
            }
        });


    }


    /**
     * 初始化轨迹
     */
    public void initOverlay(int color, int zIndex) {
        listMarker.clear();
        if (locyList.size() > 1) {
            List<LatLng> points = new ArrayList<>();
            List<LatLng> locatPoints = new ArrayList<>();
            for (int i = 0; i < locyList.size(); i++) {
                final mCarHistoryLocus car = locyList.get(i);

                if (!TextUtils.isEmpty(car.BLat) && !TextUtils.isEmpty(car.BLng)) {

                    double BLat = Double.valueOf(car.BLat);
                    double BLng = Double.valueOf(car.BLng);
                    LatLng llA = new LatLng(BLat, BLng);
                    points.add(llA);

                    if (isStart) {
                        isStart = false;
                        double BLat2 = Double.valueOf(locyList.get(i).BLat);
                        double BLng2 = Double.valueOf(locyList.get(i).BLng);

                        LatLng llA2 = new LatLng(BLat2, BLng2);
                        locatPoints.add(llA2);
                        MarkerOptions moo = new MarkerOptions().position(llA2).icon(bd).zIndex(zIndex).title("isStart");

                        listMarker.add(moo);


                    }

                    if (i == locyList.size() - 1) {
                        double BLat2 = Double.valueOf(locyList.get(i).BLat);
                        double BLng2 = Double.valueOf(locyList.get(i).BLng);

                        LatLng llA2 = new LatLng(BLat2, BLng2);
                        locatPoints.add(llA2);
                        MarkerOptions moo = new MarkerOptions().position(llA2).title("end").icon(azure).zIndex(zIndex);
                        listMarker.add(moo);


                    }
                }

            }

            for (int i = 0; i < listMarker.size(); i++) {
                mBaiduMap.addOverlay(listMarker.get(i));
            }
            OverlayOptions ooPolyline = null;
            if (points.size() > 0) {
                ooPolyline = new PolylineOptions().width(8).color(color)
                        .points(points);
            }
            if (ooPolyline != null) {
                mBaiduMap.addOverlay(ooPolyline);
                if (!TextUtils.isEmpty(locyList.get(0).BLng) && !TextUtils.isEmpty(locyList.get(0).BLat)) {

//                    longitude = Double.valueOf(locyList.get(0).BLng);
//                    latitude = Double.valueOf(locyList.get(0).BLat);
//                    intiLocation();
                }

            } else {
                Toast.makeText(getApplication(), "暂无数据", Toast.LENGTH_SHORT)
                        .show();
            }
            mBaiduMap.setMyLocationEnabled(false);
        } else if (locyList.size() == 1) {

            if (!TextUtils.isEmpty(locyList.get(0).BLng) && !TextUtils.isEmpty(locyList.get(0).BLat)) {

//                longitude = Double.valueOf(locyList.get(0).BLng);
//                latitude = Double.valueOf(locyList.get(0).BLat);
//                intiLocation();
            }
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || monitoringMap == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latitude).longitude(longitude)
                    .build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(latitude, longitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(zoom );
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * 初始化定位
     */
    public void intiLocation() {
        // 隐藏缩放控件
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mapBool = true;
    }


    @OnClick({R.id.back_llayout})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_llayout:
                ActivityStackManager.getInstance().finishActivity();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        if (monitoringMap != null) {
            monitoringMap.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        if (monitoringMap != null) {
            monitoringMap.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        if (monitoringMap != null) {
            monitoringMap.onDestroy();
        }
        if (mLocClient != null) {
            mLocClient.stop();
        }

        bd.recycle();
        azure.recycle();
        stop.recycle();
        startcar.recycle();
        // 回收 bitmap 资源
        super.onDestroy();
    }


    //记录分析划线请求数据
    private void requestData() {

        carHistoryLocusBean = new mCarHistoryLocusAnalysis();
        carHistoryLocusBean = (mCarHistoryLocusAnalysis) mIntent.getSerializableExtra("Data");
        carCodeKey = mIntent.getStringExtra("carCodeKey");
        DataRequestBase requestBase = new DataRequestBase();

        final GetHistoryLocusRequest request = new GetHistoryLocusRequest();
        request.StarTime = carHistoryLocusBean.Begin_DateTime;
        request.EndTime = carHistoryLocusBean.End_DateTime;
        request.KeyList = carCodeKey;

        requestBase.DataValue = JsonUtils.toJsonData(request);
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;

        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);
        VolleyManager.newInstance().PostJsonRequest(TAG, LPSService.GetHistoryLocus_Request_Url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        LogUtil.d("-->>onResponse============>>" + response.toString());
                        List<mCarHistoryLocusTwo> carLocusAnaly = null;

                        GetHistoryLocusResponseTwo analysisRequest = JsonUtils.parseData(response.toString(), GetHistoryLocusResponseTwo.class);
                        Message msg = Message.obtain();
                        if (analysisRequest.Success) {
                            carLocusAnaly = analysisRequest.DataValue;
                            msg.what = Constants.REQUEST_SUCC;
                            msg.obj = carLocusAnaly;
                            mHandler.sendMessage(msg);
                        } else {
                            msg.what = Constants.REQUEST_SUCC;
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
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    List<mCarHistoryLocusTwo> datalist = (List<mCarHistoryLocusTwo>) msg.obj;
                    locyListTwo.clear();
                    locyListTwo.addAll(datalist);

                    initOverlayTwo(Color.BLUE, 9);

                    requestLocation();

                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;

                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverTrackMapActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };


    /**
     * 行驶轨迹
     */
    public void initOverlayTwo(int color, int zIndex) {
        listMarker.clear();
        if (locyListTwo.size() > 1) {
            List<LatLng> points = new ArrayList<>();
            List<LatLng> locatPoints = new ArrayList<>();
            for (int i = 0; i < locyListTwo.size(); i++) {
                final mCarHistoryLocusTwo car = locyListTwo.get(i);

                if (!TextUtils.isEmpty(car.BLat) && !TextUtils.isEmpty(car.BLng)) {

                    double BLat = Double.valueOf(car.BLat);
                    double BLng = Double.valueOf(car.BLng);
                    LatLng llA = new LatLng(BLat, BLng);
                    points.add(llA);

                    if (carHistoryLocusBean.Status_Oid.equals("0")) {//行驶标注
                        if (isStartTwo) {
                            isStartTwo = false;
                            double BLat2 = Double.valueOf(locyListTwo.get(i).BLat);
                            double BLng2 = Double.valueOf(locyListTwo.get(i).BLng);

                            LatLng llA2 = new LatLng(BLat2, BLng2);
                            locatPoints.add(llA2);

                            MarkerOptions moo = new MarkerOptions().position(llA2).icon(startcar).title("stopStart").zIndex(zIndex);
                            listMarker.add(moo);

                        }

                        if (i == locyListTwo.size() - 1) {
                            double BLat2 = Double.valueOf(locyListTwo.get(i).BLat);
                            double BLng2 = Double.valueOf(locyListTwo.get(i).BLng);

                            LatLng llA2 = new LatLng(BLat2, BLng2);
                            locatPoints.add(llA2);
                            MarkerOptions moo = new MarkerOptions().position(llA2).title("stopEnd").icon(startcar).zIndex(zIndex);
                            listMarker.add(moo);
                        }
                    } else if (carHistoryLocusBean.Status_Oid.equals("1")) {//停车标注
                        if (isStartTwo) {
                            isStartTwo = false;
                            double BLat2 = Double.valueOf(locyListTwo.get(i).BLat);
                            double BLng2 = Double.valueOf(locyListTwo.get(i).BLng);

                            LatLng llA2 = new LatLng(BLat2, BLng2);
                            locatPoints.add(llA2);

                            MarkerOptions moo = new MarkerOptions().position(llA2).icon(stop).title("stopStart").zIndex(zIndex);
                            listMarker.add(moo);

                        }

                        if (i == locyListTwo.size() - 1) {
                            double BLat2 = Double.valueOf(locyListTwo.get(i).BLat);
                            double BLng2 = Double.valueOf(locyListTwo.get(i).BLng);

                            LatLng llA2 = new LatLng(BLat2, BLng2);
                            locatPoints.add(llA2);
                            MarkerOptions moo = new MarkerOptions().position(llA2).title("stopEnd").icon(stop).zIndex(zIndex);
                            listMarker.add(moo);
                        }
                    }


                }

            }


            for (int i = 0; i < listMarker.size(); i++) {
                mBaiduMap.addOverlay(listMarker.get(i));
            }
            OverlayOptions ooPolyline = null;
            if (points.size() > 0) {
                ooPolyline = new PolylineOptions().width(8).color(color)
                        .points(points);
            }
            if (ooPolyline != null) {
                mBaiduMap.addOverlay(ooPolyline);
                if (!TextUtils.isEmpty(locyListTwo.get(0).BLng) && !TextUtils.isEmpty(locyListTwo.get(0).BLat)) {

                    longitude = Double.valueOf(locyListTwo.get(0).BLng);
                    latitude = Double.valueOf(locyListTwo.get(0).BLat);
                    intiLocation();
                }

            } else {
                Toast.makeText(getApplication(), "暂无数据", Toast.LENGTH_SHORT)
                        .show();
            }
            mBaiduMap.setMyLocationEnabled(false);
        } else if (locyListTwo.size() == 1) {

            if (!TextUtils.isEmpty(locyListTwo.get(0).BLng) && !TextUtils.isEmpty(locyListTwo.get(0).BLat)) {

                longitude = Double.valueOf(locyListTwo.get(0).BLng);
                latitude = Double.valueOf(locyListTwo.get(0).BLat);
                intiLocation();
            }
        }
    }
}
