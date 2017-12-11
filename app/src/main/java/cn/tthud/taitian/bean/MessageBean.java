package cn.tthud.taitian.bean;

/**
 * Created by bopeng on 2017/11/28.
 */

/*
                "msg_id":3,
                "isread":2,
                "readtime":0,
                "istop":2,
                "mc_id":"广告通知",
                "icon":"https://qiniu.hnltou.com/538f7d04a2266b7b.jpg",
                "url":"",
                "title":"火爆~~双11来袭，全场1折，骨折、骨折呀！",
                "ishref":2,
                "suetime":"20小时前"
 */
public class MessageBean {
    private int msg_id;
    private int isread;
    private int readtime;
    private int istop;
    private String mc_id;
    private String icon;
    private String url;
    private String title;
    private String ishref;
    private String module;
    private String module_id;
    private String suetime;

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public int getReadtime() {
        return readtime;
    }

    public void setReadtime(int readtime) {
        this.readtime = readtime;
    }

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    public String getMc_id() {
        return mc_id;
    }

    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIshref() {
        return ishref;
    }

    public void setIshref(String  ishref) {
        this.ishref = ishref;
    }

    public String getSuetime() {
        return suetime;
    }

    public void setSuetime(String suetime) {
        this.suetime = suetime;
    }

    public String getModule() {
        return module;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }
}
