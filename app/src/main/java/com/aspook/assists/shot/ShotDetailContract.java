package com.aspook.assists.shot;

import com.aspook.assists.base.BasePresenter;
import com.aspook.assists.base.BaseView;
import com.aspook.assists.entity.Comment;
import com.aspook.assists.entity.Shot;

import java.util.List;

/**
 * Created by ASPOOK on 17/6/27.
 */

interface ShotDetailContract {
    interface Presenter extends BasePresenter {
        void startUserActivity();

        void startUserActivity(int position);

        void getIntentData();

        void showShotDetail();

        void getComments();

        void loadNextPage();

        boolean isAllPageLoaded();

        void share();

        void checkLikeStatus();

        void processLike();
    }

    interface View extends BaseView<Presenter> {
        void showShotDetail(Shot shot);

        void showComments(List<Comment> comments, int pageNum);

        void showLikeRes(boolean isSuccess, int likeCount);

        void showUnLikeRes(boolean isSuccess, int likeCount);
    }
}
