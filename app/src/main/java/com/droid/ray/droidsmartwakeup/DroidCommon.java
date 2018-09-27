package com.droid.ray.droidsmartwakeup;

import android.app.ActivityManager;
import android.util.Log;
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

    public static boolean isMyServiceRunning(ActivityManager activityManager) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        ActivityManager manager = activityManager;
        try {

            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

                if (DroidService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return false;
    }
}
