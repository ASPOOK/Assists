package com.aspook.assists.shot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aspook.assists.entity.Comment;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.entity.User;
import com.aspook.assists.http.HttpClient;
import com.aspook.assists.user.UserActivity;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.NetworkUtil;
import com.aspook.assists.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by ASPOOK on 17/7/4.
 */

class ShotDetailPresenter implements ShotDetailContract.Presenter {
    private static final int PAGE_SIZE = 20;
    private static final int LIKE = 1;
    private static final int UNLIKE = 2;
    private ShotDetailContract.View mDetailActivty;
    private Shot mShot;
    private Context mContext;
    private Activity mActivity;

    private int currentPageNum = 1;
    private boolean isAllPageLoaded = false;
    private List<Comment> globalComments;
    private CompositeDisposable mCompositeDisposable;

    private boolean isLiked;
    private int curLikesCount;

    ShotDetailPresenter(ShotDetailContract.View view) {
        this.mDetailActivty = view;
        view.setPresenter(this);
        mContext = (Context) mDetailActivty;
        mActivity = (Activity) mDetailActivty;
    }

    @Override
    public void start() {

    }

    @Override
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void startUserActivity() {
        if (mShot != null) {
            User user = mShot.getUser();
            if (user != null) {
                Intent intent = new Intent(mActivity, UserActivity.class);
                intent.putExtra("USER", user);
                mActivity.startActivity(intent);
            }
        }
    }

    @Override
    public void startUserActivity(int position) {
        if (globalComments != null) {
            Comment comment = globalComments.get(position);
            if (comment != null) {
                User user = comment.getUser();
                if (user != null) {
                    Intent intent = new Intent(mActivity, UserActivity.class);
                    intent.putExtra("USER", user);
                    mActivity.startActivity(intent);
                }
            }
        }
    }

    @Override
    public void getIntentData() {
        mShot = mActivity.getIntent().getParcelableExtra("SHOT");
        if (mShot != null) {
            curLikesCount = mShot.getLikes_count();
        }
    }

    @Override
    public void showShotDetail() {
        mDetailActivty.showShotDetail(mShot);
    }

    @Override
    public void getComments() {
        reqData(1);
    }

    @Override
    public void loadNextPage() {
        reqData(currentPageNum + 1);
    }

    @Override
    public boolean isAllPageLoaded() {
        return isAllPageLoaded;
    }

    @Override
    public void share() {
        if (mShot == null) {
            return;
        }
        String content = mShot.getTitle() + "\n" + mShot.getHtml_url();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);

        mActivity.startActivity(intent);
    }

    @Override
    public void checkLikeStatus() {
        if (mShot != null) {
            checkLikeStatus(mShot.getId());
        }
    }

    @Override
    public void processLike() {
        if (mShot != null) {
            if (isLiked) {
                processLike(mShot.getId(), UNLIKE);
            } else {
                processLike(mShot.getId(), LIKE);
            }
        }
    }

    private void checkLikeStatus(int shotId) {
        if (shotId <= 0) {
            mDetailActivty.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mDetailActivty.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<Response<Object>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Response<Object> response) {
                if (response != null) {
                    int code = response.code();
                    if (code == 200) {
                        isLiked = true;
                    } else if (code == 404) {
                        isLiked = false;
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mDetailActivty.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        HttpClient.getInstance().checkLikeStatus(observer, shotId, SharePreferenceUtil.getOauthToken(mContext));
    }

    private void processLike(int shotId, final int type) {
        if (shotId <= 0) {
            mDetailActivty.showErrorView(ErrorConstant.DATA_ERROR);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mDetailActivty.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }
        Observer observer = new Observer<Response<Object>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Response<Object> response) {
                int code = response.code();
                if (type == LIKE) {
                    if (code == 201) {
                        isLiked = true;
                        curLikesCount += 1;
                        mDetailActivty.showLikeRes(true, curLikesCount);
                    } else {
                        mDetailActivty.showLikeRes(false, curLikesCount);
                    }
                } else if (type == UNLIKE) {
                    if (code == 204) {
                        isLiked = false;
                        curLikesCount -= 1;
                        mDetailActivty.showUnLikeRes(true, curLikesCount);
                    } else {
                        mDetailActivty.showUnLikeRes(false, curLikesCount);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mDetailActivty.showErrorView(ErrorConstant.DATA_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };

        if (type == LIKE) {
            HttpClient.getInstance().like(observer, shotId, SharePreferenceUtil.getOauthToken(mContext));
        } else if (type == UNLIKE) {
            HttpClient.getInstance().unLike(observer, shotId, SharePreferenceUtil.getOauthToken(mContext));
        }
    }

    private void reqData(final int pageNum) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mDetailActivty.showErrorView(ErrorConstant.NETWORK_ERROR);
            return;
        }

        if (mShot != null) {
            int shotId = mShot.getId();
            if (shotId <= 0) {
                mDetailActivty.showErrorView(ErrorConstant.DATA_ERROR);
                return;
            }

            Observer observer = new Observer<List<Comment>>() {

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    if (mCompositeDisposable == null) {
                        mCompositeDisposable = new CompositeDisposable();
                    }
                    mCompositeDisposable.add(d);
                }

                @Override
                public void onNext(@NonNull List<Comment> comments) {
                    if (comments != null) {
                        // all pages loaded
                        if (comments.size() < PAGE_SIZE) {
                            isAllPageLoaded = true;
                        }
                        if (globalComments == null) {
                            globalComments = new ArrayList<>();
                        }
                        if (pageNum == 1) {
                            globalComments.clear();
                        }

                        globalComments.addAll(comments);
                        mDetailActivty.showComments(globalComments, pageNum);
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    mDetailActivty.showErrorView(ErrorConstant.DATA_ERROR);
                }

                @Override
                public void onComplete() {
                    currentPageNum = pageNum;
                }
            };

            HttpClient.getInstance().loadComments(observer, mShot.getId(), pageNum, PAGE_SIZE);
        }
    }

}
