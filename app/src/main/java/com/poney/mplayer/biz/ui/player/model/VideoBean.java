package com.poney.mplayer.biz.ui.player.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 视频类
 */
public class VideoBean implements Parcelable {

    public String name;
    public String path;
    public String size;


    public VideoBean(String name, String path, String size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }


    protected VideoBean(Parcel in) {
        name = in.readString();
        path = in.readString();
        size = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };
}
