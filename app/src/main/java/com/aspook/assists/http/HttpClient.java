package com.aspook.assists.http;

import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ASPOOK on 17/6/28.
 */

public class HttpClient {
    // TODO please change to your own token provided by Dribbble
    private static final String ACCESS_TOKEN = "your_token";
    private static final String BASE_URL = "https://api.dribbble.com/v1/";

    private DribbbleService mService;

    private HttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mService = retrofit.create(DribbbleService.class);
    }

    public static HttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.d("RetrofitMessage:" + message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        return builder.build();
    }

    public void loadShots(Observer observer, String type, String period, String sort, int page, int pageCount) {
        if (mService != null) {
            mService.getShots(ACCESS_TOKEN, type, period, sort, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadComments(Observer observer, int shotId, int page, int pageCount) {
        if (mService != null) {
            mService.getShotComments(shotId, ACCESS_TOKEN, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadUserShots(Observer observer, int userId, int page, int pageCount) {
        if (mService != null) {
            mService.getUserShots(userId, ACCESS_TOKEN, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadLikedShots(Observer observer, int userId, int page, int pageCount) {
        if (mService != null) {
            mService.getLikedShots(userId, ACCESS_TOKEN, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadFollowing(Observer observer, int userId, int page, int pageCount) {
        if (mService != null) {
            mService.getFollowing(userId, ACCESS_TOKEN, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadFollowers(Observer observer, int userId, int page, int pageCount) {
        if (mService != null) {
            mService.getFollowers(userId, ACCESS_TOKEN, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadAuthenticatedUser(Observer observer, String token) {
        if (mService != null) {
            mService.getAuthenticatedUser(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void loadFollowingShots(Observer observer, String token, int page, int pageCount) {
        if (mService != null) {
            mService.getFollowingShots(token, page, pageCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void checkFollowing(Observer observer, int userId, String token) {
        if (mService != null) {
            mService.checkFollowing(userId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void follow(Observer observer, int userId, String token) {
        if (mService != null) {
            mService.follow(userId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void unFollow(Observer observer, int userId, String token) {
        if (mService != null) {
            mService.unFollow(userId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void checkLikeStatus(Observer observer, int shotId, String token) {
        if (mService != null) {
            mService.checkLike(shotId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void like(Observer observer, int shotId, String token) {
        if (mService != null) {
            mService.like(shotId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void unLike(Observer observer, int shotId, String token) {
        if (mService != null) {
            mService.unlike(shotId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }
}
