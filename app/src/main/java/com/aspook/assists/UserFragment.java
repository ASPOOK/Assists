package com.aspook.assists;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspook.assists.base.BaseFragment;
import com.aspook.assists.entity.User;
import com.aspook.assists.glide.GlideUtil;
import com.aspook.assists.message.Action;
import com.aspook.assists.message.MessageConstant;
import com.aspook.assists.message.MessageDispatcher;
import com.aspook.assists.message.MessageHandler;
import com.aspook.assists.message.OnMessageReceiveListener;
import com.aspook.assists.user.FollowPresenter;
import com.aspook.assists.user.UserShotsPresenter;
import com.aspook.assists.user.UserContract;
import com.aspook.assists.user.UserPresenter;
import com.aspook.assists.util.ErrorConstant;
import com.aspook.assists.util.SharePreferenceUtil;
import com.aspook.assists.util.ToastUtil;
import com.aspook.assists.widget.CircleImageView;


/**
 * Created by ASPOOK on 17/6/15.
 */

public class UserFragment extends BaseFragment implements UserContract.View {
    public static final int FROM_USER_ACTIVITY = 1;
    private Handler mHandler;
    private UserContract.Presenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CircleImageView civAvatar;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvBio;
    private TextView tvProTag;
    private TextView tvFollow;
    private TextView tvSignIn;

    private TextView tvShots;
    private TextView tvLikes;
    private TextView tvFollowing;
    private TextView tvFollowers;
    private LinearLayout llShots;
    private LinearLayout llLikes;
    private LinearLayout llFollowing;
    private LinearLayout llFollowers;
    private LinearLayout llLocation;

    private boolean isLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new MessageHandler(new OnMessageReceiveListener() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case MessageConstant.OAUTH_CODE:
                        if (!mPresenter.isFromUserActivity()) {
                            if (tvSignIn != null) {
                                tvSignIn.setVisibility(View.GONE);
                            }
                            mPresenter.reqAccessToken((String) msg.obj);
                            if (mSwipeRefreshLayout != null) {
                                mSwipeRefreshLayout.setRefreshing(true);
                            }
                        }
                        break;
                    case MessageConstant.LOGOUT:
                        if (!mPresenter.isFromUserActivity()) {
                            showLogin();
                        }
                        break;
                    case MessageConstant.AUTHENTICATED_FROM_FOLLOWING:
                        if (!mPresenter.isFromUserActivity()) {
                            String oAuthToken = SharePreferenceUtil.getOauthToken(getActivity());
                            if (!TextUtils.isEmpty(oAuthToken)) {
                                mPresenter.loadUserData(oAuthToken);
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
                Action.AUTHENTICATED_FROM_FOLLOWING);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        new UserPresenter(this, context);
        if (mPresenter != null) {
            mPresenter.getData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        findViews(rootView);
        setListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isLoaded) {
            if (mPresenter != null) {
                if (mPresenter.isFromUserActivity()) {
                    mPresenter.start();
                } else {
                    String oAuthToken = SharePreferenceUtil.getOauthToken(getActivity());
                    if (TextUtils.isEmpty(oAuthToken)) {
                        showLogin();
                    } else {
                        mPresenter.loadUserData(oAuthToken);
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                    }
                }
            }
            isLoaded = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showErrorView(int errorType) {
        stopRefreshing();
        if (errorType == ErrorConstant.NETWORK_ERROR) {
            ToastUtil.toastShort(getActivity(), ErrorConstant.NETWORK_ERROR_MSG);
        } else if (errorType == ErrorConstant.DATA_ERROR) {
            ToastUtil.toastShort(getActivity(), ErrorConstant.DATA_ERROR_MSG);
        }
    }

    @Override
    public void showUserInfo(User user) {
        stopRefreshing();
        if (user != null) {
            String avatar_url = user.getAvatar_url();
            String name = user.getName();
            String location = user.getLocation();
            String bio = user.getBio();

            int shotsCount = user.getShots_count();
            int likesCount = user.getLikes_count();
            int followingCount = user.getFollowings_count();
            int followersCount = user.getFollowers_count();

            boolean isPro = user.isPro();
            if (!TextUtils.isEmpty(avatar_url)) {
                civAvatar.setVisibility(View.VISIBLE);
                GlideUtil.loadImageWithDefaultImage(getActivity(), avatar_url, civAvatar, true,
                        R.drawable.icon_avatar_default);
            }
            if (!TextUtils.isEmpty(name)) {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(name);
            } else {
                tvName.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(location)) {
                tvLocation.setText(location);
                llLocation.setVisibility(View.VISIBLE);
            }
            if (mPresenter.isFromUserActivity() && !TextUtils.isEmpty(SharePreferenceUtil.getOauthToken(getActivity()))) {
                mPresenter.checkFollowStatus();
            }
            if (!TextUtils.isEmpty(bio)) {
                tvBio.setVisibility(View.VISIBLE);
                tvBio.setText(Html.fromHtml(bio));
            } else {
                tvBio.setVisibility(View.GONE);
            }
            if (isPro) {
                tvProTag.setVisibility(View.VISIBLE);
            } else {
                tvProTag.setVisibility(View.GONE);
            }

            if (shotsCount > 0) {
                tvShots.setText(shotsCount + "");
                llShots.setVisibility(View.VISIBLE);
            }
            if (likesCount > 0) {
                tvLikes.setText(likesCount + "");
                llLikes.setVisibility(View.VISIBLE);
            }
            if (followingCount > 0) {
                tvFollowing.setText(followingCount + "");
                llFollowing.setVisibility(View.VISIBLE);
            }
            if (followersCount > 0) {
                tvFollowers.setText(followersCount + "");
                llFollowers.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void changeFollowStatus(boolean isFollowing) {
        tvFollow.setVisibility(View.VISIBLE);
        if (isFollowing) {
            tvFollow.setText("Following");
        } else {
            tvFollow.setText("Follow");
        }
    }

    @Override
    public void showFollowRes(boolean isSuccess) {
        if (isSuccess) {
            tvFollow.setText("Following");
        } else {
            ToastUtil.toastShort(getActivity(), "follow failed!");
        }
    }

    @Override
    public void showUnFollowRes(boolean isSuccess) {
        if (isSuccess) {
            tvFollow.setText("Follow");
        } else {
            ToastUtil.toastShort(getActivity(), "unfollow failed!");
        }
    }

    private void findViews(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_pull_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setEnabled(false);
        civAvatar = (CircleImageView) rootView.findViewById(R.id.civ_avatar);
        tvName = (TextView) rootView.findViewById(R.id.tv_user_name);
        tvLocation = (TextView) rootView.findViewById(R.id.tv_location);
        tvBio = (TextView) rootView.findViewById(R.id.tv_bio);
        tvProTag = (TextView) rootView.findViewById(R.id.tv_pro_tag);
        tvFollow = (TextView) rootView.findViewById(R.id.tv_follow);
        tvSignIn = (TextView) rootView.findViewById(R.id.tv_sign_in);

        tvShots = (TextView) rootView.findViewById(R.id.tv_shots);
        tvLikes = (TextView) rootView.findViewById(R.id.tv_likes);
        tvFollowing = (TextView) rootView.findViewById(R.id.tv_following);
        tvFollowers = (TextView) rootView.findViewById(R.id.tv_followers);
        llShots = (LinearLayout) rootView.findViewById(R.id.ll_shots);
        llLikes = (LinearLayout) rootView.findViewById(R.id.ll_likes);
        llFollowing = (LinearLayout) rootView.findViewById(R.id.ll_following);
        llFollowers = (LinearLayout) rootView.findViewById(R.id.ll_followers);
        llLocation = (LinearLayout) rootView.findViewById(R.id.ll_location);
    }

    private void setListeners() {
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.processFollow();
            }
        });

        llShots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startShotActivity(UserShotsPresenter.SELF);
            }
        });
        llLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startShotActivity(UserShotsPresenter.LIKED);
            }
        });
        llFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startFollowActivity(FollowPresenter.FOLLOWING);
            }
        });
        llFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startFollowActivity(FollowPresenter.FOLLOWER);
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.reqOAuthCode();
            }
        });
    }

    private void showLogin() {
        tvSignIn.setVisibility(View.VISIBLE);
        civAvatar.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
        tvBio.setVisibility(View.GONE);
        tvProTag.setVisibility(View.GONE);
        tvFollow.setVisibility(View.GONE);

        llShots.setVisibility(View.GONE);
        llLikes.setVisibility(View.GONE);
        llFollowing.setVisibility(View.GONE);
        llFollowers.setVisibility(View.GONE);
        llLocation.setVisibility(View.GONE);
    }

    private void stopRefreshing() {
        if (mSwipeRefreshLayout != null) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
