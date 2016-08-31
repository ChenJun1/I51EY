package com.cvnavi.logistics.i51eyun.app;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @version 1.0.0
 * @description
 * @date 2016-5-17 下午1:12:36
 * @email yuanlunjie@163.com
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener {

    private TextView titlt = null;
    private WebView webView = null;
    private LinearLayout back_linearLayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        titlt = (TextView) findViewById(R.id.title_tv);
        webView = (WebView) findViewById(R.id.webview);
        webView.setOnClickListener(this);
        back_linearLayout = (LinearLayout) findViewById(R.id.back_llayout);
        back_linearLayout.setOnClickListener(this);
        titlt.setText("帮助手册");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultTextEncodingName("GBK");
        webView.loadUrl(Constants.HelpManual_URL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_llayout:
                finish();
                break;
        }

    }


//    @Event(value = R.id.back_linearLayout, type = View.OnClickListener.class)
//    private void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_linearLayout:
//                finish();
//                break;
//            default:
//                break;
//        }
//    }
}
