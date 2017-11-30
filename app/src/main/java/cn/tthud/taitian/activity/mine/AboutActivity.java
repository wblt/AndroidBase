package cn.tthud.taitian.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.utils.VersionUtil;

public class AboutActivity extends ActivityBase {

    @ViewInject(R.id.tv_version)
    private TextView tv_version;

    @ViewInject(R.id.tv_about)
    private TextView tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_about);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("关于我们");
        setTopLeftDefultListener();

        tv_version.setText(VersionUtil.getAppVersionName(AboutActivity.this));
    }


}
