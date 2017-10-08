package cn.tthud.taitian.xutils;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Created by TianHongChun on 2016/5/8.
 * 文件下载管理
 */
public class DownLoadManage {
    /**
     * 同时下载数,最多3个
     */
    private final Executor executor = new PriorityExecutor(1, true);
    private final List<DownLoadInfo> downloadInfoList = new ArrayList<DownLoadInfo>();
    /**
     *
     */
    private final ConcurrentHashMap<DownLoadInfo, DownloadCallback> callbackMap = new ConcurrentHashMap<DownLoadInfo, DownloadCallback>(5);

    private static DownLoadManage downLoadManage;
    public static DownLoadManage getInstance(){
        if(downLoadManage==null){
            synchronized (DownLoadManage.class) {
                if(downLoadManage==null){
                    downLoadManage=new DownLoadManage();
                }
            }
        }
        return downLoadManage;
    }

    public synchronized void startDownload(DownLoadInfo downLoadInfo,DownLoadListener downLoadListener)  {
        String fileSavePath = new File(downLoadInfo.getFileSavePath()).getAbsolutePath();
        RequestParams params = new RequestParams(downLoadInfo.getUrl());
        params.setAutoResume(true);
        params.setAutoRename(false);
        params.setSaveFilePath(fileSavePath);
        params.setExecutor(executor);
        params.setCancelFast(true);

        DownloadCallback callback = new DownloadCallback(downLoadListener);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        callback.setCancelable(cancelable);
        callbackMap.put(downLoadInfo, callback);

        if (downloadInfoList.contains(downLoadInfo)) {
            int index = downloadInfoList.indexOf(downLoadInfo);
            downloadInfoList.remove(downLoadInfo);
            downloadInfoList.add(index, downLoadInfo);
        } else {
            downloadInfoList.add(downLoadInfo);
        }
    }
    public void stopDownload(int index) {
        DownLoadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }
    public void stopDownload(DownLoadInfo downloadInfo) {
        Callback.Cancelable cancelable = callbackMap.get(downloadInfo);
        if (cancelable != null) {
            cancelable.cancel();
        }
    }
    public void removeDownload(int index) {
        DownLoadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
        downloadInfoList.remove(index);
    }
    public void removeDownload(DownLoadInfo downloadInfo) {
        stopDownload(downloadInfo);
        downloadInfoList.remove(downloadInfo);
    }
}
