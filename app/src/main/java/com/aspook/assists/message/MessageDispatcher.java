package com.aspook.assists.message;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

import java.util.Arrays;

/**
 * MessageDispatcher
 * <p>
 * 1.message for point to point with designated Handler
 * 2.message for point to point/points with some Action
 * <p>
 * Created by ASPOOK on 16/10/24.
 */

public class MessageDispatcher {

    private static volatile MessageDispatcher instance;
    private SparseArray<HandlerRecord> mHandlerRecords;

    private MessageDispatcher() {
        mHandlerRecords = new SparseArray<>();
    }

    public static MessageDispatcher getInstance() {
        if (instance == null) {
            synchronized (MessageDispatcher.class) {
                if (instance == null) {
                    instance = new MessageDispatcher();
                }
            }
        }
        return instance;
    }

    /**
     * register Handler for receiving message with some action,only the handler with the action can receive the message
     * <p>
     * It is recommended to define a aciton for each Handler for better performance
     *
     * @param targetHandler
     * @param tag
     * @param actions       if action is null,all the handlers will receive the message
     */
    public void registerHandlerWithAction(Handler targetHandler, int key, int... actions) {
        if (mHandlerRecords == null) {
            mHandlerRecords = new SparseArray<>();
        }

        HandlerRecord targetRecord = mHandlerRecords.get(key);
        if (targetRecord != null) {
            if (actions == null) {
                return;
            }
            int[] oldActions = targetRecord.actions;
            if (oldActions != null) {
                int oldLen = oldActions.length;
                int newLen = actions.length;
                oldActions = Arrays.copyOf(oldActions, oldLen + newLen);
                System.arraycopy(actions, 0, oldActions, oldLen, newLen);
                targetRecord.actions = oldActions;
            } else {
                oldActions = actions;
            }
            targetRecord.actions = oldActions;
        } else {
            targetRecord = new HandlerRecord(actions, targetHandler);
            mHandlerRecords.put(key, targetRecord);
        }
    }


    /**
     * when you quit from an activity, you should remember to unregister the handler
     *
     * @param key
     */
    public void unregisterHandler(int key) {
        if (mHandlerRecords != null) {
            mHandlerRecords.remove(key);
        }
    }

    /**
     * release all handlers
     */
    public void releaseAll() {
        if (mHandlerRecords != null) {
            mHandlerRecords.clear();
            mHandlerRecords = null;
        }
    }

    public void sendMessageWithAction(int what, int action) {
        sendMessageWithAction(what, -1, -1, null, action);
    }

    public void sendMessageWithAction(int what, Object obj, int action) {
        sendMessageWithAction(what, -1, -1, obj, action);
    }

    public void sendMessageWithAction(int what, int arg1, int arg2, int action) {
        sendMessageWithAction(what, arg1, arg2, null, action);
    }

    public void sendMessageWithAction(int what, int arg1, int arg2, Object obj, int action) {
        try {
            if (mHandlerRecords == null) {
                return;
            }

            Message msg = Message.obtain();
            msg.what = what;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            msg.obj = obj;

            int size = mHandlerRecords.size();
            for (int i = 0; i < size; i++) {
                HandlerRecord handlerRecord = mHandlerRecords.valueAt(i);
                if (handlerRecord != null) {
                    int[] actions = handlerRecord.actions;
                    Handler targetHandler = handlerRecord.targetHandler;
                    if (targetHandler != null) {
                        if (actions != null) {
                            for (int act : actions) {
                                if (act == action) {
                                    targetHandler.sendMessage(Message.obtain(msg));
                                    break;
                                }
                            }
                        } else {
                            // if actions == null, all handlers can receive the message
                            targetHandler.sendMessage(Message.obtain(msg));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Handler targetHandler, int what, int arg1, int arg2, Object obj) {
        try {
            if (targetHandler != null) {
                Message message = Message.obtain(targetHandler, what, arg1, arg2, obj);
                message.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Handler targetHandler, int what, int arg1, int arg2) {
        try {
            if (targetHandler != null) {
                Message message = Message.obtain(targetHandler, what, arg1, arg2);
                message.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Handler targetHandler, int what, Object obj) {
        try {
            if (targetHandler != null) {
                Message message = Message.obtain(targetHandler, what, obj);
                message.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Handler targetHandler, int what) {
        try {
            if (targetHandler != null) {
                Message message = Message.obtain(targetHandler, what);
                message.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageDelayed(Handler targetHandler, int what, long delayMillis) {
        if (delayMillis <= 0) {
            sendMessage(targetHandler, what);
            return;
        }

        try {
            if (targetHandler != null) {
                Message message = Message.obtain(targetHandler, what);
                targetHandler.sendMessageDelayed(message, delayMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMessageDelayed(Handler targetHandler, int what, Object obj, long delayMillis) {
        if (delayMillis <= 0) {
            sendMessage(targetHandler, what, obj);
            return;
        }
        try {
            if (targetHandler != null) {
                Message message = Message.obtain(targetHandler, what, obj);
                targetHandler.sendMessageDelayed(message, delayMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
