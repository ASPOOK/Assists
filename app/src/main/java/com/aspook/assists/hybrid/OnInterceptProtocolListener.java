package com.aspook.assists.hybrid;

import android.webkit.WebView;

interface OnInterceptProtocolListener {
    void handleInterceptedProtocol(WebView view, String url);
}
