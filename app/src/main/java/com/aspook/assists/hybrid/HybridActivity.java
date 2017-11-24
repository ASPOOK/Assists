package com.aspook.assists.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.util.HybridUtil;

/**
 * accept: str-Title; str-WebUrl;
 */
public class HybridActivity extends BaseActivity {

    private ASWebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mTitle;

    private String title;
    private String webUrl;
    private int source;
    private boolean hasCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid);

        getIntentData();
        findViews();
        loadWebPage();
    }

    public void findViews() {
        setupToolBar();
        mWebView = (ASWebView) findViewById(R.id.uni_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTitle = (TextView) findViewById(R.id.tv_toolbar_title);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.stopLoading();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                goBackByWebView();
                break;
            case R.id.webview_close:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goBackByWebView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle("");
    }

    private void getIntentData() {
        Intent intent = getIntent();
        webUrl = intent.getStringExtra("WebUrl");
        title = intent.getStringExtra("Title");
        source = intent.getIntExtra("Source", HybridUtil.SOURCE_DEFUALT);
    }

    private void loadWebPage() {
        mWebView.setHostActivity(this);
        // must before setHostActivity
        mWebView.init();
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            hasCustomTitle = true;
        }

        if (!TextUtils.isEmpty(webUrl)) {
            mWebView.loadUrl(webUrl);
        }
    }

    /**
     * set title for web page
     *
     * @param title this title is extracted form H5
     */
    public void setToolBarTitle(String title) {
        if (hasCustomTitle || mTitle == null) {
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        } else {
            mTitle.setText("Assists");
        }
    }

    public void goBackByWebView() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            mWebView.loadUrl("about:blank");
            finish();
        }
    }

    public void changeProgress(int newProgress) {
        if (mProgressBar != null && newProgress > 0) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }
    }

}
