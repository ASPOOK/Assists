package com.aspook.assists.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseAdapter;
import com.aspook.assists.entity.FolloweeWrapper;
import com.aspook.assists.entity.FollowerWrapper;
import com.aspook.assists.entity.User;
import com.aspook.assists.glide.GlideUtil;
import com.aspook.assists.util.MetricUtil;
import com.aspook.assists.widget.CircleImageView;

import java.util.List;

/**
 * Created by ASPOOK on 17/7/19.
 */

class FollowAdapter extends BaseAdapter {
    private Context mContext;
    private List<Object> followObjects;
    private int mType;

    FollowAdapter(Context context, int followType) {
        this.mContext = context;
        this.mType = followType;
    }

    public void setData(List<Object> objects) {
        this.followObjects = objects;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_FOOTER) {
            View footer = LayoutInflater.from(mContext).inflate(R.layout.rv_footer_item, parent, false);
            holder = new FooterViewHolder(footer);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rv_follow_item, parent, false);
            holder = new FollowViewHolder(view, viewType);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FollowViewHolder) {
            final FollowViewHolder followViewHolder = (FollowViewHolder) holder;
            User user = null;
            if (mType == FollowPresenter.FOLLOWER) {
                FollowerWrapper followerWrapper = (FollowerWrapper) followObjects.get(position);
                user = followerWrapper.getFollower();
            } else if (mType == FollowPresenter.FOLLOWING) {
                FolloweeWrapper followeeWrapper = (FolloweeWrapper) followObjects.get(position);
                user = followeeWrapper.getFollowee();
            }
            if (user == null) {
                return;
            }
            String avatarUrl = user.getAvatar_url();
            String location = user.getLocation();
            String userName = user.getName();
            int followerCount = user.getFollowers_count();

            if (!TextUtils.isEmpty(avatarUrl)) {
                GlideUtil.loadImageWithDefaultImage(mContext, avatarUrl, followViewHolder.civAvatar,
                        true, R.drawable.icon_avatar_default);
            }
            if (!TextUtils.isEmpty(userName)) {
                followViewHolder.tvName.setText(userName);
            } else {
                followViewHolder.tvName.setText("");
            }
            if (!TextUtils.isEmpty(location)) {
                followViewHolder.tvLocation.setText(location);
            } else {
                followViewHolder.tvLocation.setText("");
            }
            if (followerCount > 0) {
                followViewHolder.tvFollower.setText(followerCount + " " + mContext.getString(R.string.follower));
            } else {
                followViewHolder.tvFollower.setText("");
            }

            if (mOnItemClickListener != null) {
                followViewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClicked(followViewHolder.llContainer, position);
                    }
                });
            }
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if (loadingStstus == LOADING_START) {
                footerViewHolder.tv_status.setText(mContext.getString(R.string.loading));
                footerViewHolder.iv_loading.setImageResource(R.drawable.round_progressbar);
            } else if (loadingStstus == LOADING_NO_MORE) {
                footerViewHolder.tv_status.setText(mContext.getString(R.string.end));
                footerViewHolder.iv_loading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (followObjects == null) {
            return 0;
        }

        return followObjects.size() + 1;
    }

    private class FollowViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        TextView tvName;
        TextView tvLocation;
        TextView tvFollower;
        private LinearLayout llContainer;

        FollowViewHolder(View itemView, int viewType) {
            super(itemView);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_follow_container);
            civAvatar = (CircleImageView) itemView.findViewById(R.id.civ_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvFollower = (TextView) itemView.findViewById(R.id.tv_followers);

            // set size to adpater the screen
            int width = MetricUtil.getScreenSize(mContext)[0] / 2 - (int) ((16 + 8) * MetricUtil.getDensity(mContext));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
            llContainer.setLayoutParams(params);
        }
    }
}
