package com.aspook.assists.hybrid;

import android.webkit.WebView;

interface OnPageFinishedListener {
    void handlePageFinished(WebView view, String url);
}
