package cn.tthud.taitian.xutils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.common.TaskController;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by TianHongChun on 2016/5/14.
 * 网络工具,下载图片,下载文件，网络请求,同步、异步、UI线程任务,统一管理xutils
 */
public class MXUtils {

    /**
     * 开启下载任务
     * @param downLoadInfo
     * @param downLoadListener
     */
    public static void downLoadFileStart(DownLoadInfo downLoadInfo, DownLoadListener downLoadListener){
        DownLoadManage.getInstance().startDownload(downLoadInfo,downLoadListener);
    }
    public static void downLoadFileStop(DownLoadInfo downLoadInfo){
        DownLoadManage.getInstance().stopDownload(downLoadInfo);
    }
    public static void downLoadFileRemove(DownLoadInfo downLoadInfo){
        DownLoadManage.getInstance().removeDownload(downLoadInfo);
    }

    /**
     * 加载图片,
     * @param imageView
     * @param uri
     */
    public static void loadImage(ImageView imageView, String uri){
        x.image().bind(imageView,uri);
    }
    public static void loadImage(ImageView imageView, String uri, ImageOptions options){
        x.image().bind(imageView,uri,options);
    }
    public static void loadImage(ImageView imageView, String uri, Callback.CommonCallback<Drawable> callback){
        x.image().bind(imageView,uri,callback);
    }
    public static void loadImage(ImageView imageView, String uri, ImageOptions options, Callback.CommonCallback<Drawable> callback){
        x.image().bind(imageView,uri,options,callback);
    }
    public static void loadimageClearMemCache(){
        x.image().clearMemCache();
    }
    public static void loadImageClearCacheFiles(){
        x.image().clearCacheFiles();
    }

    /**
     * http
     * @param requestParams
     * @param callback
     */
    public static void httpPost(RequestParams requestParams, Callback.CommonCallback<String> callback){
            x.http().post(requestParams,callback);
    }
    public static void httpGet(RequestParams requestParams, Callback.CommonCallback<String> callback){
        x.http().get(requestParams,callback);
    }
    /**
     * 线程任务
     * @return
     */
    public static TaskController task(){
        return  x.task();
    }


}
