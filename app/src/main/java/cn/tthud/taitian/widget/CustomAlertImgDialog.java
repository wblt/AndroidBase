package cn.tthud.taitian.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import cn.tthud.taitian.R;

/**
 * Created by wb on 2017/12/6.
 */

public class CustomAlertImgDialog extends Dialog {

    private String message;
    private Context context;
    private TextView tv_message;

    public CustomAlertImgDialog(Context context) {
        super(context);
    }

    public CustomAlertImgDialog(Context context, int theme,String message) {
        super(context,theme);
        this.context = context;
        this.message = message;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alert_img_dialog);
        init();
    }

    private void init() {
        tv_message = findViewById(R.id.tv_message);
        tv_message.setText(message);
    }
}
