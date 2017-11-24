package com.aspook.assists.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aspook.assists.shot.Constant;

/**
 * SharePreference Tool
 * <p>
 * Created by ASPOOK on 17/6/30.
 */

public class SharePreferenceUtil {
    private static final String LAYUT_STYLE = "layoutStyle";
    private static final String OAUTH_TOKEN = "OauthToken";
    private static final String SHOTS_PAGE_SIZE = "ShotsPageSize";

    private SharePreferenceUtil() {
    }

    public static void setLayoutStyle(Context context, int style) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(LAYUT_STYLE, style);
        editor.commit();
    }

    public static int getLayoutStyle(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(LAYUT_STYLE, Constant.GRIDLAYOUT);
    }

    public static void setOauthToken(Context context, String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(OAUTH_TOKEN, token);
        editor.commit();
    }

    public static String getOauthToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(OAUTH_TOKEN, "");
    }

    public static void setShotsPageSize(Context context, int size) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SHOTS_PAGE_SIZE, size);
        editor.commit();
    }

    public static int getShotsPageSize(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(SHOTS_PAGE_SIZE, 20);
    }
}
