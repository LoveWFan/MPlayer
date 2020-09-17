package com.poney.mplayer.biz.ui.player;

import android.os.Environment;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poney.mplayer.biz.ui.player.model.FileBean;
import com.poney.mplayer.common.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Boolean> mRefreshStatus;
    private MutableLiveData<String> mErrorMsg;
    private MutableLiveData<List<FileBean>> mFileBeanList;
    private List<FileBean> fileBeanList = new ArrayList<>();
    private List<File> fileList = new ArrayList<>();

    public HomeViewModel() {
        mRefreshStatus = new MutableLiveData<>();
        mErrorMsg = new MutableLiveData<>();
        mFileBeanList = new MutableLiveData<>();
        mFileBeanList.setValue(fileBeanList);
    }

    public MutableLiveData<Boolean> getRefreshStatus() {
        return mRefreshStatus;
    }

    public MutableLiveData<List<FileBean>> getFileList() {
        return mFileBeanList;
    }

    public MutableLiveData<String> getErrorMsg() {
        return mErrorMsg;
    }

    public void getStorageFiles(FragmentActivity activity) {
        fileBeanList.clear();
        fileList.clear();
        File rootFile = Environment.getExternalStorageDirectory();
        if (rootFile != null) {
            Disposable disposable = Observable.just(rootFile)
                    .flatMap((Function<File, ObservableSource<File>>) this::listFiles)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fileList::add, throwable -> {
                        mErrorMsg.setValue(throwable.getMessage());
                    }, () -> {
                        groupByFiles(fileList);
                    });
        }
    }

    private void groupByFiles(List<File> fileList) {
        Disposable disposable = Observable.fromIterable(fileList)
                .groupBy(new Function<File, File>() {
                    @Override
                    public File apply(File file) throws Exception {
                        /**以视频文件的父文件夹路径进行分组**/
                        return file.getParentFile();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileGroupedObservable -> {
                    fileGroupedObservable.subscribe(new Observer<File>() {
                        int count = 0;
                        String parentName;

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(File file) {
                            count++;
                            parentName = file.getParentFile().getName();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            fileBeanList.add(new FileBean(String.valueOf(fileGroupedObservable.getKey()), parentName, count));
                            count = 0;
                            parentName = null;
                        }
                    });
                }, throwable -> {
                    mErrorMsg.setValue(throwable.getMessage());
                }, () -> {
                    mFileBeanList.postValue(fileBeanList);
                    mRefreshStatus.postValue(false);
                });
    }

    private Observable<File> listFiles(File f) {
        if (f.isDirectory()) {
            /**如果是文件夹就递归**/
            return Observable.fromArray(f.listFiles())
                    .flatMap((Function<File, Observable<File>>) this::listFiles);
        } else {
            /**filter操作符过滤视频文件,是视频文件就通知观察者**/
            return Observable.just(f)
                    .filter((Predicate<? super File>) file -> f.exists() && f.canRead() && FileUtils.isVideo(f));
        }
    }


}