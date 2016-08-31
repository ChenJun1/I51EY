package com.cvnavi.logistics.i51eyun.app.config;

/**
 * Created by JohnnyYuan on 2016/7/4.
 */
public class LoginService extends HostAddress {

    public static String LoginService_Address = Host_Url + "/Login/Login/";

    // 获取验证码
    public static String VerifyCode_TAG = "VerifyCode";
    public static String VerifyCode_Request_Url = LoginService_Address + VerifyCode_TAG;

    // 获取版本号
    public static String GetAppVersion_TAG = "GetAppVersion";
    public static String GetAppVersion_Request_Url = LoginService_Address + GetAppVersion_TAG;

    // 获取APP下载地址
    public static String GetAppDownPath_TAG = "GetAppDownPath";
    public static String GetAppDownPath_Request_Url = LoginService_Address + GetAppDownPath_TAG;

    // 7日免登陆
    public static String GetAppAutoLogin_TAG = "GetAppAutoLogin";
    public static String GetAppAutoLogin_Request_Url = LoginService_Address + GetAppAutoLogin_TAG;

    // 登录
    public static String GetAppLogin_TAG = "GetAppLogin";
    public static String GetAppLogin_Request_Url = LoginService_Address + GetAppLogin_TAG;

    // 退出登录
    public static String ExitLogin_TAG = "ExitLogin";
    public static String ExitLogin_Request_Url = LoginService_Address + ExitLogin_TAG;

    //获取版本号
    public static String GetVersion_TAG = "GetVersion";
    public static String GetVersion_Request_Url = LoginService_Address + GetVersion_TAG;

    //APP下载地址
    public static String GetAPP_TAG = "GetAppDownUrl";
    public static String GetAPP_Request_Url = LoginService_Address + GetAPP_TAG;


    //我的信息
    public static String GetShipperInfo_TAG = "GetShipperInfo";
    public static String GetShipperInfo_Request_Url = LoginService_Address + GetShipperInfo_TAG;






}
