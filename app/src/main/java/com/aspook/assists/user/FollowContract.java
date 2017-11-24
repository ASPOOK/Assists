package com.aspook.assists.user;

import com.aspook.assists.base.BasePresenter;
import com.aspook.assists.base.BaseView;

import java.util.List;

/**
 * Created by ASPOOK on 17/7/19.
 */

interface FollowContract {
    interface Presenter extends BasePresenter {
        void getIntentData();

        boolean isAllPageLoaded();

        void loadNextPage();

        void pullToRefresh();

        int getFollowType();

        void startUserActivity(int position);
    }

    interface View extends BaseView<Presenter> {
        void setPageTitle(String title);

        void showFollowInfo(List<Object> followObjects, int pageNum);
    }
}
