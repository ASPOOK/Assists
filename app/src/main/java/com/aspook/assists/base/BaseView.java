package com.aspook.assists.base;

/**
 * Created by ASPOOK on 17/6/6.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    void showErrorView(int errorType);
}
