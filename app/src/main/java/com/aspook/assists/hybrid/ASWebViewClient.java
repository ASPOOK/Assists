package com.aspook.assists.hybrid;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aspook.assists.base.BaseActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by ASPOOK on 17/07/25.
 */

class ASWebViewClient extends WebViewClient {

    private BaseActivity hostActivity;
    private OnReceivedErrorListener mReceivedErrorListener;
    private OnPageFinishedListener mPageFinishedListener;
    private OnInterceptProtocolListener mInterceptProtocalListener;

    // TODO please change to your own callback url
    private static final String CALL_BACK = "your_own_callback_url";

    ASWebViewClient(BaseActivity activity) {
        hostActivity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.d("shouldOverrideUrlLoading:" + url);
        if (url.startsWith(CALL_BACK)) {
            if (mInterceptProtocalListener != null) {
                mInterceptProtocalListener.handleInterceptedProtocol(view, url);
                return true;
            }
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Logger.d("shouldInterceptRequest:" + request.getUrl());
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Logger.d("onPageStarted:" + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Logger.d("onPageFinished:" + url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Logger.d("onReceivedError-" + "error code:" + error.getErrorCode() + ",error description:" + error.getDescription());
        }
        if (mReceivedErrorListener != null) {
            mReceivedErrorListener.handleReceivedError(request, error);
        } else {
            super.onReceivedError(view, request, error);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }


    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    void setOnReceivedErrorListener(OnReceivedErrorListener l) {
        mReceivedErrorListener = l;
    }

    void setOnPageFinishedListener(OnPageFinishedListener l) {
        mPageFinishedListener = l;
    }

    void setOnInterceptProtocolListener(OnInterceptProtocolListener l) {
        mInterceptProtocalListener = l;
    }
}
