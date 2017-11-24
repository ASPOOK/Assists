package com.aspook.assists.shot;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseActivity;
import com.aspook.assists.base.BaseAdapter;
import com.aspook.assists.entity.Comment;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.glide.GlideUtil;
import com.aspook.assists.listener.OnItemClickListener;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.MetricUtil;
import com.aspook.assists.util.SharePreferenceUtil;
import com.aspook.assists.util.TimeConverterUtil;
import com.aspook.assists.util.ToastUtil;
import com.aspook.assists.widget.CircleImageView;

import java.util.List;

public class ShotDetailActivity extends BaseActivity implements ShotDetailContract.View {
    private ShotDetailContract.Presenter mPresenter;

    private ImageView ivShot;
    private CircleImageView civAvatar;
    private TextView tvShotAuthor;
    private TextView tvCreateTime;
    private TextView tvViewCount;
    private TextView tvLikeCount;
    private TextView tvCommentCount;
    private TextView tvDescription;
    private TextView tvTag;
    private LinearLayout llAuthor;
    private LinearLayout llLikes;
    private ImageView ivLike;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private CommentsAdapter mAdapter;
    private NestedScrollView mNestedScrollView;

    private boolean isLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_detail);
        new ShotDetailPresenter(this);
        if (mPresenter != null) {
            mPresenter.getIntentData();
        }

        findViews();
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                if (mPresenter != null) {
                    mPresenter.share();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLoaded) {
            if (mPresenter != null) {
                mPresenter.showShotDetail();
                mPresenter.getComments();
                isLoaded = true;
            }
        }
    }

    public void findViews() {
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nsv_container);
        ivShot = (ImageView) findViewById(R.id.iv_backdrop);
        setImageView(ivShot);
        civAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        llAuthor = (LinearLayout) findViewById(R.id.ll_author);
        llLikes = (LinearLayout) findViewById(R.id.ll_likes);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        tvShotAuthor = (TextView) findViewById(R.id.tv_shot_author);
        tvCreateTime = (TextView) findViewById(R.id.tv_create_time);
        tvViewCount = (TextView) findViewById(R.id.tv_view_count);
        tvLikeCount = (TextView) findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        tvDescription = (TextView) findViewById(R.id.tv_shot_description);
        tvTag = (TextView) findViewById(R.id.tv_shot_tags);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent));

        mRecycleView = (RecyclerView) findViewById(R.id.rv_comment);
        initRecycleView();
    }

    private void setImageView(ImageView iv) {
        int width = MetricUtil.getScreenSize(this)[0];
        int height = width * 300 / 400;
        CollapsingToolbarLayout.LayoutParams params = new CollapsingToolbarLayout.LayoutParams(width, height);
        iv.setLayoutParams(params);
    }

    public void initRecycleView() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setHasFixedSize(false);
        mRecycleView.setNestedScrollingEnabled(false);
        mAdapter = new CommentsAdapter(this);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setListeners() {
        llAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter != null) {
                    mPresenter.startUserActivity();
                }
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

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // NestedScrollView must has only one direct child
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (mPresenter.isAllPageLoaded()) {
                        if (mAdapter != null) {
                            mAdapter.changeLoadStatus(BaseAdapter.LOADING_NO_MORE);
                        }
                        return;
                    }
                    if (mAdapter != null) {
                        mAdapter.changeLoadStatus(BaseAdapter.LOADING_START);
                    }
                    mPresenter.loadNextPage();
                }
            }
        });

        llLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(SharePreferenceUtil.getOauthToken(ShotDetailActivity.this))) {
                    if (mPresenter != null) {
                        mPresenter.processLike();
                    }
                }
            }
        });
    }

    @Override
    public void showShotDetail(Shot shot) {
        if (shot != null) {
            if (!TextUtils.isEmpty(shot.getTitle())) {
                mCollapsingToolbar.setTitle(shot.getTitle());
            }

            String normalUrl = shot.getImages().getNormal();
            String hdpiUrl = shot.getImages().getHidpi();
            String teaserUrl = shot.getImages().getTeaser();
            String description = shot.getDescription();
            int viewCount = shot.getViews_count();
            int likesCount = shot.getLikes_count();
            int commentCount = shot.getComments_count();
            List<String> tags = shot.getTags();
            String tagStr = tags.toString().substring(1, tags.toString().length() - 1);

            if (!TextUtils.isEmpty(normalUrl)) {
                GlideUtil.loadImageWithCrossFade(this, normalUrl, ivShot);
            } else if (!TextUtils.isEmpty(hdpiUrl)) {
                GlideUtil.loadImageWithCrossFade(this, hdpiUrl, ivShot);
            } else if (!TextUtils.isEmpty(teaserUrl)) {
                GlideUtil.loadImageWithCrossFade(this, teaserUrl, ivShot);
            }

            GlideUtil.loadImageWithDefaultImage(this, shot.getUser().getAvatar_url(),
                    civAvatar, true, R.drawable.icon_avatar_default);
            tvShotAuthor.setText(shot.getUser().getName());
            if (!TextUtils.isEmpty(shot.getCreated_at())) {
                tvCreateTime.setText(TimeConverterUtil.utc2GMT(shot.getCreated_at()));
            }

            tvViewCount.setText(viewCount > 0 ? viewCount + "" : "0");
            tvLikeCount.setText(likesCount > 0 ? likesCount + "" : "0");
            tvCommentCount.setText(commentCount > 0 ? commentCount + "" : "0");

            if (!TextUtils.isEmpty(description)) {
                tvDescription.setText(Html.fromHtml(description));
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(tagStr)) {
                tvTag.setText("Tags: " + tagStr);
            } else {
                tvTag.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(SharePreferenceUtil.getOauthToken(this))) {
                mPresenter.checkLikeStatus();
            }
        }
    }

    @Override
    public void showComments(List<Comment> comments, int pageNum) {
        if (mAdapter != null) {
            if (pageNum > 1) {
                if (mPresenter.isAllPageLoaded()) {
                    mAdapter.changeLoadStatus(BaseAdapter.LOADING_NO_MORE);
                }
            }
            mAdapter.setData(comments);
        }
    }

    @Override
    public void showLikeRes(boolean isSuccess, int likeCount) {
        if (isSuccess) {
            showCurLikesCount(likeCount);
        } else {
            ToastUtil.toastShort(this, "like failed!");
        }
    }

    @Override
    public void showUnLikeRes(boolean isSuccess, int likeCount) {
        if (isSuccess) {
            showCurLikesCount(likeCount);
        } else {
            ToastUtil.toastShort(this, "unlike failed!");
        }
    }

    @Override
    public void showErrorView(int errorType) {
        if (errorType == ErrorConstant.NETWORK_ERROR) {
            ToastUtil.toastShort(this, ErrorConstant.NETWORK_ERROR_MSG);
        } else if (errorType == ErrorConstant.DATA_ERROR) {
            ToastUtil.toastShort(this, ErrorConstant.DATA_ERROR_MSG);
        }
    }

    @Override
    public void setPresenter(ShotDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clearDisposable();
        }
    }

    private void showCurLikesCount(int likeCount) {
        if (ivLike != null) {
            doAnimation(ivLike);
        }
        tvLikeCount.setText(likeCount + "");
    }

    private void doAnimation(ImageView v) {
        float initX = v.getTranslationX();
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                ObjectAnimator.ofFloat(v, "translationX", initX, initX + 15).setDuration(500),
                ObjectAnimator.ofFloat(v, "translationX", initX + 15, initX).setDuration(500));
        set.start();
    }
}
