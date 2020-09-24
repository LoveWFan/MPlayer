package com.poney.mplayer.biz.ui.player.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FileBean implements Parcelable {
    /**
     * 文件夹路径
     **/
    public String path;
    /**
     * 文件夹里有多少个视频文件
     **/
    public int count;
    private static final long serialVersionUID = 1L;
    public String name;


    public FileBean(String path, String name, int count) {
        this.path = path;
        this.name = name;
        this.count = count;
    }

    protected FileBean(Parcel in) {
        path = in.readString();
        count = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeInt(count);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel in) {
            return new FileBean(in);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };
}