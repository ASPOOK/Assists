package com.aspook.assists.http;

import com.aspook.assists.entity.Comment;
import com.aspook.assists.entity.FolloweeWrapper;
import com.aspook.assists.entity.FollowerWrapper;
import com.aspook.assists.entity.PureShot;
import com.aspook.assists.entity.Shot;
import com.aspook.assists.entity.ShotWrapper;
import com.aspook.assists.entity.Token;
import com.aspook.assists.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * All services defined here using Retrofit
 * <p>
 * Created by ASPOOK on 17/6/28.
 */

public interface DribbbleService {

    @GET("shots")
    Observable<List<Shot>> getShots(@Query("access_token") String token,
                                    @Query("list") String type,
                                    @Query("timeframe") String period,
                                    @Query("sort") String sort,
                                    @Query("page") int page,
                                    @Query("per_page") int count);

    @GET("shots/{id}/comments")
    Observable<List<Comment>> getShotComments(@Path("id") int shotId,
                                              @Query("access_token") String token,
                                              @Query("page") int page,
                                              @Query("per_page") int count);

    @GET("users/{id}/shots")
    Observable<List<PureShot>> getUserShots(@Path("id") int userId,
                                            @Query("access_token") String token,
                                            @Query("page") int page,
                                            @Query("per_page") int count);

    @GET("users/{id}/likes")
    Observable<List<ShotWrapper>> getLikedShots(@Path("id") int userId,
                                                @Query("access_token") String token,
                                                @Query("page") int page,
                                                @Query("per_page") int count);

    @GET("users/{id}/followers")
    Observable<List<FollowerWrapper>> getFollowers(@Path("id") int userId,
                                                   @Query("access_token") String token,
                                                   @Query("page") int page,
                                                   @Query("per_page") int count);

    @GET("users/{id}/following")
    Observable<List<FolloweeWrapper>> getFollowing(@Path("id") int userId,
                                                   @Query("access_token") String token,
                                                   @Query("page") int page,
                                                   @Query("per_page") int count);

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Token> getAccessToken(@Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("code") String code);

    @GET("user")
    Observable<User> getAuthenticatedUser(@Query("access_token") String oAuthToken);

    @GET("user/following/shots")
    Observable<List<Shot>> getFollowingShots(@Query("access_token") String oAuthToken,
                                             @Query("page") int page,
                                             @Query("per_page") int count);

    @GET("user/following/{id}")
    Observable<Response<String>> checkFollowing(@Path("id") int userId,
                                                @Query("access_token") String oAuthToken);

    @PUT("users/{id}/follow")
    Observable<Response<String>> follow(@Path("id") int userId,
                                        @Query("access_token") String oAuthToken);

    @DELETE("users/{id}/follow")
    Observable<Response<String>> unFollow(@Path("id") int userId,
                                          @Query("access_token") String oAuthToken);

    @GET("shots/{id}/like")
    Observable<Response<Object>> checkLike(@Path("id") int shotId,
                                           @Query("access_token") String oAuthToken);

    @FormUrlEncoded
    @POST("shots/{id}/like")
    Observable<Response<Object>> like(@Path("id") int shotId,
                                      @Field("access_token") String oAuthToken);

    @DELETE("shots/{id}/like")
    Observable<Response<Object>> unlike(@Path("id") int shotId,
                                        @Query("access_token") String oAuthToken);


}
