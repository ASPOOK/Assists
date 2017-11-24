package com.aspook.assists.shot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseAdapter;
import com.aspook.assists.entity.Comment;
import com.aspook.assists.entity.User;
import com.aspook.assists.glide.GlideUtil;
import com.aspook.assists.util.TimeConverterUtil;
import com.aspook.assists.widget.CircleImageView;

import java.util.List;

/**
 * Adatper for shot comments
 * <p>
 * Created by ASPOOK on 17/7/7.
 */

class CommentsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comment> comments;

    CommentsAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_FOOTER) {
            View footer = LayoutInflater.from(mContext).inflate(R.layout.rv_footer_item, parent, false);
            holder = new FooterViewHolder(footer);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rv_comment_item, parent, false);
            holder = new CommentViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CommentViewHolder) {
            Comment comment = comments.get(position);
            if (comment != null) {
                final CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                User user = comment.getUser();
                String comment_content = comment.getBody();
                String createTime = comment.getCreated_at();
                if (!TextUtils.isEmpty(comment_content)) {
                    commentViewHolder.tvComment.setText(Html.fromHtml(comment_content.replace("<p>", "").replace("</p>", "")));
                } else {
                    commentViewHolder.tvComment.setText("");
                }
                if (!TextUtils.isEmpty(createTime)) {
                    commentViewHolder.tvTime.setText(TimeConverterUtil.utc2GMT(createTime));
                } else {
                    commentViewHolder.tvTime.setText("");
                }
                if (user != null) {
                    String avatarUrl = user.getAvatar_url();
                    String userName = user.getName();
                    if (!TextUtils.isEmpty(avatarUrl)) {
                        GlideUtil.loadImageWithDefaultImage(mContext, avatarUrl, commentViewHolder.civAvatar,
                                true, R.drawable.icon_avatar_default);
                    }
                    if (!TextUtils.isEmpty(userName)) {
                        commentViewHolder.tvUserName.setText(userName);
                    } else {
                        commentViewHolder.tvUserName.setText("");
                    }

                    if (mOnItemClickListener != null) {
                        commentViewHolder.civAvatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mOnItemClickListener.onItemClicked(commentViewHolder.civAvatar, position);
                            }
                        });
                    }
                }
            }
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if (loadingStstus == LOADING_START) {
                footerViewHolder.tv_status.setText("loading……");
                footerViewHolder.iv_loading.setImageResource(R.drawable.round_progressbar);
            } else if (loadingStstus == LOADING_NO_MORE) {
                footerViewHolder.iv_loading.setVisibility(View.GONE);
                footerViewHolder.tv_status.setText("The End");
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
        if (comments == null) {
            return 0;
        }
        return comments.size() + 1;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        TextView tvUserName;
        TextView tvComment;
        TextView tvTime;

        CommentViewHolder(View itemView) {
            super(itemView);

            civAvatar = (CircleImageView) itemView.findViewById(R.id.civ_avatar);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
