package com.aspook.assists.listener;

import android.view.View;

/**
 * used in RecycleView
 * <p>
 * Created by ASPOOK on 17/6/30.
 */

public interface OnItemClickListener {
    /**
     * call back when RecycleView's item is clicked
     *
     * @param v
     * @param position
     */
    void onItemClicked(View v, int position);

    /**
     * call back when RecycleView's item is long clicked
     *
     * @param v
     * @param position
     */
    void onItemLongClicked(View v, int position);
}
