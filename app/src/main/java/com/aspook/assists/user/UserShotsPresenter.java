package com.aspook.assists.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.aspook.assists.R;
import com.aspook.assists.entity.PureShot;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.entity.ShotWrapper;
import com.aspook.assists.entity.User;
import com.aspook.assists.http.HttpClient;
import com.aspook.assists.shot.ShotDetailActivity;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.NetworkUtil;
import com.aspook.assists.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by ASPOOK on 17/7/19.
 */

public class UserShotsPresenter implements UserShotsContract.Presenter {
    public static final int SELF = 1;
    public static final int LIKED = 2;

    private UserShotsContract.View mShotActivity;
    private Context mContext;
    private Activity mActivity;
    private int mUserId;
    private User mUser;
    private int mType;

    private int pageSize;
    private int currentPageNum = 1;
    private boolean isAllPageLoaded = false;

    private List<Object> globalShotObjects;
    private CompositeDisposable mCompositeDisposable;


    UserShotsPresenter(UserShotsContract.View view) {
        this.mShotActivity = view;
        view.setPresenter(this);
        mContext = (Context) mShotActivity;
        mActivity = (Activity) mShotActivity;
        pageSize = SharePreferenceUtil.getShotsPageSize(mContext);
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
        mUser = mActivity.getIntent().getParcelableExtra("USER");
        mType = mActivity.getIntent().getIntExtra("SHOT_TYPE", -1);

        mUserId = mUser.getId();
        setTitle();
    }

    @Override
    public boolean isAllPageLoaded() {
        return isAllPageLoaded;
    }

    @Override
    public int getShotType() {
        return mType;
    }

    @Override
    public void startShotDetailActivity(View v, int position) {
        if (globalShotObjects != null) {
            Shot shot = null;
            if (mType == UserShotsPresenter.SELF) {
                PureShot pureShot = (PureShot) globalShotObjects.get(position);
                shot = new Shot();
                bulidShot(shot, pureShot);
            } else if (mType == UserShotsPresenter.LIKED) {
                shot = ((ShotWrapper) globalShotObjects.get(position)).getShot();
            }

            if (shot != null) {
                Intent intent = new Intent(mContext, ShotDetailActivity.class);
                intent.putExtra("SHOT", shot);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, v, mContext.getString(R.string.transition_name));
                ActivityCompat.startActivity(mContext, intent, options.toBundle());
            }
        }
    }

    @Override
    public void loadNextPage() {
        reqData(currentPageNum + 1);
    }

    @Override
    public void pullToRefresh() {
        reqData(1);
    }

    private void reqUserShots(final int pageNum) {
        Observer observer = new Observer<List<PureShot>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<PureShot> userShots) {
                if (userShots != null) {
                    if (userShots.size() < pageSize) {
                        isAllPageLoaded = true;
                    }
                    if (globalShotObjects == null) {
                        globalShotObjects = new ArrayList<>();
                    }
                    if (pageNum == 1) {
                        globalShotObjects.clear();
                    }
                    globalShotObjects.addAll(userShots);

                    mShotActivity.shotShots(globalShotObjects, pageNum);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mShotActivity.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {
                currentPageNum = pageNum;
            }
        };

        HttpClient.getInstance().loadUserShots(observer, mUserId, pageNum, pageSize);
    }

    private void reqLikedShots(final int pageNum) {
        Observer observer = new Observer<List<ShotWrapper>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<ShotWrapper> likedShots) {
                if (likedShots != null) {
                    if (likedShots.size() < pageSize) {
                        isAllPageLoaded = true;
                    }
                    if (globalShotObjects == null) {
                        globalShotObjects = new ArrayList<>();
                    }
                    if (pageNum == 1) {
                        globalShotObjects.clear();
                    }
                    globalShotObjects.addAll(likedShots);

                    mShotActivity.shotShots(globalShotObjects, pageNum);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mShotActivity.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {
                currentPageNum = pageNum;
            }
        };

        HttpClient.getInstance().loadLikedShots(observer, mUserId, pageNum, pageSize);
    }

    private void reqData(int pageNum) {
        if (NetworkUtil.isNetworkAvailable(mActivity)) {
            if (mUserId > 0) {
                if (mType == SELF) {
                    reqUserShots(pageNum);
                } else if (mType == LIKED) {
                    reqLikedShots(pageNum);
                }
            } else {
                mShotActivity.showErrorView(ErrorConstant.DATA_ERROR);
            }
        } else {
            mShotActivity.showErrorView(ErrorConstant.NETWORK_ERROR);
        }
    }

    private void setTitle() {
        if (mType == SELF) {
            mShotActivity.setPageTitle("Shots");
        } else if (mType == LIKED) {
            mShotActivity.setPageTitle("Likes");
        }
    }

    private void bulidShot(Shot dst, PureShot src) {
        if (dst != null && src != null) {
            dst.setUser(mUser);
            dst.setId(src.getId());
            dst.setTitle(src.getTitle());
            dst.setImages(src.getImages());
            dst.setDescription(src.getDescription());
            dst.setViews_count(src.getViews_count());
            dst.setLikes_count(src.getLikes_count());
            dst.setComments_count(src.getComments_count());
            dst.setCreated_at(src.getCreated_at());
            dst.setTags(src.getTags());
            dst.setHtml_url(src.getHtml_url());
        }
    }

}
