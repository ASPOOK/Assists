package com.aspook.assists.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.util.HybridUtil;

public class AboutActivity extends BaseActivity {

    private static final String GITHUB = "https://github.com/aspook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();
        findViewById(R.id.tv_copyright).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HybridUtil.startHybridActivity(AboutActivity.this, GITHUB);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("About");
    }
}
