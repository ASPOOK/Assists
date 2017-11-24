package com.aspook.assists.hybrid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.util.ToastUtil;

/**
 * Created by ASPOOK on 17/7/25.
 */

public class ASWebView extends SafeWebView {
    /**
     * host activity
     */
    private BaseActivity hostActivity;

    // TODO please change to your own callback url
    private static final String CALL_BACK_OK = "your_own_callback_url?code=";
    private static final String CALL_BACK_ERROR = "your_own_callback_url?error=";

    public ASWebView(Context context) {
        super(context);
        setup();
    }

    public ASWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public ASWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void setup() {
        setBackgroundColor(Color.WHITE);
        WebSettings ws = getSettings();
        setupWebSettings(ws);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setHorizontalScrollBarEnabled(true);
        setVerticalScrollBarEnabled(true);
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    startBrowser(url);
                    if (hostActivity != null) {
                        ToastUtil.toastShort(hostActivity, "starting browser to download!");
                        hostActivity.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void startBrowser(String url) {
        if (url == null) {
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (hostActivity != null) {
                hostActivity.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set custom WebViewClient、WebChromeClient for WebView
     *
     * @param mWebViewClient
     * @param mWebChromeClient
     */
    public void init(ASWebViewClient mWebViewClient, ASWebChromeClient mWebChromeClient) {
        if (mWebViewClient == null) {
            mWebViewClient = new ASWebViewClient(hostActivity);

            mWebViewClient.setOnInterceptProtocolListener(new OnInterceptProtocolListener() {
                @Override
                public void handleInterceptedProtocol(WebView view, String url) {
                    if (url.contains(CALL_BACK_OK)) {
                        String code = url.replace(CALL_BACK_OK, "");
                        if (!TextUtils.isEmpty(code)) {
                            MessageDispatcher.getInstance().sendMessageWithAction(MessageConstant.OAUTH_CODE, code, Action.OAUTH_CODE);
                        }
                    } else if (url.contains(CALL_BACK_ERROR)) {
                        String[] arr = url.replace(CALL_BACK_ERROR, "").split("&");
                        if (arr.length > 1) {
                            ToastUtil.toastShort(hostActivity, arr[0]);
                        }
                    }
                    if (hostActivity != null) {
                        hostActivity.finish();
                    }
                }
            });

            mWebViewClient.setOnPageFinishedListener(new OnPageFinishedListener() {
                @Override
                public void handlePageFinished(WebView view, String url) {

                }
            });

            mWebViewClient.setOnReceivedErrorListener(new OnReceivedErrorListener() {
                @Override
                public void handleReceivedError(WebResourceRequest request, WebResourceError error) {
                    if (hostActivity != null) {
                        ToastUtil.toastShort(hostActivity, "loading error!");
                        if (canGoBack()) {
                            goBack();
                        } else {
                            hostActivity.finish();
                        }
                    }
                }
            });
        }
        setWebViewClient(mWebViewClient);

        if (mWebChromeClient == null) {
            mWebChromeClient = new ASWebChromeClient(hostActivity);
            mWebChromeClient.setOnProgressChangedListener(new OnProgressChangedListener() {
                @Override
                public void handleProgressChanged(int newProgress) {
                    // show web page loading status
                    if (hostActivity instanceof HybridActivity) {
                        ((HybridActivity) hostActivity).changeProgress(newProgress);
                    }
                }
            });
        }
        setWebChromeClient(mWebChromeClient);
    }

    /**
     * use default WebViewClient and WebChromeClient
     */
    public void init() {
        init(null, null);
    }

    /**
     * set custom WebChromeClient for WebView，default WebViewClient
     *
     * @param mWebChromeClient
     */
    public void init(ASWebChromeClient mWebChromeClient) {
        init(null, mWebChromeClient);
    }

    /**
     * set custom WebViewClient for WebView，default WebChromeClient
     *
     * @param mWebViewClient
     */
    public void init(ASWebViewClient mWebViewClient) {
        init(mWebViewClient, null);
    }

    protected void setupWebSettings(WebSettings ws) {
        ws.setDefaultTextEncodingName("utf-8");
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
        ws.setDisplayZoomControls(false);

        ws.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ws.setAllowFileAccessFromFileURLs(true);
            ws.setAllowUniversalAccessFromFileURLs(true);
        }

        // enable html cache
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        ws.setAppCacheMaxSize(1024 * 1024 * 8);
        ws.setAppCachePath("/data/data/" + getContext().getPackageName() + "/cache");

        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ws.setUseWideViewPort(true);
        }

        // support http and https mixed in one page
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public void setHostActivity(BaseActivity hostActivity) {
        this.hostActivity = hostActivity;
    }

    public void callJS(String jsFunc) {
        loadUrl("javascript:" + jsFunc);
    }
}
