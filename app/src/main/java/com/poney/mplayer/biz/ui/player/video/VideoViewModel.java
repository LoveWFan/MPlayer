package com.poney.mplayer.biz.ui.player.video;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poney.mplayer.biz.ui.player.model.VideoBean;
import com.poney.mplayer.common.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class VideoViewModel extends ViewModel {
    private MutableLiveData<Boolean> mProgress;
    private MutableLiveData<String> mErrorMsg;
    private MutableLiveData<List<VideoBean>> mVideoBeanList;
    private List<VideoBean> videoBeanList = new ArrayList<>();

    public VideoViewModel() {
        mErrorMsg = new MutableLiveData<>();
        mProgress = new MutableLiveData<>();
        mVideoBeanList = new MutableLiveData<>();
        mVideoBeanList.setValue(videoBeanList);
    }

    public MutableLiveData<List<VideoBean>> getVideoBeanList() {
        return mVideoBeanList;
    }

    public MutableLiveData<Boolean> getProgress() {
        return mProgress;
    }

    public void getVideoData(File directory) {
        if (directory != null && directory.isDirectory()) {
            Disposable subscribe = Observable.fromArray(Objects.requireNonNull(directory.listFiles(f -> f.exists() && f.canRead() && FileUtils.isVideo(f))))
                    .subscribe(
                            file -> {
                                String name = file.getName();
                                String size = FileUtils.showFileSize(file.length());
                                String path = file.getPath();
                                videoBeanList.add(new VideoBean(name, path, size));
                            }, throwable -> {
                                mErrorMsg.postValue(throwable.getMessage());
                            }
                            , () -> {
                                mProgress.postValue(false);
                            });

        }
    }
}
