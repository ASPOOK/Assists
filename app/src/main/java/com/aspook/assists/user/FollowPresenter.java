package com.aspook.assists.user;

import android.app.Activity;
import android.content.Intent;

import com.aspook.assists.entity.FolloweeWrapper;
import com.aspook.assists.entity.FollowerWrapper;
import com.aspook.assists.entity.User;
import com.aspook.assists.http.HttpClient;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by ASPOOK on 17/7/19.
 */

public class FollowPresenter implements FollowContract.Presenter {
    private static final int PAGE_SIZE = 20;
    public static final int FOLLOWER = 1;
    public static final int FOLLOWING = 2;

    private FollowContract.View mFollowActivity;
    private Activity mActivity;

    private int mUserId;
    private int mType;
    private int currentPageNum = 1;
    private boolean isAllPageLoaded = false;
    private List<Object> globalFollowObjects;
    private CompositeDisposable mCompositeDisposable;

    FollowPresenter(FollowContract.View view) {
        this.mFollowActivity = view;
        view.setPresenter(this);
        mActivity = (Activity) mFollowActivity;
    }

    @Override
    public void start() {
        reqData(1);
    }

    @Override
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void getIntentData() {
        mUserId = mActivity.getIntent().getIntExtra("USER_ID", -1);
        mType = mActivity.getIntent().getIntExtra("FOLLOW_TYPE", -1);
        setTitle();
    }

    @Override
    public boolean isAllPageLoaded() {
        return isAllPageLoaded;
    }

    @Override
    public void loadNextPage() {
        reqData(currentPageNum + 1);
    }

    @Override
    public void pullToRefresh() {
        reqData(1);
    }

    @Override
    public int getFollowType() {
        return mType;
    }

    @Override
    public void startUserActivity(int position) {
        if (globalFollowObjects != null) {
            User user = null;
            if (mType == FollowPresenter.FOLLOWER) {
                FollowerWrapper followerWrapper = (FollowerWrapper) globalFollowObjects.get(position);
                user = followerWrapper.getFollower();
            } else if (mType == FollowPresenter.FOLLOWING) {
                FolloweeWrapper followeeWrapper = (FolloweeWrapper) globalFollowObjects.get(position);
                user = followeeWrapper.getFollowee();
            }

            if (user != null) {
                Intent intent = new Intent(mActivity, UserActivity.class);
                intent.putExtra("USER", user);
                mActivity.startActivity(intent);
            }
        }
    }

    private void reqFollowerData(final int pageNum) {
        Observer observer = new Observer<List<FollowerWrapper>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<FollowerWrapper> followers) {
                if (followers != null) {
                    if (followers.size() < PAGE_SIZE) {
                        isAllPageLoaded = true;
                    }
                    if (globalFollowObjects == null) {
                        globalFollowObjects = new ArrayList<>();
                    }
                    if (pageNum == 1) {
                        globalFollowObjects.clear();
                    }
                    globalFollowObjects.addAll(followers);

                    mFollowActivity.showFollowInfo(globalFollowObjects, pageNum);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mFollowActivity.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {
                currentPageNum = pageNum;
            }
        };

        HttpClient.getInstance().loadFollowers(observer, mUserId, pageNum, PAGE_SIZE);
    }

    private void reqFollowingData(final int pageNum) {
        Observer observer = new Observer<List<FolloweeWrapper>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<FolloweeWrapper> followings) {
                if (followings != null) {
                    if (followings.size() < PAGE_SIZE) {
                        isAllPageLoaded = true;
                    }
                    if (globalFollowObjects == null) {
                        globalFollowObjects = new ArrayList<>();
                    }
                    if (pageNum == 1) {
                        globalFollowObjects.clear();
                    }
                    globalFollowObjects.addAll(followings);

                    mFollowActivity.showFollowInfo(globalFollowObjects, pageNum);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mFollowActivity.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {
                currentPageNum = pageNum;
            }
        };

        HttpClient.getInstance().loadFollowing(observer, mUserId, pageNum, PAGE_SIZE);
    }

    private void reqData(int pageNum) {
        if (NetworkUtil.isNetworkAvailable(mActivity)) {
            if (mUserId > 0) {
                if (mType == FOLLOWER) {
                    reqFollowerData(pageNum);
                } else if (mType == FOLLOWING) {
                    reqFollowingData(pageNum);
                }
            } else {
                mFollowActivity.showErrorView(ErrorConstant.DATA_ERROR);
            }
        } else {
            mFollowActivity.showErrorView(ErrorConstant.NETWORK_ERROR);
        }
    }

    private void setTitle() {
        if (mType == FOLLOWER) {
            mFollowActivity.setPageTitle("Followers");
        } else if (mType == FOLLOWING) {
            mFollowActivity.setPageTitle("Following");
        }
    }


}
