package com.aspook.assists.http;

import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.android.BuildConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ASPOOK on 17/7/25.
 */

public class HttpOauthClient {
    private static final String BASE_OAUTH_URL = "https://dribbble.com/";

    // TODO please change to your own client id and client secret
    private static final String CLIENT_ID = "your_client_id";
    private static final String CLIENT_SECRET = "your_client_secret";
    private DribbbleService mService;

    private HttpOauthClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(BASE_OAUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mService = retrofit.create(DribbbleService.class);
    }

    public static HttpOauthClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpOauthClient INSTANCE = new HttpOauthClient();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    Logger.d("RetrofitMessage:" + message);
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        return builder.build();
    }

    public void getAccessToken(Observer observer, String code) {
        if (mService != null) {
            mService.getAccessToken(CLIENT_ID, CLIENT_SECRET, code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }
}
