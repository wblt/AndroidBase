package cn.tthud.taitian.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class LoginActivity extends ActivityBase {

    @ViewInject(R.id.login_phone)
    private EditText login_phone;

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.register_btn)
    private TextView register_btn;

    @ViewInject(R.id.forget_pwd)
    private TextView forget_pwd;

    @ViewInject(R.id.username_xx)
    private ImageView username_xx;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.login_btn)
    private TextView login_btn;

    private String phone;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.login_activity_main);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("登录");
        setTopLeftDefultListener();
        initListener();

    }

    /*
         * 初始化监听器
         */
    public void initListener() {
        login_phone.addTextChangedListener(tw);
        login_pwd.addTextChangedListener(tw);
    }

    // 输入框内容改变的监听器
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            if (getCurrentFocus() == null) {
                return;
            }
            int current = getCurrentFocus().getId();
            if (content.equals("")) {
                if (current == R.id.login_phone) {
                    username_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.login_phone) {
                    username_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Event(value = {R.id.register_btn,R.id.forget_pwd,R.id.login_btn,R.id.username_xx,R.id.pwd_xx},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        Intent intent ;
        switch (id){
            case R.id.login_btn:
                //UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                personCenter();
                break;
            case R.id.register_btn:
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_pwd:
                intent = new Intent(LoginActivity.this,ForgetPwdActivity.class);
                startActivity(intent);
                break;
        }
    }


    public static void navToLogin(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String temp = "";
            for (String key : data.keySet()) {
                temp = temp + key + " : " + data.get(key) + "\n";
            }
            Log.i(temp);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            result.setText("错误" + t.getMessage());
            Log.i("错误" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    private void personCenter() {
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.PERSONAL_CENTER);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("个人中心获取",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    Log.i("请求回调-------");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
