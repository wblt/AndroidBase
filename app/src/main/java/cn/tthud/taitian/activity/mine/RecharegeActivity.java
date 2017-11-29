package cn.tthud.taitian.activity.mine;

import android.os.Bundle;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;

/**
 * Created by bopeng on 2017/11/29.
 */

public class RecharegeActivity extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_recharge);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("充值");
        setTopLeftDefultListener();
    }
}
