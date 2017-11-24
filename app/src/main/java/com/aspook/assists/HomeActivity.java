package com.aspook.assists;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.message.MessageHandler;
import com.aspook.assists.message.OnMessageReceiveListener;
import com.aspook.assists.settings.SettingsActivity;
import com.aspook.assists.shot.Constant;
import com.aspook.assists.util.SharePreferenceUtil;
import com.aspook.assists.util.ToastUtil;

public class HomeActivity extends BaseActivity {

    private static final int NAVIGATION_HOME = 1;
    private static final int NAVIGATION_EXPLORE = 2;
    private static final int NAVIGATION_USER = 3;

    private static final long WAIT_TIME = 2000;
    private static long touchTime = 0;

    private HomeFragment mHomeFragment;
    private ExploreFragment mExploreFragment;
    private UserFragment mUserFragment;
    private Menu mMenu;
    private Handler mHandler;
    private BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        switchFragment(NAVIGATION_HOME);

        mHandler = new MessageHandler(new OnMessageReceiveListener() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case MessageConstant.LOGOUT:
                        mNavigation.setSelectedItemId(R.id.navigation_home);
                        switchFragment(NAVIGATION_HOME);
                        break;
                    default:
                        break;
                }
            }
        });

        MessageDispatcher.getInstance().registerHandlerWithAction(mHandler, hashCode(), Action.LOGOUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        mMenu = menu;
        showMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.list_style:
                MessageDispatcher.getInstance().sendMessageWithAction(MessageConstant.LAYOUT_STYLE_CHANGED, Action.LAYOUT_STYLE_CHANGED);
                item.setVisible(false);
                mMenu.findItem(R.id.grid_style).setVisible(true);
                break;
            case R.id.grid_style:
                MessageDispatcher.getInstance().sendMessageWithAction(MessageConstant.LAYOUT_STYLE_CHANGED, Action.LAYOUT_STYLE_CHANGED);
                item.setVisible(false);
                mMenu.findItem(R.id.list_style).setVisible(true);
                break;
            case R.id.settings:
                startSettingsActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= WAIT_TIME) {
                ToastUtil.toastShort(this, "Press again to exit");
                touchTime = currentTime;
            } else {
                MessageDispatcher.getInstance().releaseAll();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void switchFragment(int navigationIndex) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (navigationIndex) {
            case NAVIGATION_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content, mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }

                if (mExploreFragment != null) {
                    fragmentTransaction.hide(mExploreFragment);
                }
                if (mUserFragment != null) {
                    fragmentTransaction.hide(mUserFragment);
                }

                fragmentTransaction.commitAllowingStateLoss();

                break;
            case NAVIGATION_EXPLORE:
                if (mExploreFragment == null) {
                    mExploreFragment = new ExploreFragment();
                    fragmentTransaction.add(R.id.content, mExploreFragment);
                } else {
                    fragmentTransaction.show(mExploreFragment);
                }

                if (mHomeFragment != null) {
                    fragmentTransaction.hide(mHomeFragment);
                }
                if (mUserFragment != null) {
                    fragmentTransaction.hide(mUserFragment);
                }

                fragmentTransaction.commitAllowingStateLoss();

                break;
            case NAVIGATION_USER:
                if (mUserFragment == null) {
                    mUserFragment = new UserFragment();
                    fragmentTransaction.add(R.id.content, mUserFragment);
                } else {
                    fragmentTransaction.show(mUserFragment);
                }

                if (mHomeFragment != null) {
                    fragmentTransaction.hide(mHomeFragment);
                }
                if (mExploreFragment != null) {
                    fragmentTransaction.hide(mExploreFragment);
                }

                fragmentTransaction.commitAllowingStateLoss();

                break;
            default:
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showMenu();
                    switchFragment(NAVIGATION_HOME);
                    return true;
                case R.id.navigation_explore:
                    showMenu();
                    switchFragment(NAVIGATION_EXPLORE);
                    return true;
                case R.id.navigation_user:
                    hideMenu();
                    switchFragment(NAVIGATION_USER);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    public void showMenu() {
        if (mMenu != null) {
            if (SharePreferenceUtil.getLayoutStyle(this) == Constant.GRIDLAYOUT) {
                mMenu.findItem(R.id.list_style).setVisible(true);
            } else {
                mMenu.findItem(R.id.grid_style).setVisible(true);
            }
            mMenu.findItem(R.id.settings).setVisible(false);
        }
    }

    public void hideMenu() {
        if (mMenu != null) {
            mMenu.findItem(R.id.grid_style).setVisible(false);
            mMenu.findItem(R.id.list_style).setVisible(false);
            mMenu.findItem(R.id.settings).setVisible(true);
        }
    }

    public void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageDispatcher.getInstance().unregisterHandler(hashCode());
    }
}
