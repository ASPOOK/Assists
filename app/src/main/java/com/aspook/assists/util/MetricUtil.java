package com.aspook.assists.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class MetricUtil {
    private MetricUtil() {
    }

    public static int dp2px(Context context, int dpVal) {
        return (int) (dpVal * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dp(Context context, int pxVal) {
        return (int) (pxVal / context.getResources().getDisplayMetrics().density);
    }

    public static int sp2px(Context cOntext, int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                , spVal, cOntext.getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context, int pxVal) {
        return (int) (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * return the size of screen in pixel
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        int[] wh = new int[2];
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        wh[0] = metrics.widthPixels;
        wh[1] = metrics.heightPixels;

        return wh;
    }

    /**
     * return the density of screen
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.density;
    }

    /**
     * return the height of statusBar
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

}
