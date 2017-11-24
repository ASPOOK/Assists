package com.aspook.assists.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASPOOK on 17/7/19.
 */

public class Shot extends PureShot implements Parcelable {
    private User user;

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
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.user, flags);
    }

    public Shot() {
    }

    protected Shot(Parcel in) {
        super(in);
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Shot> CREATOR = new Creator<Shot>() {
        @Override
        public Shot createFromParcel(Parcel source) {
            return new Shot(source);
        }

        @Override
        public Shot[] newArray(int size) {
            return new Shot[size];
        }
    };
}
