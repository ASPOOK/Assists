package com.aspook.assists.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.aspook.assists.entity.Token;
import com.aspook.assists.entity.User;
import com.aspook.assists.http.HttpClient;
import com.aspook.assists.http.HttpOauthClient;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.HybridUtil;
import com.aspook.assists.util.NetworkUtil;
import com.aspook.assists.util.SharePreferenceUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by ASPOOK on 17/7/7.
 */

public class UserPresenter implements UserContract.Presenter {
    // TODO please change to your own client id
    private static final String CLIENT_ID = "your_own_client_id";
    private static final String BASE_URL = "https://dribbble.com/oauth/authorize?";
    private static final int FOLLOW = 1;
    private static final int UNFOLLOW = 2;
    private CompositeDisposable mCompositeDisposable;
    private UserContract.View mUserFragment;
    private Context mContext;
    private Fragment mFragment;
    private User mUser;
    private int from;
    private boolean isFollowing;

    public UserPresenter(UserContract.View view, Context context) {
        this.mUserFragment = view;
        view.setPresenter(this);
        mContext = context;
        mFragment = (Fragment) view;
    }

    @Override
    public void start() {
        if (mUser != null) {
            mUserFragment.showUserInfo(mUser);
            if (!NetworkUtil.isNetworkAvailable(mContext)) {
                mUserFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
            }
        } else {
            mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
        }
    }

    @Override
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void getData() {
        Bundle bundle = mFragment.getArguments();
        if (bundle != null) {
            mUser = bundle.getParcelable("USER");
            from = bundle.getInt("FROM", 0);
        }

    }

    @Override
    public void startFollowActivity(int followType) {
        Intent intent = new Intent(mContext, FollowActivity.class);
        intent.putExtra("USER_ID", mUser.getId());
        intent.putExtra("FOLLOW_TYPE", followType);
        mContext.startActivity(intent);
    }

    @Override
    public void startShotActivity(int shotType) {
        Intent intent = new Intent(mContext, UserShotsActivity.class);
        intent.putExtra("USER", mUser);
        intent.putExtra("SHOT_TYPE", shotType);
        mContext.startActivity(intent);
    }

    @Override
    public void reqOAuthCode() {
        String reqUrl = BASE_URL + "client_id=" + CLIENT_ID + "&scope=public+write+comment+upload";
        HybridUtil.startHybridActivity(mContext, reqUrl, HybridUtil.SOURCE_AUTHORIZATION);
    }

    @Override
    public void reqAccessToken(String code) {
        getAccessToken(code);
    }

    @Override
    public void loadUserData(String token) {
        getUserInfo(token);
    }

    @Override
    public boolean isFromUserActivity() {
        return from == 1;
    }

    @Override
    public void checkFollowStatus() {
        checkFollowing(mUser.getId());
    }

    @Override
    public void processFollow() {
        if (isFollowing) {
            processFollow(mUser.getId(), UNFOLLOW);
        } else {
            processFollow(mUser.getId(), FOLLOW);
        }
    }

    private void checkFollowing(int userId) {
        if (userId <= 0) {
            mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mUserFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<Response<String>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Response<String> response) {
                if (response != null) {
                    // 204-Following,404-unFollow
                    int code = response.code();
                    if (code == 204) {
                        isFollowing = true;
                        mUserFragment.changeFollowStatus(true);
                    } else if (code == 404) {
                        isFollowing = false;
                        mUserFragment.changeFollowStatus(false);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        HttpClient.getInstance().checkFollowing(observer, userId, SharePreferenceUtil.getOauthToken(mContext));
    }

    private void processFollow(int userId, final int type) {
        if (userId <= 0) {
            mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mUserFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<Response<String>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Response<String> response) {
                if (response != null) {
                    // 204-Following,404-unFollow
                    int code = response.code();
                    if (code == 204) {
                        if (type == FOLLOW) {
                            isFollowing = true;
                            mUserFragment.showFollowRes(true);
                        } else if (type == UNFOLLOW) {
                            isFollowing = false;
                            mUserFragment.showUnFollowRes(true);
                        }
                    } else {
                        if (type == FOLLOW) {
                            mUserFragment.showFollowRes(false);
                        } else if (type == UNFOLLOW) {
                            mUserFragment.showUnFollowRes(false);
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        if (type == FOLLOW) {
            HttpClient.getInstance().follow(observer, userId, SharePreferenceUtil.getOauthToken(mContext));
        } else if (type == UNFOLLOW) {
            HttpClient.getInstance().unFollow(observer, userId, SharePreferenceUtil.getOauthToken(mContext));
        }
    }

    private void getAccessToken(String code) {
        if (TextUtils.isEmpty(code)) {
            mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mUserFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<Token>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Token token) {
                loadUserData(token.getAccess_token());
                SharePreferenceUtil.setOauthToken(mContext, token.getAccess_token());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        HttpOauthClient.getInstance().getAccessToken(observer, code);
    }

    private void getUserInfo(String token) {
        if (TextUtils.isEmpty(token)) {
            mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mUserFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<User>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull User user) {
                mUser = user;
                mUserFragment.showUserInfo(mUser);
                MessageDispatcher.getInstance().sendMessageWithAction(MessageConstant.AUTHENTICATED_FROM_USER, Action.AUTHENTICATED_FROM_USER);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mUserFragment.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        HttpClient.getInstance().loadAuthenticatedUser(observer, token);
    }

}
