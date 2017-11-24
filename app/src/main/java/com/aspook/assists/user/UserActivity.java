package com.aspook.assists.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.aspook.assists.R;
import com.aspook.assists.UserFragment;
import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.entity.User;

public class UserActivity extends BaseActivity {
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getIntentData();
        findViews();
        showFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getIntentData() {
        mUser = getIntent().getParcelableExtra("USER");
    }

    private void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserFragment mUserFragment = new UserFragment();
        fragmentTransaction.add(R.id.content, mUserFragment);
        Bundle bundle = new Bundle();
        bundle.putInt("FROM", UserFragment.FROM_USER_ACTIVITY);
        bundle.putParcelable("USER", mUser);
        mUserFragment.setArguments(bundle);

        fragmentTransaction.commitAllowingStateLoss();
    }

}
