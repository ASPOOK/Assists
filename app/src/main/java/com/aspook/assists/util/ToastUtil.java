package com.aspook.assists.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * ToastUtil
 * <p>
 * Created by ASPOOK on 17/6/30.
 */

public class ToastUtil {
    private ToastUtil() {
    }

    public static void toastShort(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
