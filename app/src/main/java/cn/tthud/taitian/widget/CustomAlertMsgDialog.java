package cn.tthud.taitian.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.tthud.taitian.R;

/**
 * Created by wb on 2017/12/26.
 */

public class CustomAlertMsgDialog extends Dialog implements View.OnClickListener {
    private String message;
    private Context context;
    private TextView tv_message;
    private TextView ok_btn;
    private ViewClickListener viewClickListener;

    public CustomAlertMsgDialog(Context context) {
        super(context);
    }

    public CustomAlertMsgDialog(Context context, int theme,String message,ViewClickListener viewClickListener) {
        super(context,theme);
        this.viewClickListener = viewClickListener;
        this.context = context;
        this.message = message;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alert_msg_dialog);
        init();
    }

    private void init() {
        tv_message = findViewById(R.id.tv_message);
        tv_message.setText(message);
        ok_btn = findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(this);
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
