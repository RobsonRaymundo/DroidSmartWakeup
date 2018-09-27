package com.droid.ray.droidsmartwakeup;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {

    public static String TAG = "DroidSmartWakeup";

    public static String getLogTagWithMethod(Throwable stack) {
        StackTraceElement[] trace = stack.getStackTrace();
        return trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
    }

    public static void TimeSleep(int time) {
        try {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
            Thread.sleep(time);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }
}
