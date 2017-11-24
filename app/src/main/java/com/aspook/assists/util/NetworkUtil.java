package com.aspook.assists.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * check newwork status
 * <p>
 * Created by ASPOOK on 17/6/30.
 */

public class NetworkUtil {
    private NetworkUtil() {
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info == null) {
                return false;
            }

            return info.isAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
