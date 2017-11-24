package com.aspook.assists.util;

import android.content.Context;
import android.content.Intent;

import com.aspook.assists.BuildConfig;
import com.aspook.assists.hybrid.HybridActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by ASPOOK on 16/10/27.
 */

public class HybridUtil {
    /**
     * the source page to start web page for special use
     */
    public static final int SOURCE_DEFUALT = 0;
    public static final int SOURCE_AUTHORIZATION = 1;

    private HybridUtil() {

    }

    public static void startHybridActivity(Context context, String url) {
        startHybridActivity(context, url, "");
    }

    /**
     * if don't want to use the title from H5 page, you can set your custom title
     *
     * @param context
     * @param url
     * @param title
     */
    public static void startHybridActivity(Context context, String url, String title) {
        startHybridActivity(context, url, title, SOURCE_DEFUALT);
    }

    public static void startHybridActivity(Context context, String url, int source) {
        startHybridActivity(context, url, "", source);
    }

    public static void startHybridActivity(Context context, String url, String title, int source) {
        if (BuildConfig.DEBUG) {
            Logger.e("open webview, url:" + url);
        }
        Intent intent = new Intent(context, HybridActivity.class);
        intent.putExtra("WebUrl", url);
        intent.putExtra("Title", title);
        intent.putExtra("Source", source);
        context.startActivity(intent);
    }

}
