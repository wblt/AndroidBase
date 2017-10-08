package cn.tthud.taitian.xutils;

import android.graphics.Bitmap;
import android.widget.ImageView;


import org.xutils.image.ImageOptions;

import cn.tthud.taitian.R;

/**
 * Created by TianHongChun on 2016/5/14.
 */
public class MImagOptions {

    private static ImageOptions circularImageOptions;

    private static ImageOptions imageOptions;

    private static ImageOptions imageOptionsRadius;

    //圆形图片加载配置
    public static ImageOptions getCircularImageOptions() {
        if (circularImageOptions==null){
            circularImageOptions = new ImageOptions.Builder()
                    .setCircular(true)
                    .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                    .setUseMemCache(true)
                    .setConfig(Bitmap.Config.RGB_565)
                    .setLoadingDrawableId(R.mipmap.head_default)
                    .setFailureDrawableId(R.mipmap.head_default)
                    .build();
        }
        return circularImageOptions;
    }

    //方形图片加载配置
    public static ImageOptions getCircularImageOptionsRadius() {
        if (imageOptionsRadius==null){
            imageOptionsRadius = new ImageOptions.Builder()
                    .setRadius(15)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.mipmap.icon_default)
                    .setFailureDrawableId(R.mipmap.icon_default)
                    .setConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return imageOptionsRadius;
    }


    //方形图片加载配置
    public static ImageOptions getImageOptions() {
        if (imageOptions==null){
            imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                    .setLoadingDrawableId(R.mipmap.icon_default)
                    .setFailureDrawableId(R.mipmap.icon_default)
                    .setConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return imageOptions;
    }
    //方形图片加载配置
    public static ImageOptions getImageOptionsByCrop() {
        if (imageOptions==null){
            imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.mipmap.icon_default)
                    .setFailureDrawableId(R.mipmap.icon_default)
                    .setConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return imageOptions;
    }

}
