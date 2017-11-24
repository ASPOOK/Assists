package com.aspook.assists.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASPOOK on 17/7/18.
 */

public class FollowerWrapper implements Parcelable {
    private int id;
    private String created_at;
    private User follower;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.follower, flags);
    }

    public FollowerWrapper() {
    }

    protected FollowerWrapper(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.follower = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<FollowerWrapper> CREATOR = new Parcelable.Creator<FollowerWrapper>() {
        @Override
        public FollowerWrapper createFromParcel(Parcel source) {
            return new FollowerWrapper(source);
        }

        @Override
        public FollowerWrapper[] newArray(int size) {
            return new FollowerWrapper[size];
        }
    };
}
