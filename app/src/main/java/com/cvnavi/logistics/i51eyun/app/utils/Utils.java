package com.cvnavi.logistics.i51eyun.app.utils;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.bean.model.mMainService;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.response.QuerySimpleCodeLengthResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;

import org.json.JSONObject;

import java.util.List;

import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/27.
 */
public class Utils {
    /**
     * 获取资源文件里的dimension
     *
     * @param resId
     * @return
     */
    public static int getResourcesDimension(int resId) {
        return MyApplication.getInstance().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取资源文件里的color
     *
     * @param resId
     * @return
     */
    public static int getResourcesColor(int resId) {
        return MyApplication.getInstance().getResources().getColor(resId);
    }

    /**
     * 获取资源文件里的字符串
     *
     * @param resId
     * @return
     */
    public static String getResourcesString(int resId) {
        return MyApplication.getInstance().getResources().getString(resId);
    }

    /**
     * 获取资源文件里的字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getResourcesArrString(int resId) {
        return MyApplication.getInstance().getResources().getStringArray(resId);
    }


    /**
     * 扫描条形码 生成一个全票号
     * 格式 C-20150038320-16-1
     * 生成的全票号格式:公司号(6位数)+（000..）+（A或者b.c）+20150038320  总共30位
     *
     * @param scanning
     * @return
     */
    public static String createAllTicketNo(String scanning) {
        if (TextUtils.isEmpty(scanning) || MyApplication.getInstance().getLoginInfo() == null) {
            return null;
        }

        String[] arr = scanning.split("-");

        if (arr.length < 3) {
            return null;
        }
        String ticket_type = arr[0];
        String ticket_num = arr[1];

        if (TextUtils.isEmpty(ticket_num) && ticket_num.length() > 23) {
            return null;
        }

        int length = 23 - ticket_num.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        return MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid + sb.toString() + ticket_type + ticket_num;
    }

    public static void getQueryLength() {

        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;

        VolleyManager.newInstance().PostJsonRequest(TMSService.QuerySimpleCodeLength_TAG, TMSService.QuerySimpleCodeLength_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>searchRespon = " + response.toString());
                QuerySimpleCodeLengthResponse data = GsonUtil.newInstance().fromJson(response, QuerySimpleCodeLengthResponse.class);
                if (data.isSuccess()) {
                    SharedPreferencesTool.putString(SharedPreferencesTool.QUERY_LENGTH, data.getDataValue());
                } else {
                    SharedPreferencesTool.putString(SharedPreferencesTool.QUERY_LENGTH, "6");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SharedPreferencesTool.putString(SharedPreferencesTool.QUERY_LENGTH, "6");
            }
        });


    }


    /**
     * 判断是否有该权限
     *
     * @param
     * @return
     */
    public static boolean checkOperate(String ServiceID) {
        List<mMainService> list = MyApplication.getInstance().getLoginInfo().DataValue.OperateList;
        if (list != null) {
            for (mMainService mMainService : list) {
                if (mMainService.ServiceID.equals(ServiceID)) {
                    return true;
                }
            }
        } else {
            return false;
        }

        return false;
    }


}
