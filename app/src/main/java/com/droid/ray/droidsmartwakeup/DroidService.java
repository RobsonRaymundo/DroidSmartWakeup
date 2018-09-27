package com.droid.ray.droidsmartwakeup;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

@SuppressLint("NewApi")
public class DroidService extends AccessibilityService implements SensorEventListener {
    public static boolean openSensorProximity;
    private SensorManager sensorManager;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            SetSensorProximity(true);
        }
    }

    @SuppressWarnings("deprecation")
    private void TurnOnScreen() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, DroidCommon.TAG);
        try {
            wl.acquire();

        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
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

    private void ShowDroidBackground() {
        try {
            Intent dialogIntent = new Intent(this, DroidBackground.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            this.startActivity(dialogIntent);
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
                    DroidCommon.TimeSleep(1000);
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
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + event.values[0] + " < " + event.sensor.getMaximumRange());
                openSensorProximity = (event.values[0] == event.sensor.getMaximumRange());
                if (openSensorProximity) {
                    if (DroidPreferences.GetBool(getBaseContext(), "openSensorProximity") == false) {
                        ShowDroidBackground();
                        SetSensorProximity(false);
                        TurnOnScreen();
                    }
                }
                DroidPreferences.SetBool(getBaseContext(), "openSensorProximity", openSensorProximity);
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onInterrupt() {

    }

}