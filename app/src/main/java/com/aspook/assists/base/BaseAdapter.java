package com.aspook.assists.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspook.assists.R;
import com.aspook.assists.listener.OnItemClickListener;

/**
 * BaseAdapter for RecyclerView to load more and handle click events
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected int loadingStstus = -1;
    protected static final int TYPE_FOOTER = 100;
    public static final int LOADING_START = 101;
    public static final int LOADING_NO_MORE = 102;

    public OnItemClickListener mOnItemClickListener;

    public void changeLoadStatus(int status) {
        this.loadingStstus = status;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_status;
        public ImageView iv_loading;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            iv_loading = (ImageView) itemView.findViewById(R.id.iv_loading);
        }
    }

}
