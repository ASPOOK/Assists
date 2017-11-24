package com.aspook.assists.message;


import android.os.Handler;

/**
 * Created by aspook on 2017/11/16.
 */

public class HandlerRecord {
    public int[] actions;
    public Handler targetHandler;

    public HandlerRecord(int[] actions, Handler handler) {
        this.actions = actions;
        this.targetHandler = handler;
    }
}
