package cn.tthud.taitian.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bopeng on 2017/11/2.
 */

public class ActivityBean {

    private String activity_id;     // 活动ID
    private String solevar;         // 活动唯一标识
    private String cp_id;           // 活动公司ID
    private String pid;             // 活动父ID
    private String title;           // 活动标题
    private String server;          // 活动服务器名称
    private String pre_domain;      // 活动域名前缀
    private String pre_activity;    // 活动前缀
    private String total;           // 活动总人数
    private String type;            // 活动类型[1 http,2 https]
    private String editor;          // 活动赛制
    private String cost;            // 活动费用
    private String status;          // 活动状态[1进行中，2未开始，3已结束]
    private String prise;           // 活动点赞数量
    private String isrecommend;     // 活动是否推荐[1是，2否]
    private String area;            // 活动地点
    private String start;           // 活动开始时间
    private String end;             // 活动结束时间
    private String sort;            // 活动排序
    private String modtime;         // 活动修改时间
    private String isdel;           // 活动是否删除[1是，2否]
    private String area_title;      // 活动地址
    private String com_title;       // 公司标题
    private List<String> img;       // 活动Banner
    private String thumb;           // 活动缩略图
    private String top;             // 排名
    private String description;     // 描述
    private String url;             // h5链接

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getSolevar() {
        return solevar;
    }

    public void setSolevar(String solevar) {
        this.solevar = solevar;
    }

    public String getCp_id() {
        return cp_id;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPre_domain() {
        return pre_domain;
    }

    public void setPre_domain(String pre_domain) {
        this.pre_domain = pre_domain;
    }

    public String getPre_activity() {
        return pre_activity;
    }

    public void setPre_activity(String pre_activity) {
        this.pre_activity = pre_activity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrise() {
        return prise;
    }

    public void setPrise(String prise) {
        this.prise = prise;
    }

    public String getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(String isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getModtime() {
        return modtime;
    }

    public void setModtime(String modtime) {
        this.modtime = modtime;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getArea_title() {
        return area_title;
    }

    public void setArea_title(String area_title) {
        this.area_title = area_title;
    }

    public String getCom_title() {
        return com_title;
    }

    public void setCom_title(String com_title) {
        this.com_title = com_title;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
