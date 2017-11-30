package cn.tthud.taitian.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tthud.taitian.R;

/**
 * Created by wb on 2017/11/30.
 */

public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    private ViewClickListener viewClickListener;
    private Context context;

    private TextView tv_message;
    private ImageView iv_close;
    private TextView tv_contain;
    private String message;


    public CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(Context context, int theme,String message, ViewClickListener viewClickListener) {
        super(context,theme);
        this.viewClickListener = viewClickListener;
        this.context = context;
        this.message = message;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alert_dialog);
        init();
    }

    private void init() {
        tv_message = findViewById(R.id.tv_message);
        iv_close = findViewById(R.id.iv_close);
        tv_contain = findViewById(R.id.tv_contain);
        tv_message.setOnClickListener(this);
        tv_contain.setOnClickListener(this);
        tv_message.setText(message);
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
