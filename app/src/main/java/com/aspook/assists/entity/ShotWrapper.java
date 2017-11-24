package com.aspook.assists.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * used for liked shots
 * <p>
 * Created by ASPOOK on 17/7/19.
 */

public class ShotWrapper implements Parcelable {
    private int id;
    private String created_at;
    private Shot shot;

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

    public Shot getShot() {
        return shot;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.shot, flags);
    }

    public ShotWrapper() {
    }

    protected ShotWrapper(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.shot = in.readParcelable(Shot.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShotWrapper> CREATOR = new Parcelable.Creator<ShotWrapper>() {
        @Override
        public ShotWrapper createFromParcel(Parcel source) {
            return new ShotWrapper(source);
        }

        @Override
        public ShotWrapper[] newArray(int size) {
            return new ShotWrapper[size];
        }
    };
}
