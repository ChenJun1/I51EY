package com.cvnavi.logistics.i51eyun.app;

import com.baidu.mapapi.SDKInitializer;
import com.cvnavi.logistics.i51eyun.app.bean.model.mVerifyCode;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.cache.BasicDataBuffer;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import org.litepal.LitePalApplication;


public class MyApplication extends LitePalApplication {
    private static MyApplication ms_MyApplication;

    private mVerifyCode verifyCode;

    public mVerifyCode getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(mVerifyCode verifyCode) {
        this.verifyCode = verifyCode;
    }

    private GetAppLoginResponse loginInfo;

    public GetAppLoginResponse getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(GetAppLoginResponse loginInfo) {
        this.loginInfo = loginInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ms_MyApplication = this;
        BasicDataBuffer.newInstance().init(this);
        init();
        LogUtil.setDebug(true);
    }

    public static synchronized MyApplication getInstance() {
        return ms_MyApplication;
    }

    private void init() {
        SDKInitializer.initialize(getApplicationContext());
    }

}
