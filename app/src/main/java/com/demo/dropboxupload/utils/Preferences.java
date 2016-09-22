package com.demo.dropboxupload.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Eli on 9/20/2016.
 * Utility class that sets and gets data values from SharedPreferences
 */

public class Preferences {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "connect.sid";

    public static void setPreference(String label, ArrayList<String> keyArray, Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(label, new HashSet<String>(keyArray))
                .apply();
    }

    public static ArrayList<String> getPreference(String label, Context context, ArrayList<String> backup) {
        return new ArrayList<>(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getStringSet(label, new HashSet<String>(backup)));
    }

    public static void setPreference(String label, String key, Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(label, key)
                .apply();
    }

    public static String getPreference(String label, Context context, String backup) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(label, backup);
    }

    public static void setPreference(String label, int key, Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putInt(label, key)
                .apply();
    }

    public static int getPreference(String label, Context context, int backup) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(label, backup);
    }

    public static void setCookie(Map<String, String> headers, Context context) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            setPreference(COOKIE_KEY, headers.get(SET_COOKIE_KEY), context);
        }
    }

    public static String getCookie(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(COOKIE_KEY, "");
    }

    public static void setPreference(String label, boolean key, Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(label, key)
                .apply();
    }

    public static boolean getPreference(String label, Context context, boolean backup) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(label, backup);
    }

}