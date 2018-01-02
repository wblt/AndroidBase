package cn.tthud.taitian.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tthud.taitian.R;

/**
 * Created by wb on 2018/1/2.
 */

public class CustomAlertTowDialog extends Dialog implements View.OnClickListener {

    private ImageView mIvLogo;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;
    private String message;
    private Context context;
    private ViewClickListener viewClickListener;

    public CustomAlertTowDialog(@NonNull Context context) {
        super(context);
    }

    public CustomAlertTowDialog(Context context, int theme,String message,ViewClickListener viewClickListener) {
        super(context,theme);
        this.viewClickListener = viewClickListener;
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_sure_false);
        init();
    }

    private void init() {
        mIvLogo = (ImageView) findViewById(R.id.iv_logo);
        mTvSure = (TextView) findViewById(R.id.tv_sure);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvCancel.setOnClickListener(this);
        mTvSure.setOnClickListener(this);
        mTvContent.setText(message);
    }

    @Override
    public void onClick(View view) {
        if (viewClickListener != null) {
            viewClickListener.onClick(view);
        }
    }

    public interface ViewClickListener{
         void onClick(View view);
    }
}
