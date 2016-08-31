package com.cvnavi.logistics.i51eyun.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cvnavi.logistics.i51eyun.app.service.MyCheckLoginService;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.BaseWebActivity;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.CircleFlowIndicator;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ImagePagerAdapter;
import com.cvnavi.logistics.i51eyun.app.widget.bannerview.ViewFlow;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 测试activity
 * activity_main
 */
public class MainActivity extends Activity {
    public static final int CMD_STOP_SERVICE = 0;
    Button btnStart;//开始服务Button对象应用
    Button btnStop;//停止服务Button对象应用
    TextView tv;//TextView对象应用
    DataReceiver dataReceiver;//BroadcastReceiver对象
    @Override
    public void onCreate(Bundle savedInstanceState) {//重写onCreate方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);//设置显示的屏幕
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);
        tv = (TextView)findViewById(R.id.tv);
        btnStart.setOnClickListener(new View.OnClickListener() {//为按钮添加点击事件监听
            @Override
            public void onClick(View v) {//重写onClick方法
                Intent myIntent = new Intent(MainActivity.this, MyCheckLoginService.class);
                MainActivity.this.startService(myIntent);//发送Intent启动Service
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {//为按钮添加点击事件监听
            @Override
            public void onClick(View v) {//重写onClick方法
                Intent myIntent = new Intent();//创建Intent对象
                myIntent.setAction("com.roy.MyService");
                myIntent.putExtra("cmd", CMD_STOP_SERVICE);
                sendBroadcast(myIntent);//发送广播
            }
        });
    }

    private class DataReceiver extends BroadcastReceiver {//继承自BroadcastReceiver的子类
        @Override
        public void onReceive(Context context, Intent intent) {//重写onReceive方法
            double data = intent.getDoubleExtra("data", 0);
            tv.setText("Service的数据为:"+data);
        }
    }
    @Override
    protected void onStart() {//重写onStart方法
        dataReceiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();//创建IntentFilter对象
        filter.addAction("com.roy.MyActivity");
        registerReceiver(dataReceiver, filter);//注册Broadcast Receiver
        super.onStart();
    }
    @Override
    protected void onStop() {//重写onStop方法
        unregisterReceiver(dataReceiver);//取消注册Broadcast Receiver
        super.onStop();
    }
}
