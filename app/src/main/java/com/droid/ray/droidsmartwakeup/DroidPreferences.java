package com.droid.ray.droidsmartwakeup;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Robson on 31/07/2017.
 */

public class DroidPreferences {

    public static final String PREF_ID = DroidCommon.TAG;

    public static void SetInteger(Context context, String chave, int valor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(chave, valor);
        editor.commit();
    }

    public static int GetInteger(Context context, String chave) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        int i = sharedPreferences.getInt(chave, 0);
        return i;

    }

    public static void SetString(Context context, String chave, String valor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(chave, valor);
        editor.commit();
    }

    public static String GetString(Context context, String chave) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        String i = sharedPreferences.getString(chave, "");
        return i;
    }

    public static void SetBool(Context context, String chave, boolean valor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(chave, valor);
        editor.commit();
    }

    public static boolean GetBool(Context context, String chave) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        boolean i = sharedPreferences.getBoolean(chave, true);
        return i;
    }

    public static HashMap<String, String> GetAllString(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        return (HashMap<String, String>) sharedPreferences.getAll();
    }
}
