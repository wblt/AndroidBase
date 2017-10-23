package cn.tthud.taitian.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;

public class ModifyInfoActivity extends ActivityBase {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appendMainBody(this,R.layout.activity_modify_info);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("完善个人信息");
        setTopLeftDefultListener();
    }
}
