package cn.tthud.taitian.widget.banner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.tthud.taitian.R;

public class SimpleImageBanner extends BaseIndicaorBanner<BannerItem, SimpleImageBanner> {
    private ColorDrawable colorDrawable;
    private Context context;
    
    public SimpleImageBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
        this.context = context;
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        final BannerItem item = list.get(position);
        tv.setText(item.title);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(context, R.layout.banner_adapter_simple_image, null);
        ImageView iv = ViewFindUtils.find(inflate, R.id.iv);
        final BannerItem item = list.get(position);
        int itemWidth = dm.widthPixels;
        int itemHeight = (int) (itemWidth * 2 * 1.0f / 4);
        //iv.setScaleType(ImageView.ScaleType.FIT_XY);
        //iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = item.imgUrl;
        if (!TextUtils.isEmpty(imgUrl)) {
//        	f.configLoadfailImage(R.drawable.default_700);
//        	f.configLoadingImage(R.drawable.default_700);
//        	f.display(iv, imgUrl);
            Glide.with(context)
                    .load(imgUrl)
                    //.override(itemWidth, itemHeight)
                    //.centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv);
        } else {
            iv.setImageDrawable(colorDrawable);
        }
        return inflate;
    }
}
