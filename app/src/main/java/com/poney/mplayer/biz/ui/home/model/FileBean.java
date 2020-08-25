package com.poney.mplayer.biz.ui.home.model;

import java.io.Serializable;

public class FileBean implements Serializable {
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

    public FileBean() {
    }

    public FileBean(String path, int count) {
        this.path = path;
        this.count = count;
    }

    public FileBean(String path, String name, int count) {
        this.path = path;
        this.name = name;
        this.count = count;
    }
}