package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.MyFleetRecordActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarHistoryLocusAnalysis;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${ChenJ} on 2016/8/9.
 */
public class MyFleetLocusFragment extends BaseDelayFragment implements BaiduMap.OnMapLoadedCallback {
    @BindView(R.id.locus_map)
    MapView locusMap;


    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    // 经纬度
    private double longitude = 0.0;//经度
    private double latitude = 0.0;
    private MyFleetRecordActivity context;
    private List<mCarHistoryLocusAnalysis> list;
    // 地图大头针
    BitmapDescriptor start = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_nav_start);
    BitmapDescriptor end = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_nav_end);

    // 覆盖物 坐标
    private List<MarkerOptions> listMarker;

    MapStatus ms;

    public static MyFleetLocusFragment getInstance() {
        return new MyFleetLocusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fleet_locus, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        mBaiduMap = locusMap.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        context = (MyFleetRecordActivity) getActivity();
        list = new ArrayList<>();
        if (context.getLocusList() != null) {

            list.addAll(context.getLocusList());
        }
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i) != null && !TextUtils.isEmpty(list.get(i).BLng) && !TextUtils.isEmpty(list.get(i).BLat)) {

                    longitude = Double.valueOf(list.get(i).BLng);
                    latitude = Double.valueOf(list.get(i).BLat);

                    ms = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(9).build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    break;

                }

            }
            drawingLocus();
        } else {
            DialogUtils.showNormalToast("暂无轨迹信息！");
        }

        // intiLocation();
    }

    private void drawingLocus() {
        mBaiduMap.clear();
        List<LatLng> points = new ArrayList<LatLng>();
        listMarker = new ArrayList<>();
        Boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.isEmpty(list.get(i).BLat) && !TextUtils.isEmpty(list.get(i).BLng)) {

                double Blat = Double.valueOf(list.get(i).BLat);
                double Blnt = Double.valueOf(list.get(i).BLng);
                LatLng latlnt = new LatLng(Blat, Blnt);
                points.add(latlnt);
                if (flag) {
                    flag = false;
                    MarkerOptions moo = new MarkerOptions().position(latlnt).icon(start).zIndex(9);
                    mBaiduMap.addOverlay(moo);
                }
//                if(i==list.size()-1){
//                    MarkerOptions moo = new MarkerOptions().position(latlnt).icon(end).zIndex(9);
//                    mBaiduMap.addOverlay(moo);
//                }
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(list.get(i).BLat) && !TextUtils.isEmpty(list.get(i).BLng)) {
                double Blat = Double.valueOf(list.get(i).BLat);
                double Blnt = Double.valueOf(list.get(i).BLng);
                LatLng latlnt = new LatLng(Blat, Blnt);
                MarkerOptions moo = new MarkerOptions().position(latlnt).icon(end).zIndex(9);
                mBaiduMap.addOverlay(moo);
                break;
            }
        }
        if(points.size()<2){
            DialogUtils.showNormalToast("暂无轨迹信息！");

        }else{
            OverlayOptions oop = new PolylineOptions().width(8).color(Utils.getResourcesColor(R.color.darkslateblue)).points(points);
            mBaiduMap.addOverlay(oop);
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
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
//        mapBool = true;
    }

    @Override
    protected void lazyLoad() {
        init();
    }

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || locusMap == null) {
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
                builder.target(ll).zoom(18.f - 5);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        if (locusMap != null) {
            locusMap.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (locusMap != null) {
            locusMap.onDestroy();
        }
        if (mLocClient != null) {
            mLocClient.stop();
        }
        start.recycle();
        end.recycle();
//        stop.recycle();
//        startcar.recycle();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        if (locusMap != null) {
            locusMap.onResume();
        }
        super.onResume();
    }
}
