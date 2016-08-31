package com.cvnavi.logistics.i51eyun.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JohnnyYuan on 2016/7/6.
 */
public class ContextUtil {

    public static String replaceBlank(String str) {
        String dest = "";
        if (TextUtils.isEmpty(str) == false) {
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(str);
            dest = matcher.replaceAll("");
        }
        return dest;
    }

    /**
     * 取小数点后两位
     *
     * @param number
     * @return
     */
    public static String getDouble(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        Double numbers = Double.valueOf(number);
        numbers = (double) Math.round(numbers * 100) / 100;
        String strNumber = String.valueOf(numbers);
        return strNumber;
    }

    /**
     * 拨号
     * @param callNumber
     * @param context
     */
    public static void callAlertDialog(final String callNumber, final Context context) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
        dBuilder.setTitle("提示");
        dBuilder.setMessage("拨号：" + callNumber);
        dBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));

                context.startActivity(in);
            }
        }).setNegativeButton("取消", null);
        dBuilder.create();
        dBuilder.show();
    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }



}
