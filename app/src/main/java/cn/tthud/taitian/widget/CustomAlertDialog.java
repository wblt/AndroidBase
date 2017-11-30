package cn.tthud.taitian.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import cn.tthud.taitian.R;

/**
 * Created by wb on 2017/11/30.
 */

public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    private ViewClickListener viewClickListener;
    private Context context;

    public CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(Context context, int theme, ViewClickListener viewClickListener) {
        super(context,theme);
        this.viewClickListener = viewClickListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alert_dialog);
        init();
    }

    private void init() {

    }

    @Override
    public void onClick(View view) {
        if (viewClickListener != null) {
            viewClickListener.onClick(view);
        }
    }

    public interface ViewClickListener{
        public void onClick(View view);
    }
}
