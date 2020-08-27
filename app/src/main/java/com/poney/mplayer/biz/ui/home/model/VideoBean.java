package com.poney.mplayer.biz.ui.home.model;

import java.io.Serializable;

/**
 * 视频类
 */
public class VideoBean implements Serializable {

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
