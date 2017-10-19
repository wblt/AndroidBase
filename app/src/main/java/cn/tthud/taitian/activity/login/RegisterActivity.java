package cn.tthud.taitian.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;

public class RegisterActivity extends ActivityBase {

    @ViewInject(R.id.login_phone)
    private EditText login_phone;

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.login_pwd2)
    private EditText login_pwd2;

    @ViewInject(R.id.code)
    private EditText code;

    @ViewInject(R.id.pn)
    private EditText pn;

    @ViewInject(R.id.login_send_code)
    private TextView login_send_code;

    @ViewInject(R.id.lay_xieyi)
    private LinearLayout lay_xieyi;

    @ViewInject(R.id.next_btn)
    private TextView next_btn;

    @ViewInject(R.id.username_xx)
    private ImageView username_xx;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.pwd_xx2)
    private ImageView pwd_xx2;

    @ViewInject(R.id.pn_xx)
    private ImageView pn_xx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.register_activity_main);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("注册");
        setTopLeftListener(this);
    }

    // 初始化视图
    public void initView() {

    }

    /*
     * 初始化监听器
	 */
    public void initListener() {

    }

    @Event(value = {R.id.login_send_code,R.id.next_btn,R.id.username_xx,
            R.id.pwd_xx,R.id.pn_xx,R.id.pwd_xx2,R.id.lay_xieyi},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int viewId = view.getId();
        switch (viewId) {

        }
    }
}
