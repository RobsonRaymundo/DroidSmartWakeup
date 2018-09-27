package com.droid.ray.droidsmartwakeup;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
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
            active = DroidCommon.isMyServiceRunning((ActivityManager) getSystemService(ACTIVITY_SERVICE));
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



}
