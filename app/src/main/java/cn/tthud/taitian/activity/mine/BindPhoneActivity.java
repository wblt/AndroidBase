package cn.tthud.taitian.activity.mine;

import android.os.Bundle;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;

public class BindPhoneActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appendMainBody(this,R.layout.activity_bind_phone);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("绑定手机号码");
        setTopLeftDefultListener();
    }
}
