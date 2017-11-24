package com.aspook.assists.hybrid;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;


interface OnReceivedErrorListener {
    void handleReceivedError(WebResourceRequest request, WebResourceError error);
}
