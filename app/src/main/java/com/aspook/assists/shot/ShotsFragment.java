package com.aspook.assists.shot;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseAdapter;
import com.aspook.assists.base.BaseFragment;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.listener.OnItemClickListener;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.message.MessageHandler;
import com.aspook.assists.message.OnMessageReceiveListener;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.SharePreferenceUtil;
import com.aspook.assists.util.ToastUtil;

import java.util.List;

/**
 * working as View in MVP pattern
 * <p>
 * Created by ASPOOK on 17/6/15.
 */

public class ShotsFragment extends BaseFragment implements ShotsContract.View {
    private Context mContext;
    private ShotsContract.Presenter mPresenter;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvSignIn;
    private ShotsAdapter mAdapter;
    private Handler mHandler;

    private int defaultStyle = Constant.GRIDLAYOUT;
    private int firstVisibleItemPosition = 0;
    private int lastVisibleItemPosition = -1;
    private boolean isBackFromDetail;
    private int shotCategory;

    /**
     * Returns a new instance of this fragment for the given category
     */
    public static ShotsFragment newInstance(int category) {
        ShotsFragment fragment = new ShotsFragment();
        Bundle args = new Bundle();
        args.putInt("SHOT_CATEGORY", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defaultStyle = getLayoutStyle();
        shotCategory = getArguments().getInt("SHOT_CATEGORY");
        mHandler = new MessageHandler(new OnMessageReceiveListener() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case MessageConstant.LAYOUT_STYLE_CHANGED:
                        switchLayoutStyle();
                        break;
                    case MessageConstant.OAUTH_CODE:
                        if (shotCategory == Constant.FOLLOWING) {
                            if (tvSignIn != null) {
                                tvSignIn.setVisibility(View.GONE);
                            }
                            if (mRecycleView != null) {
                                mRecycleView.setVisibility(View.VISIBLE);
                            }
                            if (mPresenter != null) {
                                mPresenter.reqAccessToken((String) msg.obj);
                                if (mSwipeRefreshLayout != null) {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                }
                            }
                        }
                        break;
                    case MessageConstant.LOGOUT:
                        if (shotCategory == Constant.FOLLOWING) {
                            if (tvSignIn != null) {
                                tvSignIn.setVisibility(View.VISIBLE);
                            }
                            if (mRecycleView != null) {
                                mRecycleView.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case MessageConstant.AUTHENTICATED_FROM_USER:
                        if (shotCategory == Constant.FOLLOWING) {
                            if (mRecycleView != null) {
                                mRecycleView.setVisibility(View.VISIBLE);
                            }
                            if (mPresenter != null) {
                                mPresenter.start();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        MessageDispatcher.getInstance().registerHandlerWithAction(mHandler, hashCode(),
                Action.LOGOUT,
                Action.OAUTH_CODE,
                Action.LAYOUT_STYLE_CHANGED,
                Action.AUTHENTICATED_FROM_USER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
        if (mPresenter != null) {
            mPresenter.setContext(mContext);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MessageDispatcher.getInstance().unregisterHandler(hashCode());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shots, container, false);
        tvSignIn = (TextView) rootView.findViewById(R.id.tv_sign_in);
        initRecycleView(rootView);

        setListeners();

        return rootView;
    }

    @Override
    public void setPresenter(ShotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (shotCategory == Constant.FOLLOWING && TextUtils.isEmpty(SharePreferenceUtil.getOauthToken(mContext))) {
            return;
        }
        if (isBackFromDetail) {
            isBackFromDetail = false;
            return;
        }
        if (mPresenter != null) {
            if (!isBackFromDetail) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                mPresenter.start();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && shotCategory == Constant.FOLLOWING) {
            if (mContext == null) {
                return;
            }
            String oAuthToken = SharePreferenceUtil.getOauthToken(mContext);
            if (TextUtils.isEmpty(oAuthToken)) {
                if (tvSignIn != null) {
                    tvSignIn.setVisibility(View.VISIBLE);
                }
                if (mRecycleView != null) {
                    mRecycleView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void showShots(List<Shot> shots, int pageNum) {
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
            ToastUtil.toastShort(mContext, ErrorConstant.NETWORK_ERROR_MSG);
        } else if (errorType == ErrorConstant.DATA_ERROR) {
            ToastUtil.toastShort(mContext, ErrorConstant.DATA_ERROR_MSG);
        }
    }

    private void initRecycleView(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_pull_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_shots);
        if (defaultStyle == Constant.GRIDLAYOUT) {
            mLayoutManager = new GridLayoutManager(mContext, 2);
        } else {
            mLayoutManager = new LinearLayoutManager(mContext);
        }
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new ShotsAdapter(mContext);
        mAdapter.setLayoutStyle(defaultStyle);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    // get last saved layout style
    private int getLayoutStyle() {
        return SharePreferenceUtil.getLayoutStyle(mContext);
    }

    private void changeLayoutStyle(int style) {
        if (mLayoutManager != null && mRecycleView != null && mAdapter != null) {
            if (style == Constant.LINEARLAYOUT) {
                mLayoutManager = new LinearLayoutManager(mContext);
                mRecycleView.setLayoutManager(mLayoutManager);
                mAdapter.setLayoutStyle(Constant.LINEARLAYOUT);
                mAdapter.notifyDataSetChanged();
            } else if (style == Constant.GRIDLAYOUT) {
                mLayoutManager = new GridLayoutManager(mContext, 2);
                mRecycleView.setLayoutManager(mLayoutManager);
                mAdapter.setLayoutStyle(Constant.GRIDLAYOUT);
                mAdapter.notifyDataSetChanged();
            }

            mRecycleView.smoothScrollToPosition(firstVisibleItemPosition);
        }
    }

    // switch the layout style
    private void switchLayoutStyle() {
        if (mLayoutManager != null && mRecycleView != null && mAdapter != null) {
            if (defaultStyle == Constant.GRIDLAYOUT) {
                defaultStyle = Constant.LINEARLAYOUT;
            } else if (defaultStyle == Constant.LINEARLAYOUT) {
                defaultStyle = Constant.GRIDLAYOUT;
            }

            changeLayoutStyle(defaultStyle);
            SharePreferenceUtil.setLayoutStyle(mContext, defaultStyle);
        }
    }

    private void setListeners() {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(View v, int position) {
                    if (mPresenter != null) {
                        mPresenter.startShotDetailActivity(v, position);
                        isBackFromDetail = true;
                    }
                }

                @Override
                public void onItemLongClicked(View v, int position) {
                    // do nothing
                }
            });
        }

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
                if (mLayoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    firstVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                } else if (mLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    firstVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                }

            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.reqOAuthCode();
            }
        });

    }

    private void stopRefreshing() {
        if (mSwipeRefreshLayout != null) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter.clearDisposable();
        }
    }
}
