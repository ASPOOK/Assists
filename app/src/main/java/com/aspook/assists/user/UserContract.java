package com.aspook.assists.user;

import com.aspook.assists.base.BasePresenter;
import com.aspook.assists.base.BaseView;
import com.aspook.assists.entity.User;

/**
 * Created by ASPOOK on 17/7/7.
 */

public interface UserContract {
    interface Presenter extends BasePresenter {

        void getData();

        void startFollowActivity(int followType);

        void startShotActivity(int shotType);

        void reqOAuthCode();

        void reqAccessToken(String code);

        void loadUserData(String token);

        boolean isFromUserActivity();

        void checkFollowStatus();

        void processFollow();
    }

    interface View extends BaseView<Presenter> {
        void showUserInfo(User user);

        void changeFollowStatus(boolean isFollowing);

        void showFollowRes(boolean isSuccess);

        void showUnFollowRes(boolean isSuccess);
    }
}
