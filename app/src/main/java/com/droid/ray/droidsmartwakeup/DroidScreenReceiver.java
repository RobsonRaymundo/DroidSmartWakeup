package com.droid.ray.droidsmartwakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DroidScreenReceiver extends BroadcastReceiver {

    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                screenOff = true;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                screenOff = false;
            }
          //  Intent i = new Intent(context, DroidService.class);
          //  i.putExtra("screen_state", screenOff);
          //  context.startService(i);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }
}