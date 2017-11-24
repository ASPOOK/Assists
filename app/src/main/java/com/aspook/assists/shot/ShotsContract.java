package com.aspook.assists.shot;

import android.content.Context;

import com.aspook.assists.base.BasePresenter;
import com.aspook.assists.base.BaseView;
import com.aspook.assists.entity.Shot;

import java.util.List;


/**
 * contract interface to manager Presenter and View
 * all actions for Presenter and View should defined here
 * <p>
 * Created by ASPOOK on 17/6/27.
 */

interface ShotsContract {

    interface Presenter extends BasePresenter {
        void pullToRefresh();

        void loadNextPage();

        void startShotDetailActivity(android.view.View view, int position);

        boolean isAllPageLoaded();

        void setContext(Context context);

        void reqOAuthCode();

        void reqAccessToken(String code);
    }

    interface View extends BaseView<Presenter> {
        void showShots(List<Shot> shots, int pageNum);
    }
}
