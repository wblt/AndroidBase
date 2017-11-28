package cn.tthud.taitian.bean;

/**
 * Created by bopeng on 2017/11/28.
 */

public class MessageBean {
    private int msg_id;     // 消息编号
    private int isread;     // 是否阅读[1是，2否]
    private int readtime;   // 阅读时间
    private int istop;      // 是否置顶[1是，2否]
    private int mc_id;      // 消息类别编号
    private String icon;    // 图标
    private String url;     // 链接
    private String title;   // 标题

    public int getMsg_id() {
        return msg_id;
    }

    public int getIsread() {
        return isread;
    }

    public int getReadtime() {
        return readtime;
    }

    public int getIstop() {
        return istop;
    }

    public int getMc_id() {
        return mc_id;
    }

    public String getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public void setReadtime(int readtime) {
        this.readtime = readtime;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    public void setMc_id(int mc_id) {
        this.mc_id = mc_id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
