package com.aspook.assists.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.util.FileManager;
import com.aspook.assists.util.HybridUtil;
import com.aspook.assists.util.SharePreferenceUtil;

public class SettingsActivity extends BaseActivity {

    private static final String SUPPORT_URL = "https://help.dribbble.com";
    private TextView tvMinimum;
    private TextView tvNormal;
    private TextView tvLarge;
    private TextView tvMaximum;
    private TextView tvLogout;
    private LinearLayout llAbout;
    private LinearLayout llSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViews();
        setListeners();
        initPageSzie();
    }

    public void findViews() {
        initToolbar();
        tvMinimum = (TextView) findViewById(R.id.tv_size_minimum);
        tvNormal = (TextView) findViewById(R.id.tv_size_normal);
        tvLarge = (TextView) findViewById(R.id.tv_size_large);
        tvMaximum = (TextView) findViewById(R.id.tv_size_maximum);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        llAbout = (LinearLayout) findViewById(R.id.ll_about);
        llSupport = (LinearLayout) findViewById(R.id.ll_support);
    }

    public void setListeners() {
        tvMinimum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageSize(12, tvMinimum.getId());
            }
        });
        tvNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageSize(20, tvNormal.getId());
            }
        });
        tvLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageSize(30, tvLarge.getId());
            }
        });
        tvMaximum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageSize(50, tvMaximum.getId());
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(SharePreferenceUtil.getOauthToken(SettingsActivity.this))) {
                    finish();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharePreferenceUtil.setOauthToken(SettingsActivity.this, "");
                            FileManager.clearWebViewCache();
                            MessageDispatcher.getInstance().sendMessageWithAction(MessageConstant.LOGOUT, Action.LOGOUT);
                        }
                    }).start();

                    finish();
                }
            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HybridUtil.startHybridActivity(SettingsActivity.this, SUPPORT_URL);
            }
        });
    }

    private void changePageSize(int size, int viewId) {
        SharePreferenceUtil.setShotsPageSize(this, size);
        changeSelectedStatus(viewId);
    }

    private void changeSelectedStatus(int viewId) {
        TextView[] textViews = {tvMinimum, tvNormal, tvLarge, tvMaximum};
        for (TextView tv : textViews) {
            if (tv.getId() == viewId) {
                tv.setBackgroundResource(R.drawable.bg_text_pink);
            } else {
                tv.setBackgroundDrawable(null);
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Settings");
    }

    public void initPageSzie() {
        int pageSize = SharePreferenceUtil.getShotsPageSize(this);
        if (pageSize == 12) {
            changeSelectedStatus(tvMinimum.getId());
        } else if (pageSize == 20) {
            changeSelectedStatus(tvNormal.getId());
        } else if (pageSize == 30) {
            changeSelectedStatus(tvLarge.getId());
        } else if (pageSize == 50) {
            changeSelectedStatus(tvMaximum.getId());
        }
    }
}
