package com.aspook.assists.message;

import android.os.Handler;
import android.os.Message;

/**
 * Created by ASPOOK on 17/7/3.
 */

public class MessageHandler extends Handler {

    private OnMessageReceiveListener mOnMessageReceiveListener;

    public MessageHandler(OnMessageReceiveListener listener) {
        this.mOnMessageReceiveListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (mOnMessageReceiveListener != null) {
            mOnMessageReceiveListener.handleMessage(msg);
        }
    }

}
