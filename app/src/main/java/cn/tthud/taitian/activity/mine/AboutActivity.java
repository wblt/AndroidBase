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

        tv_about.setText("  无论你承认不承认、接受不接受，每个时代都会产生许多新的词语，其中有些新词语会迅速消亡，另一些，比如“上位”，则可能保存下来并得到广泛的使用。因此，认识新词语、学习新词语，也许应该成为现代人生活的一部分,爬上更高的地位”、“得到更高级的职位”，也可引申为“走红”、“上一个台阶。");
        tv_version.setText("版本: V"+VersionUtil.getAppVersionName(AboutActivity.this));
    }


}
