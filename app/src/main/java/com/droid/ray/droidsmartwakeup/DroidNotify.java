package com.droid.ray.droidsmartwakeup;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.util.Log;

public class DroidNotify extends PreferenceActivity {

    private boolean active;
    private Preference service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        super.onCreate(savedInstanceState);
        try {
            addPreferencesFromResource(R.xml.preferences);

            service = (Preference) findPreference("service");
            service.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    } catch (Exception ex) {
                    }
                    return true;
                }
            });
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }

    }

    public void onResume() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        super.onResume();
        try {
            active = isMyServiceRunning();
            if (active) {
                service.setTitle(R.string.app_active);
                service.setSummary(R.string.app_deactive);
            } else {
                service.setTitle(R.string.app_inactive);
                service.setSummary(R.string.app_activate);
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }


    private boolean isMyServiceRunning() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        try {

            for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

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
