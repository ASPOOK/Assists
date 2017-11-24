package com.aspook.assists.hybrid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by ASPOOK on 17/07/25.
 */

public class SafeWebView extends WebView {
    public SafeWebView(Context context) {
        super(context);
        removeSearchBoxJavaBridgeInterface();
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        removeSearchBoxJavaBridgeInterface();
    }

    public SafeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeSearchBoxJavaBridgeInterface();
    }

    @SuppressLint("NewApi")
    private void removeSearchBoxJavaBridgeInterface() {
        // delete injected JS object before 4.2
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            removeJavascriptInterface("searchBoxJavaBridge_");
        }
    }
}

