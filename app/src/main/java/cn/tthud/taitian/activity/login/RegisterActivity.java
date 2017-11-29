package cn.tthud.taitian.activity.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class RegisterActivity extends ActivityBase {

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.login_pwd2)
    private EditText login_pwd2;

    @ViewInject(R.id.lay_xieyi)
    private LinearLayout lay_xieyi;


    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.pwd_xx2)
    private ImageView pwd_xx2;

    @ViewInject(R.id.btn_mine)
    private Button btn_mine;
    boolean btn_select = true;
    private String pwd;         // 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.register_activity_main);
//        appendTopBody(R.layout.activity_top_text);
//        setTopBarTitle("注册");
//        setTopLeftListener(this);

        initView();
        initListener();
    }

    // 初始化视图
    public void initView() {
        btn_mine.setSelected(true);
    }

    /*
     * 初始化监听器
	 */
    public void initListener() {
        login_pwd.addTextChangedListener(tw);
        login_pwd2.addTextChangedListener(tw);
    }

    @Event(value = {R.id.pwd_xx,R.id.pwd_xx2,R.id.lay_xieyi},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.lay_xieyi:
                break;
            case R.id.pwd_xx:
                login_pwd.setText("");
                break;
            case R.id.pwd_xx2:
                login_pwd2.setText("");
                break;
            case R.id.next_btn: // 注册

                break;
        }
    }

    

    // 输入框内容改变的监听器
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            if (getCurrentFocus() == null) {
                return;
            }
            int current = getCurrentFocus().getId();
            if (content.equals("")) {
                if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.login_pwd2) {
                    pwd_xx2.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.login_pwd2) {
                    pwd_xx2.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public void onTabClicked(View view) {
        if (btn_select == false) {
            btn_select = true;
            btn_mine.setSelected(true);
        } else {
            btn_select = false;
            btn_mine.setSelected(false);
        }
    }
}
