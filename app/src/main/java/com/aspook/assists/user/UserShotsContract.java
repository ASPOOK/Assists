package com.aspook.assists.user;

import com.aspook.assists.base.BasePresenter;
import com.aspook.assists.base.BaseView;

import java.util.List;

/**
 * Created by ASPOOK on 17/7/19.
 */

interface UserShotsContract {
    interface Presenter extends BasePresenter {
        void getIntentData();

        void loadNextPage();

        void pullToRefresh();

        boolean isAllPageLoaded();

        int getShotType();

        void startShotDetailActivity(android.view.View v, int position);
    }

    interface View extends BaseView<Presenter> {
        void setPageTitle(String title);

        void shotShots(List<Object> shots, int pageNum);
    }
}
