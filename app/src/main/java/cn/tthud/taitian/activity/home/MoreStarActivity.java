package cn.tthud.taitian.activity.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;

public class MoreStarActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_more_star);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("更多学员");
        setTopLeftDefultListener();
    }
}
