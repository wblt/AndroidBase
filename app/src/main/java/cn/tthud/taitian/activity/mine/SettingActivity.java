package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.xutils.view.annotation.Event;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;

public class SettingActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_setting);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("设置");
        setTopLeftDefultListener();

    }

    @Event(value = {R.id.edit_phone,R.id.edit_pwd,R.id.lay_bind,R.id.lay_remove},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.edit_phone:
                intent = new Intent(this,ModifyPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_pwd:
                intent = new Intent(this,ModifyPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_bind:

                break;
            case R.id.lay_remove:

                break;

        }
    }
}
