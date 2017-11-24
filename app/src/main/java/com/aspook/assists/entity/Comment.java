package com.aspook.assists.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASPOOK on 17/7/7.
 */

public class Comment implements Parcelable {

    /**
     * id : 6354085
     * body : <p>This is beautiful Nick! I've got a long flight to Tokyo coming up, would love to animate this if you'd be down. </p>
     * likes_count : 0
     * likes_url : https://api.dribbble.com/v1/shots/3634954/comments/6354085/likes
     * created_at : 2017-07-06T17:35:38Z
     * updated_at : 2017-07-06T17:35:38Z
     * user : {"id":492711,"name":"Remington McElhaney","username":"Remington_M","html_url":"https://dribbble.com/Remington_M","avatar_url":"https://cdn.dribbble.com/users/492711/avatars/normal/30fb71fa287891f60bd1b8fe36df9f27.jpg?1462144989","bio":"UX Motion Designer at Google.","location":"Mountain View, CA","links":{"web":"http://www.RemingtonM.com","twitter":"https://twitter.com/RemingtonM_"},"buckets_count":0,"comments_received_count":163,"followers_count":687,"followings_count":232,"likes_count":1991,"likes_received_count":2355,"projects_count":0,"rebounds_received_count":0,"shots_count":27,"teams_count":1,"can_upload_shot":true,"type":"Player","pro":false,"buckets_url":"https://api.dribbble.com/v1/users/492711/buckets","followers_url":"https://api.dribbble.com/v1/users/492711/followers","following_url":"https://api.dribbble.com/v1/users/492711/following","likes_url":"https://api.dribbble.com/v1/users/492711/likes","projects_url":"https://api.dribbble.com/v1/users/492711/projects","shots_url":"https://api.dribbble.com/v1/users/492711/shots","teams_url":"https://api.dribbble.com/v1/users/492711/teams","created_at":"2014-02-03T18:24:13Z","updated_at":"2017-07-06T21:40:21Z"}
     */

    private int id;
    private String body;
    private int likes_count;
    private String likes_url;
    private String created_at;
    private String updated_at;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.body);
        dest.writeInt(this.likes_count);
        dest.writeString(this.likes_url);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.id = in.readInt();
        this.body = in.readString();
        this.likes_count = in.readInt();
        this.likes_url = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
