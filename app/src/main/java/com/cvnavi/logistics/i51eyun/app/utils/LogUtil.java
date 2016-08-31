package com.cvnavi.logistics.i51eyun.app.utils;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {
    public static String customTagPrefix = "-->>LogUtil";

    public static boolean debug;

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    private LogUtil() {
    }

    private static String generateTag() {
        StackTraceElement caller = (new Throwable()).getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag,
                new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (debug) {
            String tag = generateTag();
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (debug) {
            String tag = generateTag();
            Log.e(tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (debug) {
            String tag = generateTag();
            Log.i(tag, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (debug) {
            String tag = generateTag();
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (debug) {
            String tag = generateTag();
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.w(tag, tr);
        }
    }

    public static void wtf(String content) {
        if (debug) {
            String tag = generateTag();
            Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (debug) {
            String tag = generateTag();
            Log.wtf(tag, tr);
        }
    }
}
