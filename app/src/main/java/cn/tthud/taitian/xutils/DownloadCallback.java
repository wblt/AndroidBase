package cn.tthud.taitian.xutils;

import android.util.Log;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by TianHongChun on 2016/5/8.
 * 文件下载监听
 */
public class DownloadCallback implements
        Callback.CommonCallback<File>,Callback.ProgressCallback<File>,Callback.Cancelable {

    private Cancelable cancelable;
    private boolean cancelled = false;
    DownLoadListener downLoadListener;
    DownLoadInfo downLoadInfo;

    public DownloadCallback(DownLoadListener downLoadListener){
        this.downLoadListener=downLoadListener;
        this.downLoadInfo=(DownLoadInfo)downLoadListener.getDownLoadPojo();
    }

    private void log(String content){
        Log.i(DownloadCallback.class.getName(), content);
    }


    public void setCancelable(Cancelable cancelable){
        this.cancelable=cancelable;
    }

//////////////////////
// ///////// CommonCallback/////////////
// //////////////////////
    @Override
    public void onSuccess(File result) {
        downLoadInfo.setState(DownloadState.FINISHED);
        downLoadListener.onSuccess(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        downLoadInfo.setState(DownloadState.ERROR);
        downLoadListener.onError(ex,isOnCallback);
    }

    @Override
    public void onCancelled(CancelledException cex) {
        downLoadInfo.setState(DownloadState.STOPPED);
        downLoadListener.onCancelled(cex);
    }

    @Override
    public void onFinished() {

    }


///////////////////////////
///////////ProgressCallback//////////////
    /////////////////////////

    @Override
    public void onWaiting() {
        downLoadInfo.setState(DownloadState.WAITING);
        downLoadListener.onWaiting();
    }

    @Override
    public void onStarted() {
        downLoadInfo.setState(DownloadState.STARTED);
        downLoadListener.onStarted();
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        downLoadInfo.setState(DownloadState.STARTED);
        downLoadInfo.setFileLength(total);
        downLoadInfo.setProgress((int) (current * 100 / total));
        downLoadListener.onLoading(total,current);
    }

//Cancelable
    @Override
    public void cancel() {
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
