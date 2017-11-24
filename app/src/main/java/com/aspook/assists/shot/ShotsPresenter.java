package com.aspook.assists.shot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;

import com.aspook.assists.R;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.entity.Token;
import com.aspook.assists.http.HttpClient;
import com.aspook.assists.http.HttpOauthClient;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.HybridUtil;
import com.aspook.assists.util.NetworkUtil;
import com.aspook.assists.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * working as Presenter in MVP pattern
 * <p>
 * Created by ASPOOK on 17/6/27.
 */

public class ShotsPresenter implements ShotsContract.Presenter {
    private static final String FOLLOWING = "following";
    // TODO please change to your own client id
    private static final String CLIENT_ID = "your_own_client_id";
    private static final String BASE_URL = "https://dribbble.com/oauth/authorize?";
    private int shotCategory;
    private int currentPageNum = 1;
    private int pageSize;
    private boolean isAllPageLoaded = false;
    private List<Shot> globalShots;
    private Context mContext;
    private Activity mHostActivity;

    private String type = null;
    private String period = null;
    private String sort = null;

    private ShotsContract.View mFragment;
    private CompositeDisposable mCompositeDisposable;

    public ShotsPresenter(ShotsContract.View view, int category) {
        this.mFragment = view;
        this.shotCategory = category;
        view.setPresenter(this);
        configParams();
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
    public void pullToRefresh() {
        reqData(1);
    }

    @Override
    public void loadNextPage() {
        reqData(currentPageNum + 1);
    }

    @Override
    public void startShotDetailActivity(View view, int position) {
        Shot shot = globalShots.get(position);
        if (shot != null) {
            Intent intent = new Intent(mContext, ShotDetailActivity.class);
            intent.putExtra("SHOT", shot);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mHostActivity, view, mContext.getString(R.string.transition_name));

            ActivityCompat.startActivity(mContext, intent, options.toBundle());
        }
    }

    @Override
    public boolean isAllPageLoaded() {
        return isAllPageLoaded;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
        mHostActivity = (Activity) context;
        pageSize = SharePreferenceUtil.getShotsPageSize(mContext);
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

    private void getAccessToken(String code) {
        if (TextUtils.isEmpty(code)) {
            mFragment.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
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
                SharePreferenceUtil.setOauthToken(mContext, token.getAccess_token());
                reqData(1);
                MessageDispatcher.getInstance().sendMessageWithAction(MessageConstant.AUTHENTICATED_FROM_FOLLOWING, Action.AUTHENTICATED_FROM_FOLLOWING);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mFragment.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        HttpOauthClient.getInstance().getAccessToken(observer, code);

    }

    private void configParams() {
        switch (shotCategory) {
            case Constant.POLULAR:
                type = null;
                break;
            case Constant.RECENTS:
                sort = "recent";
                type = null;
                break;
            case Constant.FOLLOWING:
                type = FOLLOWING;
                break;
            case Constant.DEBUTS:
                type = "debuts";
                break;
            case Constant.PLAYOFFS:
                type = "playoffs";
                break;
            case Constant.GIFS:
                type = "animated";
                break;
            case Constant.TEAMS:
                type = "teams";
                break;
            case Constant.REBOUNDS:
                type = "rebounds";
                break;
            default:
                break;
        }
    }

    private void reqData(final int pageNum) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mFragment.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<List<Shot>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<Shot> shots) {
                if (shots != null) {
                    if (shots.size() < pageSize) {
                        isAllPageLoaded = true;
                    }
                    if (globalShots == null) {
                        globalShots = new ArrayList<>();
                    }
                    if (pageNum == 1) {
                        globalShots.clear();
                    }
                    globalShots.addAll(shots);

                    mFragment.showShots(globalShots, pageNum);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mFragment.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {
                currentPageNum = pageNum;
            }
        };

        if (FOLLOWING.equals(type)) {
            String oAuthToken = SharePreferenceUtil.getOauthToken(mContext);
            if (!TextUtils.isEmpty(oAuthToken)) {
                HttpClient.getInstance().loadFollowingShots(observer, oAuthToken, pageNum, pageSize);
            }
        } else {
            HttpClient.getInstance().loadShots(observer, type, period, sort, pageNum, pageSize);
        }
    }

}
