package cn.tthud.taitian.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wb on 2017/12/13.
 */


@Entity
public class Message {
    /*
     @Entity：将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类;
    @nameInDb：在数据库中的名字，如不写则为实体中类名；
    @Id：选择一个long / Long属性作为实体ID。 在数据库方面，它是主键。 参数autoincrement是设置ID值自增；
    @NotNull：使该属性在数据库端成为“NOT NULL”列。 通常使用@NotNull标记原始类型（long，int，short，byte）是有意义的；
    @Transient：表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化。
     */
    /*
    data[list][msg_id]、data[list][isread]、data[list][readtime]、data[list][istop]、
    data[list][mc_id]、data[list][icon]、
    data[list][url]、data[list][title]、data[list][ishref]、data[list][suetime]、data[type]
     */
    @Id(autoincrement = true)
    private Long _id;
    private String msg_id = "";
    private String isread = "";
    private String readtime = "";
    private String istop = "";
    private String mc_id = "";
    private String icon = "";
    private String url = "";
    private String title = "";
    private String ishref = "";
    private long suetime = 0;
    private String type = "";
    private String module = "";
    private String module_id = "";
    private String time_switch = "";
    @Generated(hash = 1878293657)
    public Message(Long _id, String msg_id, String isread, String readtime, String istop,
            String mc_id, String icon, String url, String title, String ishref, long suetime,
            String type, String module, String module_id, String time_switch) {
        this._id = _id;
        this.msg_id = msg_id;
        this.isread = isread;
        this.readtime = readtime;
        this.istop = istop;
        this.mc_id = mc_id;
        this.icon = icon;
        this.url = url;
        this.title = title;
        this.ishref = ishref;
        this.suetime = suetime;
        this.type = type;
        this.module = module;
        this.module_id = module_id;
        this.time_switch = time_switch;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getMsg_id() {
        return this.msg_id;
    }
    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }
    public String getIsread() {
        return this.isread;
    }
    public void setIsread(String isread) {
        this.isread = isread;
    }
    public String getReadtime() {
        return this.readtime;
    }
    public void setReadtime(String readtime) {
        this.readtime = readtime;
    }
    public String getIstop() {
        return this.istop;
    }
    public void setIstop(String istop) {
        this.istop = istop;
    }
    public String getMc_id() {
        return this.mc_id;
    }
    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIshref() {
        return this.ishref;
    }
    public void setIshref(String ishref) {
        this.ishref = ishref;
    }
    public long getSuetime() {
        return this.suetime;
    }
    public void setSuetime(long suetime) {
        this.suetime = suetime;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getModule() {
        return this.module;
    }
    public void setModule(String module) {
        this.module = module;
    }
    public String getModule_id() {
        return this.module_id;
    }
    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }
    public String getTime_switch() {
        return this.time_switch;
    }
    public void setTime_switch(String time_switch) {
        this.time_switch = time_switch;
    }

}

