package com.poney.mplayer.biz.ui.home.model;

/**
 * 视频类
 */
public class VideoBean {

    public String name;
    public String path;
    public String size;

    public VideoBean(String name, String size) {
        this.name = name;
        this.size = size;
    }

    public VideoBean(String name, String path, String size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }


}
