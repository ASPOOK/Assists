package com.aspook.assists;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspook.assists.base.BaseFragment;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.message.MessageHandler;
import com.aspook.assists.message.OnMessageReceiveListener;
import com.aspook.assists.shot.ShotsFragment;
import com.aspook.assists.shot.ShotsPresenter;


/**
 * Created by ASPOOK on 17/6/15.
 */

public class HomeFragment extends BaseFragment {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Handler mHandler;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MessageHandler(new OnMessageReceiveListener() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case MessageConstant.LOGOUT:
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(0);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        MessageDispatcher.getInstance().registerHandlerWithAction(mHandler, hashCode(), Action.LOGOUT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the HomeFragment.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        // avoid loading too many images at one time
        //mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageDispatcher.getInstance().unregisterHandler(hashCode());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ShotsFragment
            ShotsFragment fragment = ShotsFragment.newInstance(position + 1);
            // Create the presenter
            new ShotsPresenter(fragment, position + 1);

            return fragment;
        }

        @Override
        public int getCount() {
            // total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_popular);
                case 1:
                    return getResources().getString(R.string.tab_recents);
                case 2:
                    return getResources().getString(R.string.tab_following);
                default:
                    break;
            }
            return null;
        }
    }
}
