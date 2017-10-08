package cn.tthud.taitian.xutils;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by TianHongChun on 2016/5/8.
 */
public interface DownLoadListener {

    public  void onWaiting();

    public  void onStarted();

    public  void onLoading(long total, long current);

    public  void onSuccess(File result);

    public  void onError(Throwable ex, boolean isOnCallback);

    public  void onCancelled(Callback.CancelledException cex);
    public Object getDownLoadPojo();

}
