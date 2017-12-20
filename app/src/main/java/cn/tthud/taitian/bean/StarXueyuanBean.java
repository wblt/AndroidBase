package cn.tthud.taitian.bean;

/**
 * Created by wb on 2017/11/27.
 */

public class StarXueyuanBean {
    /*
    "realname":"毛六场",
                "pl_id":"5995b6c49f84d7776d40",
                "title":"春晚第六场",
                "url":"http://new.tthud.cn/wechat.php/tcb_appDDis",
                "img":"http://qiniu.hnltou.com/logo-shangwei.jpg"
     */
    private String realname;
    private String title;
    private String img;
    private String url;
    private String pl_id;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPl_id() {
        return pl_id;
    }

    public void setPl_id(String pl_id) {
        this.pl_id = pl_id;
    }
}
