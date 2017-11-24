package com.aspook.assists.user;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.base.BaseAdapter;
import com.aspook.assists.listener.OnItemClickListener;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.ToastUtil;

import java.util.List;

/**
 * Follower and Followee
 */
public class FollowActivity extends BaseActivity implements FollowContract.View {
    private FollowContract.Presenter mPresenter;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FollowAdapter mAdapter;

    private int lastVisibleItemPosition = -1;
    private boolean isLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        new FollowPresenter(this);
        if (mPresenter != null) {
            mPresenter.getIntentData();
        }

        findViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            if (!isLoaded) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                mPresenter.start();
                isLoaded = true;
            }
        }
    }

    @Override
    public void setPresenter(FollowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setPageTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    @Override
    public void showFollowInfo(List<Object> followObjects, int pageNum) {
        stopRefreshing();
        if (mAdapter != null) {
            if (pageNum > 1) {
                if (mPresenter.isAllPageLoaded()) {
                    mAdapter.changeLoadStatus(BaseAdapter.LOADING_NO_MORE);
                }
            }
            mAdapter.setData(followObjects);
        }
    }

    @Override
    public void showErrorView(int errorType) {
        stopRefreshing();
        if (errorType == ErrorConstant.NETWORK_ERROR) {
            ToastUtil.toastShort(this, ErrorConstant.NETWORK_ERROR_MSG);
        } else if (errorType == ErrorConstant.DATA_ERROR) {
            ToastUtil.toastShort(this, ErrorConstant.DATA_ERROR_MSG);
        }
    }

    public void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_pull_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRecycleView = (RecyclerView) findViewById(R.id.rv_follow);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new FollowAdapter(this, mPresenter.getFollowType());
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setListeners() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPresenter.pullToRefresh();
                }
            });
        }

        mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == mAdapter.getItemCount() - 1) {
                    if (mPresenter.isAllPageLoaded()) {
                        mAdapter.changeLoadStatus(BaseAdapter.LOADING_NO_MORE);
                        return;
                    }

                    mAdapter.changeLoadStatus(BaseAdapter.LOADING_START);
                    mPresenter.loadNextPage();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }
        });

        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(View v, int position) {
                    if (mPresenter != null) {
                        mPresenter.startUserActivity(position);
                    }
                }

                @Override
                public void onItemLongClicked(View v, int position) {

                }
            });
        }

    }

    private void stopRefreshing() {
        if (mSwipeRefreshLayout != null) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clearDisposable();
        }
    }
}
