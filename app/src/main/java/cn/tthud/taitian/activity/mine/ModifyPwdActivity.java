package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class ModifyPwdActivity extends ActivityBase {

    @ViewInject(R.id.old_pwd)
    private EditText old_pwd;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.new_pwd)
    private EditText new_pwd;

    @ViewInject(R.id.pwd_xx2)
    private ImageView pwd_xx2;

    @ViewInject(R.id.new_pwd2)
    private EditText new_pwd2;

    @ViewInject(R.id.pwd2_xx2)
    private ImageView pwd2_xx2;

    @ViewInject(R.id.login_btn)
    private Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_modify_pwd);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("修改密码");
        setTopLeftDefultListener();

        initView();
        initListener();
    }

    private void initView() {

    }

    /*
         * 初始化监听器
         */
    public void initListener() {
        old_pwd.addTextChangedListener(tw);
        new_pwd.addTextChangedListener(tw);
        new_pwd2.addTextChangedListener(tw);
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
                if (current == R.id.old_pwd) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.new_pwd) {
                    pwd_xx2.setVisibility(View.INVISIBLE);
                } else if (current == R.id.new_pwd2) {
                    pwd2_xx2.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.old_pwd) {
                    pwd_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.new_pwd) {
                    pwd_xx2.setVisibility(View.VISIBLE);
                } else if (current == R.id.new_pwd2) {
                    pwd2_xx2.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Event(value = {R.id.pwd_xx,R.id.pwd_xx2,R.id.pwd2_xx2,R.id.login_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.login_btn:
                submit();
                break;
            case R.id.pwd_xx:
                old_pwd.setText("");
                break;
            case R.id.pwd_xx2:
                new_pwd.setText("");
                break;
            case R.id.pwd2_xx2:
                new_pwd2.setText("");
                break;
        }
    }

    private void submit() {
        String oldpassword = old_pwd.getText().toString();
        final String newpassword = new_pwd.getText().toString();
        String newpassword2 = new_pwd2.getText().toString();
        if (TextUtils.isEmpty(oldpassword)) {
            showMsg("原始密码为空");
            return;
        }
        if (TextUtils.isEmpty(newpassword)) {
            showMsg("新密码为空");
            return;
        }
        if (!newpassword2.equals(newpassword)) {
            showMsg("新的密码输入不一致");
            return;
        }
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_EDIT_PWD);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("password", newpassword);
        requestParams.addParameter("usedpwd", oldpassword);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("修改密码",requestParams) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg("修改成功");
                        SPUtils.putString(SPUtils.PASSWORD,newpassword);
                        finish();
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
