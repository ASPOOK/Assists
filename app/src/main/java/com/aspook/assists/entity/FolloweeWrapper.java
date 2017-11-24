package com.aspook.assists.entity;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASPOOK on 17/7/18.
 */

public class FolloweeWrapper implements Parcelable {
    private int id;
    private String created_at;
    private User followee;

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

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.followee, flags);
    }

    public FolloweeWrapper() {
    }

    protected FolloweeWrapper(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.followee = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<FolloweeWrapper> CREATOR = new Parcelable.Creator<FolloweeWrapper>() {
        @Override
        public FolloweeWrapper createFromParcel(Parcel source) {
            return new FolloweeWrapper(source);
        }

        @Override
        public FolloweeWrapper[] newArray(int size) {
            return new FolloweeWrapper[size];
        }
    };
}
