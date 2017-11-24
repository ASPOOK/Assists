package com.aspook.assists.hybrid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.aspook.assists.base.BaseActivity;


/**
 * Created by ASPOOK on 17/07/25.
 */

class ASWebChromeClient extends WebChromeClient {

    private OnProgressChangedListener mListener;
    private BaseActivity hostActivity;

    ASWebChromeClient(BaseActivity activity) {
        hostActivity = activity;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        try {
            if (hostActivity.isFinishing()) {
                result.cancel();
                return true;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(hostActivity);
            builder.setTitle("Tips");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok,
                    new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
            builder.setCancelable(false);
            builder.create();
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            result.cancel();
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mListener != null) {
            mListener.handleProgressChanged(newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (hostActivity instanceof HybridActivity) {
            ((HybridActivity) hostActivity).setToolBarTitle(title);
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    void setOnProgressChangedListener(OnProgressChangedListener l) {
        mListener = l;
    }
}
