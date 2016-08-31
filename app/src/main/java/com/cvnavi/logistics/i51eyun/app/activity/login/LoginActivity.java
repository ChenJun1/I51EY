package com.cvnavi.logistics.i51eyun.app.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivityForNoTitle;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.HelpActivity;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.CargoMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.DriverMainActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.MyTaskActivity;
import com.cvnavi.logistics.i51eyun.app.activity.employee.EmployeeMainActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mVerifyCode;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetAppLoginRequest;
import com.cvnavi.logistics.i51eyun.app.bean.request.VerifyCodeRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.VerifyCodeResponse;
import com.cvnavi.logistics.i51eyun.app.config.LoginService;
import com.cvnavi.logistics.i51eyun.app.extra.MyCountDownTimer;
import com.cvnavi.logistics.i51eyun.app.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SharedPreferencesTool;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.utils.VerifyPhoneNumUtil;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.LoadingDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ScreenSupport;
import com.cvnavi.logistics.i51eyun.app.widget.edittext.EditTextWithDel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 登陆界面
 */

public class LoginActivity extends BaseActivityForNoTitle implements View.OnClickListener {
    private static final int TEL_PHONE_NUM_LENGTH = 11;
    MyCountDownTimer myCountDownTimer;
    long exitTime;
    @BindView(R.id.help_iv)
    ImageView helpIv;
    @BindView(R.id.help_rl)
    RelativeLayout helpRl;
    @BindView(R.id.phone_num_et)
    EditTextWithDel phoneNumEt;
    @BindView(R.id.pwd_et)
    EditTextWithDel pwdEt;
    @BindView(R.id.verification_btn)
    Button verificationBtn;
    @BindView(R.id.pwd_llayout)
    LinearLayout pwdLlayout;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.bg_ll)
    LinearLayout bgLl;

    private SweetAlertDialog loadingDialog = null;
    //短信拦截功能 没有完善（待开发）
    private BroadcastReceiver smsReceiver;
    private IntentFilter filter;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        myCountDownTimer = new MyCountDownTimer(60000, 1000, verificationBtn);
        ScreenSupport.displayMetrics(this);
//        receiveMsm();


        ((EditTextWithDel) (findViewById(R.id.phone_num_et))).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < TEL_PHONE_NUM_LENGTH) {
                    ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText("");
                    cleanToken();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        initLoginData();
    }

    private void receiveMsm() {
        filter = new IntentFilter();
        //设置短信拦截参数
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for (Object obj : objs) {
                    byte[] pdu = (byte[]) obj;
                    SmsMessage sms = SmsMessage.createFromPdu(pdu);
                    String message = sms.getMessageBody();
                    String from = sms.getOriginatingAddress();
                    LogUtil.d("-->>  from = " + from);
                    if (from.equals("18301969307")) {
                        if (!TextUtils.isEmpty(from)) {
                            String code = patternCode(message);
                            if (!TextUtils.isEmpty(code)) {
                                Message msg = new Message();
                                msg.what = 1;
                                Bundle bundle = new Bundle();
                                bundle.putString("messagecode", code);
                                msg.setData(bundle);
                                msgHangler.sendMessage(msg);
                            }
                        }
                    }
                }
            }
        };
        registerReceiver(smsReceiver, filter);
    }

    private Handler msgHangler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String codeMsg = msg.getData().getString("messagecode");
                    ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText(codeMsg);
                    break;

                default:
                    break;
            }

        }

        ;
    };

    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    @OnClick({R.id.verification_btn, R.id.confirm_btn, R.id.bg_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verification_btn:
                if (verifyPhoneNumData() == false) {
                    return;
                }
                myCountDownTimer.start();
                verification();

                break;
            case R.id.confirm_btn:
                if (verifyData() == false) {
                    return;
                }

                login();

                break;
            case R.id.bg_ll:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                break;
        }
    }

    private boolean verifyData() {
        if (verifyPhoneNumData() == false) {
            return false;
        }

        if (TextUtils.isEmpty(((EditTextWithDel) (findViewById(R.id.pwd_et))).getText().toString())) {
            Toast.makeText(this, R.string.input_pwd_or_vefiry_code_hint, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verifyPhoneNumData() {
        if (TextUtils.isEmpty(phoneNumEt.getText().toString())) {
            Toast.makeText(this, R.string.input_phone_num_hint, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (VerifyPhoneNumUtil.isMobileNO(phoneNumEt.getText().toString()) == false) {
            Toast.makeText(this, R.string.input_right_phone_num_hint, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initLoginData() {
        ((EditTextWithDel) (findViewById(R.id.phone_num_et))).setText(SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UER_TEL, null));
//         ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText(SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UER_VERIFY_CODE, null));
        if (SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_Token, null) != null) {
            ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText(SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UER_VERIFY_CODE, null));
            login();
        } else {
            ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText("");
        }
    }


    private void verification() {
        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
        verifyCodeRequest.User_Tel = ((EditTextWithDel) (findViewById(R.id.phone_num_et))).getText().toString();

        final DataRequestBase requestBase = new DataRequestBase();
        requestBase.DataValue = verifyCodeRequest.User_Tel;

        LogUtil.d("-->> request = " + new Gson().toJson(requestBase));
        VolleyManager.newInstance().PostJsonRequest(LoginService.VerifyCode_TAG, LoginService.VerifyCode_Request_Url, GsonUtil.newInstance().toJson(requestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message message = Message.obtain();
                LogUtil.d("-->> respon=" + jsonObject.toString());

                VerifyCodeResponse responseData = GsonUtil.newInstance().fromJson(jsonObject, VerifyCodeResponse.class);
                if (responseData.Success) {

                    mVerifyCode myVerifyCode = GsonUtil.newInstance().fromJson(responseData.DataValue, mVerifyCode.class);
//                            SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UER_UUID, responseData.mVerifyCode.UUID);
                    message.what = Constants.REQUEST_SUCC;
                    message.obj = myVerifyCode;
                } else {
                    message.what = Constants.REQUEST_FAIL;
                    message.obj = responseData.ErrorText;
                }

                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Message message = Message.obtain();
                message.what = Constants.REQUEST_FAIL;
                handler.sendEmptyMessage(message.what);
            }
        });


    }

    /**
     * 15216747933 司机账号
     * 15216747938 货主账号
     * 15021705443 APP员工
     * <p/>
     * <p/>
     * 15216747938 货主账号E
     * 15021705443  APP员工F
     * * 15216747933 司机账号G
     * <p/>
     * 18301969307 APP员工
     */
    private void login() {
        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.show();

        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.User_Key = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_User_Key, null);
        dataRequestBase.UserType_Oid = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UserType_Oid, null);
        dataRequestBase.Token = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_Token, null);
        dataRequestBase.Company_Oid = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_Company_Oid, null);
        String tag = null;
        String url = null;

        if (TextUtils.isEmpty(dataRequestBase.Token) || (!((EditTextWithDel) (findViewById(R.id.phone_num_et))).getText().toString().equals(SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UER_TEL, null)))) {
            //首次登錄,或者登陆的手机号和保存的手机号不一样
            tag = LoginService.VerifyCode_TAG;
            url = LoginService.GetAppLogin_Request_Url;

            GetAppLoginRequest DataValue = new GetAppLoginRequest();
            DataValue.User_Tel = ((EditTextWithDel) (findViewById(R.id.phone_num_et))).getText().toString();
            DataValue.VerifyCode = ((EditTextWithDel) (findViewById(R.id.pwd_et))).getText().toString();
            DataValue.UUID = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UER_UUID, null);//测试环境中要传
            dataRequestBase.DataValue = GsonUtil.newInstance().toJsonStr(DataValue);

        } else {
            //七日免登录
            tag = LoginService.GetAppAutoLogin_TAG;
            url = LoginService.GetAppAutoLogin_Request_Url;

            dataRequestBase.DataValue = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_UER_TEL, null);
        }

        LogUtil.d("-->> 登录请求 :" + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(tag, url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.d("-->> 登录返回：" + jsonObject.toString());
                Message message = Message.obtain();
                GetAppLoginResponse info = GsonUtil.newInstance().fromJson(jsonObject, GetAppLoginResponse.class);
                if (info != null) {
                    if (info.Success) {
                        message.what = Constants.REQUEST_SUCC;
                        message.obj = info;
                    } else {
                        message.what = Constants.REQUEST_FAIL;
                        message.obj = info.ErrorText;
                    }
                } else {
                    message.what = Constants.REQUEST_FAIL;
                }

                loginHandler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = Message.obtain();
                msg.what = Constants.REQUEST_ERROR;
                loginHandler.sendMessage(msg);
            }
        });
    }

    private void saveToken(GetAppLoginResponse info) {
        if (info.DataValue != null) {
            SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_User_Key, info.DataValue.User_Key);
            SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UserType_Oid, info.DataValue.UserType_Oid);
            SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_Token, info.DataValue.Token);
            SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_Company_Oid, info.DataValue.Company_Oid);
        }

        if (verifyPhoneNumData() == false) {
            return;
        }
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UER_TEL, ((EditTextWithDel) (findViewById(R.id.phone_num_et))).getText().toString());
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UER_VERIFY_CODE, ((EditTextWithDel) (findViewById(R.id.pwd_et))).getText().toString());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText("");
                    mVerifyCode code = (mVerifyCode) msg.obj;
                    if (code != null && code.VerifyCode != null) {
                        // 写入缓存
                        MyApplication.getInstance().setVerifyCode(code);
                        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UER_UUID, code.UUID);
                    }
                    DialogUtils.showSuccToast(Utils.getResourcesString(R.string.get_verifycode_succ));

                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.get_verifycode_faild));
                    break;
            }
        }
    };

    private Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadingDialog.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    GetAppLoginResponse loginInfo = (GetAppLoginResponse) msg.obj;
                    if (loginInfo != null && loginInfo.DataValue != null) {
                        //保存数据
                        saveToken(loginInfo);
                        //写入缓存
                        MyApplication.getInstance().setLoginInfo(loginInfo);

                        String userType = loginInfo.DataValue.UserType_Oid;
                        if (userType.equals("G")) {
                            skipActivity(LoginActivity.this, DriverMainActivity.class);
//                            skipActivity(LoginActivity.this, MyTaskActivity.class);
                        } else if (userType.equals("E")) {
                            skipActivity(LoginActivity.this, CargoMainActivity.class);
                        } else if (userType.equals("F")) {
                            skipActivity(LoginActivity.this, EmployeeMainActivity.class);
                        }
                    } else {
                        DialogUtils.showFailToast(Utils.getResourcesString(R.string.login_fail));
                    }
                    break;
                case Constants.REQUEST_FAIL:
                    String infoError = (String) msg.obj;
                    if (infoError != null && infoError.equals("Displace")) {
                        DialogUtils.showFailToast("账号已在别的设备登录!");
                    } else {
                        DialogUtils.showFailToast(Utils.getResourcesString(R.string.login_fail));
                    }
                    reLogin();
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(LoginActivity.this, Utils.getResourcesString(R.string.request_error));
                    reLogin();
                    break;
            }
        }
    };

    //重新登陆
    private void reLogin() {
        ((EditTextWithDel) (findViewById(R.id.pwd_et))).setText("");
        cleanToken();
    }

    private void cleanToken() {
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_User_Key, null);
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UserType_Oid, null);
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_Token, null);
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_Company_Oid, null);
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UER_TEL, null);
        SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_UER_VERIFY_CODE, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplication(), "再按一次退出!", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityStackManager.getInstance().AppExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.help_rl)
    public void onClick() {
        startActivity(new Intent(LoginActivity.this, HelpActivity.class));
    }

}
