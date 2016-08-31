package com.cvnavi.logistics.i51eyun.app.activity.driver.home.location;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.mCarControl;
import com.cvnavi.logistics.i51eyun.app.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.app.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DriverCarMonitorMapActivity extends BaseActivity {

    private final String TAG = DriverCarMonitorMapActivity.class.getName();

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
    @BindView(R.id.driver_Serial_Oid_tx)
    TextView driver_Serial_Oid_tx;
    @BindView(R.id.history_locus_btn)
    Button historyLocusBtn;

    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private MyLocationListener mLocListener = new MyLocationListener();
    // 经纬度
    private Double longitude = 0.0;
    private Double latitude = 0.0;
    // 是否首次定位
    private boolean isFirstLoc = true;

    private mCarControl carControlBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_location);
        ButterKnife.bind(this);
        initMap();
        init();
    }

    private void init() {
        titltTextView.setText(Utils.getResourcesString(R.string.map));
        if (getIntent().getSerializableExtra(Constants.TODRIVERMONITORMAPACTIVITY) != null) {
            carControlBean = (mCarControl) getIntent().getSerializableExtra(Constants.TODRIVERMONITORMAPACTIVITY);
            if (!TextUtils.isEmpty(carControlBean.BLng) && !TextUtils.isEmpty(carControlBean.BLat)) {
                longitude = Double.valueOf(carControlBean.BLng);
                latitude = Double.valueOf(carControlBean.BLat);
            }else{
                DialogUtils.showNormalToast(Utils.getResourcesString(R.string.error_null_loc));
            }
            setViewData();
        }
    }

    private void showStop(LatLng mLatLng, String CHS_Address) {

        View popu = View.inflate(this, R.layout.activity_fragment_2_showpop,
                null);
        TextView title = (TextView) popu.findViewById(R.id.showpop_title_text);


        title.setText( "地址：" + CHS_Address.replaceAll("；", "；\n　　　"));
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

    private void setViewData() {

        SetViewValueUtil.setTextViewValue(carCodeTx, carControlBean.CarCode);
        SetViewValueUtil.setTextViewValue(carAddressTx, carControlBean.CHS_Address);
        SetViewValueUtil.setTextViewValue(driverNameTx, carControlBean.Driver);
        SetViewValueUtil.setTextViewValue(driverTelTx, carControlBean.Driver_Tel);
        SetViewValueUtil.setTextViewValue(driver_Serial_Oid_tx, carControlBean.Letter_Oid);

        LatLng latLng=new LatLng(Double.valueOf(carControlBean.BLat),Double.valueOf(carControlBean.BLng));
        showStop(latLng,carControlBean.CHS_Address);
    }

    private void initMap() {
        mBaiduMap = monitoringMap.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(mLocListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();


    }

    @OnClick({R.id.car_code_tx, R.id.driver_tel_tx,R.id.history_locus_btn,R.id.back_llayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.car_code_tx:
                isFirstLoc = true;
                mLocClient.requestLocation();
                break;
            case R.id.driver_tel_tx:
                ContextUtil.callAlertDialog(driverTelTx.getText().toString(), DriverCarMonitorMapActivity.this);
                break;
            case R.id.history_locus_btn:
                Intent intent=new Intent(this,DriverRecordMainActivity.class);
                intent.putExtra(Constants.CAR_KEY,carControlBean.CarCodeKey);
               startActivity(intent);
                break;
            case R.id.back_llayout:
                finish();
            default:
                break;
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || bdLocation == null) {
                return;
            }
            try {

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(latitude).longitude(longitude)
                        .build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(latitude, longitude);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(12.f - 2);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                            .newMapStatus(builder.build()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        // 回收 bitmap 资源
        super.onDestroy();
    }

}
