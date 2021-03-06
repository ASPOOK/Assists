package com.aspook.assists.shot;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.base.BaseAdapter;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.glide.GlideUtil;
import com.aspook.assists.util.MetricUtil;

import java.util.List;

/**
 * Adapter for ShotsFragment
 * <p>
 * Created by ASPOOK on 17/6/30.
 */

public class ShotsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Shot> shots;
    private int layoutStyle = Constant.GRIDLAYOUT;

    private static final int TYPE_GRIDLAYOUT = 1;
    private static final int TYPE_LINEARLAYOUT = 2;

    public ShotsAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Shot> shots) {
        this.shots = shots;
        notifyDataSetChanged();
    }

    public void setLayoutStyle(int style) {
        this.layoutStyle = style;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_FOOTER) {
            View footer = LayoutInflater.from(mContext).inflate(R.layout.rv_footer_item, parent, false);
            holder = new FooterViewHolder(footer);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rv_shot_item, parent, false);
            holder = new ShotViewHolder(view, viewType);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShotViewHolder) {
            final ShotViewHolder shotViewHolder = (ShotViewHolder) holder;
            Shot shot = shots.get(position);
            String imgUrl = shot.getImages().getNormal();
            boolean isAnimated = shot.isAnimated();
            GlideUtil.loadImageWithCrossFade(mContext, imgUrl, shotViewHolder.ivShot);
            if (isAnimated) {
                shotViewHolder.tvTag.setVisibility(View.VISIBLE);
            } else {
                shotViewHolder.tvTag.setVisibility(View.GONE);
            }

            if (mOnItemClickListener != null) {
                shotViewHolder.cvShot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClicked(shotViewHolder.cvShot, position);
                    }
                });

                shotViewHolder.cvShot.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemClickListener.onItemLongClicked(shotViewHolder.cvShot, position);
                        return false;
                    }
                });
            }
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if (loadingStstus == LOADING_START) {
                footerViewHolder.tv_status.setText("loading……");
                footerViewHolder.iv_loading.setImageResource(R.drawable.round_progressbar);
            } else if (loadingStstus == LOADING_NO_MORE) {
                footerViewHolder.tv_status.setText("The End");
                footerViewHolder.iv_loading.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else if (layoutStyle == Constant.LINEARLAYOUT) {
            return TYPE_LINEARLAYOUT;
        } else {
            return TYPE_GRIDLAYOUT;
        }
    }

    @Override
    public int getItemCount() {
        if (shots == null) {
            return 0;
        }

        return shots.size() + 1;
    }

    class ShotViewHolder extends RecyclerView.ViewHolder {
        ImageView ivShot;
        CardView cvShot;
        TextView tvTag;

        public ShotViewHolder(View itemView, int viewType) {
            super(itemView);
            ivShot = (ImageView) itemView.findViewById(R.id.iv_shot);
            cvShot = (CardView) itemView.findViewById(R.id.cv_shot);
            tvTag = (TextView) itemView.findViewById(R.id.tv_gif_tag);

            // set the image size to adpater the screen
            int width = 0;
            if (viewType == TYPE_GRIDLAYOUT) {
                width = MetricUtil.getScreenSize(mContext)[0] / 2 - (int) ((16 + 8 + 5 + 5) * MetricUtil.getDensity(mContext));
            } else if (viewType == TYPE_LINEARLAYOUT) {
                width = MetricUtil.getScreenSize(mContext)[0] - (int) ((16 + 16 + 5 + 5) * MetricUtil.getDensity(mContext));
            }
            int height = width * 300 / 400;

            int margin = (int) (5 * MetricUtil.getDensity(mContext));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.setMargins(margin, margin, margin, margin);
            ivShot.setLayoutParams(params);
        }
    }
}
