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
 * User shots and liked shots
 */
public class UserShotsActivity extends BaseActivity implements UserShotsContract.View {
    private UserShotsContract.Presenter mPresenter;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserShotsAdapter mAdapter;

    private int lastVisibleItemPosition = -1;
    private boolean isLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot);

        new UserShotsPresenter(this);
        mPresenter.getIntentData();

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
        mRecycleView = (RecyclerView) findViewById(R.id.rv_shots);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new UserShotsAdapter(this, mPresenter.getShotType());
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
                        mPresenter.startShotDetailActivity(v, position);
                    }
                }

                @Override
                public void onItemLongClicked(View v, int position) {

                }
            });
        }

    }

    @Override
    public void setPresenter(UserShotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setPageTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    @Override
    public void shotShots(List<Object> shots, int pageNum) {
        stopRefreshing();
        if (mAdapter != null) {
            if (pageNum > 1) {
                if (mPresenter.isAllPageLoaded()) {
                    mAdapter.changeLoadStatus(BaseAdapter.LOADING_NO_MORE);
                }
            }
            mAdapter.setData(shots);
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
