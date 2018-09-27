package com.droid.ray.droidsmartwakeup;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("NewApi")
public class DroidService extends AccessibilityService implements SensorEventListener {
    public static boolean waitingTimeOutNofitication;
    public static boolean openSensorProximity;
    private static int timeOutScreenDisplay;
    public static boolean newNotification;
    private SensorManager sensorManager;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        newNotification = false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        timeOutScreenDisplay = 0;
        if (waitingTimeOutNofitication == false) {
            newNotification = true;
            try {
                String packageName = (String) event.getPackageName();
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (!pm.isScreenOn() || packageName.contains("com.android.mms")) {
                    postMessageInThread();
                }

            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());

            } finally {
                timeSleep(1000);
                newNotification = false;
            }
        }

    }

    @Override
    public void onInterrupt() {

    }

    private void timeSleep(int time) {
        try {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
            Thread.sleep(time);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    private void turnOnScreen() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        PowerManager.WakeLock wl = null;
        //    wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "DroidNotification");
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "DroidNotification");

        try {
            wl.acquire();
            //waitingTimeOutNotification(pm, km, timeNotification);
        } finally {
            try {
                if (wl.isHeld()) {
                    wl.release();
                }
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    }

    private void EnabledSensorPriximity() {
        SetSensorProximity(true);
    }

    private void DisabledSensorPriximity() {
        SetSensorProximity(false);
    }

    private void LoopingTimeOutSensor() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            openSensorProximity = false;
            EnabledSensorPriximity();
            int timeOutNotificationSensor = 72000; // 6000 =  10 minutos   (600 = 1 minuto)
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            while (pm.isScreenOn() == false && openSensorProximity == false) {
                timeOutScreenDisplay = timeOutScreenDisplay + 1;
                if (timeOutScreenDisplay > timeOutNotificationSensor) {
                    break;
                }
                timeSleep(1000);
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        } finally {
            DisabledSensorPriximity();
        }

        if (openSensorProximity) {
            turnOnScreen();
        }
    }


    //implementation:
    private void postMessageInThread() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    LoopingTimeOutSensor();
                }
            };
            t.start();
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public void SetSensorProximity(boolean turnOn) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + turnOn);
        try {
            if (turnOn && sensorManager == null) {
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

                if (proximitySensor != null) {
                    sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    timeSleep(1000);
                }
            }
            if (turnOn == false && sensorManager != null) {
                sensorManager.unregisterListener(this);
                //  timeSleep(700);
                sensorManager = null;
            }

        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        try {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + event.values[0] + " < " +  event.sensor.getMaximumRange()) ;
                openSensorProximity = (event.values[0] == event.sensor.getMaximumRange());
            }

        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onCreate() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        super.onCreate();
        try {
            // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            BroadcastReceiver mReceiver = new DroidScreenReceiver();
            registerReceiver(mReceiver, filter);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }


    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            if (waitingTimeOutNofitication == false) {
                boolean screenOff = intent.getBooleanExtra("screen_state", false);
                if (screenOff) {
                    SetSensorProximity(true);
                } else {
                    SetSensorProximity(false);
                }

            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }
}