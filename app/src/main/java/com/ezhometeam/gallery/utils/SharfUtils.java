package com.ezhometeam.gallery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by n on 06/07/2017.
 */

public class SharfUtils {
    public static void increateDeniedNotAgainPermission(Context context, String permission, int increase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int current = preferences.getInt(permission, 0);
        preferences.edit().putInt(permission, current + increase).apply();
    }

    public static void saveDeniedNotAgainPermission(Context context, String permission, int number) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(permission, number).apply();
    }

    public static int getNumberDeniedNotAgainPermission(Context context, String permission) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(permission, 0);
    }

}

