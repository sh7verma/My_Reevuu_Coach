package com.sandrios.sandriosCamera.internal.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by app on 22-Dec-17.
 */

public class GalleryModel implements Parcelable {
    String path;
    int check,position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeInt(this.check);
        dest.writeInt(this.position);
    }

    public GalleryModel() {
    }

    protected GalleryModel(Parcel in) {
        this.path = in.readString();
        this.check = in.readInt();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<GalleryModel> CREATOR = new Parcelable.Creator<GalleryModel>() {
        @Override
        public GalleryModel createFromParcel(Parcel source) {
            return new GalleryModel(source);
        }

        @Override
        public GalleryModel[] newArray(int size) {
            return new GalleryModel[size];
        }
    };


}
