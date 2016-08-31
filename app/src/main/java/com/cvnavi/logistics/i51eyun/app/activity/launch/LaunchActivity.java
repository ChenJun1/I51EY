package com.cvnavi.logistics.i51eyun.app.activity.launch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.guid.GuidViewPagerActivity;
import com.cvnavi.logistics.i51eyun.app.activity.login.LoginActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.app.bean.model.AppVsionBean;
import com.cvnavi.logistics.i51eyun.app.bean.response.AppVsionBeanResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppUpdateBeanResponse;
import com.cvnavi.logistics.i51eyun.app.config.LoginService;
import com.cvnavi.logistics.i51eyun.app.service.DownAPKService;
import com.cvnavi.logistics.i51eyun.app.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.SharedPreferencesTool;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ScreenSupport;

import org.json.JSONObject;

import java.io.File;

import volley.VolleyManager;


public class LaunchActivity extends BaseActivity {
    private String TAG=LaunchActivity.class.getName();
    private final int GetAppUriOk=123;
    private final int GetAppVsionOk=124;
    private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟3秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ScreenSupport.displayMetrics(this);
        setFullScreen(true);
       // toLogin();
        getVison(LoginService.GetVersion_Request_Url);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GetAppVsionOk:
                    AppVsionBean vison= (AppVsionBean) msg.obj;
                    if(vison==null){
                        DialogUtils.showNormalToast("获取版本失败！");
                        break;
                    }
                    String visonStr=vison.AndroidVersion;
                  //  String visonStr="1";
                    String curVerName= ContextUtil.getVerName(LaunchActivity.this);
                    if(!curVerName.equals(visonStr)){
                        DialogUtils.showMessageDialog(LaunchActivity.this, "更新提示",
                                "当前版本非最新版本，请下载最新版本！", "确定", "取消", new CustomDialogListener() {
                                    @Override
                                    public void onDialogClosed(int closeType) {
                                        if(CustomDialogListener.BUTTON_NO==closeType){
                                                finish();
                                        }else if(CustomDialogListener.BUTTON_OK==closeType){
                                            getAppDownUrl(LoginService.GetAPP_Request_Url);
                                        }
                                    }
                                });
                    }else{
                        toLogin();
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj==null? Utils.getResourcesString(R.string.request_Fill):msg.obj.toString());
                    finish();
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(LaunchActivity.this,Utils.getResourcesString(R.string.request_error));
                    finish();
                    break;
                case GetAppUriOk:
                    AppUpdateBean bean = (AppUpdateBean) msg.obj;
                    File file = null;
                    if (bean != null) {//判断是否存在安装包
                        String filePath = DownAPKService.getStoragePath()
                                + bean.AppName;
                        file = new File(filePath);
                        if (file.exists()) {
                            boolean delete = file.delete();
                            if (delete) {
                                successGetDwonAppUrl(bean);
                            }
                        } else {
                            successGetDwonAppUrl(bean);
                        }

                    }
                    break;
                default:
                    break;
            }

        }
    };

     private void getAppDownUrl(final String Url) {
         VolleyManager.newInstance().PostJsonRequest(null, Url, new JSONObject(), new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 Log.v(TAG, "-->>post json对象: " + response.toString());
                 GetAppUpdateBeanResponse Vsion= JsonUtils.parseData(response.toString(),GetAppUpdateBeanResponse.class);
                 Message msg = Message.obtain();
                 if (Vsion.Success) {
                     msg.what = GetAppUriOk;
                     msg.obj = Vsion.DataValue;
                 } else {
                     msg.what = Constants.REQUEST_FAIL;
                 }
                 mHandler.sendMessage(msg);
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.e(TAG, "-->>" + error.getMessage(), error);
                 Message msg=Message.obtain();
                 msg.what=Constants.REQUEST_ERROR;
                 mHandler.sendMessage(msg);
             }
         });
     }

    private void getVison(final String Url) {
        VolleyManager.newInstance().PostJsonRequest(null, Url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v(TAG, "-->>post json对象: " + response.toString());
                AppVsionBeanResponse Vsion= JsonUtils.parseData(response.toString(),AppVsionBeanResponse.class);
                Message msg = Message.obtain();
                if (Vsion.Success) {
                    msg.what = GetAppVsionOk;
                    msg.obj = Vsion.DataValue;
                } else {
                    msg.what = Constants.REQUEST_FAIL;
                }
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "-->>" + error.getMessage(), error);
                Message msg=Message.obtain();
                msg.what=Constants.REQUEST_ERROR;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void toLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesTool.getBoolean(SharedPreferencesTool.FRIST_LOGIN, true)) {
                    //将登录标志位设置为false，下次登录时不在显示首次登录界面
                    SharedPreferencesTool.putBoolean(SharedPreferencesTool.FRIST_LOGIN, false);
                    skipActivity(LaunchActivity.this, GuidViewPagerActivity.class);
//                    skipActivity(LaunchActivity.this, LoginActivity.class);
                } else {
                    skipActivity(LaunchActivity.this, LoginActivity.class);
                }
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
    public void successGetDwonAppUrl(final AppUpdateBean appBean) {

        if (appBean != null) {
            DialogUtils.showMessageDialog(LaunchActivity.this, "下载提示",
                    "开始后台下载，完成后自动安装！", "确定", "取消", new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            if (CustomDialogListener.BUTTON_NO == closeType) {
                                finish();

                            } else if (CustomDialogListener.BUTTON_OK == closeType) {
                                DownAPKService.startService(LaunchActivity.this,
                                        "e航", appBean.AppUrl,
                                        Integer.parseInt(appBean.AppSize));
                            }
                        }
                    });
        }
    }
    public void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

}
