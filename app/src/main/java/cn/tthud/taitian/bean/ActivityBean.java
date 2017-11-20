package cn.tthud.taitian.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bopeng on 2017/11/2.
 */

public class ActivityBean implements Parcelable{

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


    public List<String> getImg() {
        return img;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getThumb() {
        return thumb;
    }

    public String getSolevar() {
        return solevar;
    }

    public String getCp_id() {
        return cp_id;
    }

    public String getPid() {
        return pid;
    }

    public String getTitle() {
        return title;
    }

    public String getServer() {
        return server;
    }

    public String getPre_domain() {
        return pre_domain;
    }

    public String getPre_activity() {
        return pre_activity;
    }

    public String getTotal() {
        return total;
    }

    public String getType() {
        return type;
    }

    public String getEditor() {
        return editor;
    }

    public String getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public String getPrise() {
        return prise;
    }

    public String getIsrecommend() {
        return isrecommend;
    }

    public String getArea() {
        return area;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getSort() {
        return sort;
    }

    public String getModtime() {
        return modtime;
    }

    public String getIsdel() {
        return isdel;
    }

    public String getArea_title() {
        return area_title;
    }

    public String getCom_title() {
        return com_title;
    }

    public String getTop() {
        return top;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public void setSolevar(String solevar) {
        this.solevar = solevar;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPre_domain(String pre_domain) {
        this.pre_domain = pre_domain;
    }

    public void setPre_activity(String pre_activity) {
        this.pre_activity = pre_activity;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrise(String prise) {
        this.prise = prise;
    }

    public void setIsrecommend(String isrecommend) {
        this.isrecommend = isrecommend;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setModtime(String modtime) {
        this.modtime = modtime;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public void setArea_title(String area_title) {
        this.area_title = area_title;
    }

    public void setCom_title(String com_title) {
        this.com_title = com_title;
    }

    public void setTop(String top) {
        this.top = top;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activity_id);
        dest.writeString(this.solevar);
        dest.writeString(this.cp_id);
        dest.writeString(this.pid);
        dest.writeString(this.title);
        dest.writeString(this.server);
        dest.writeString(this.pre_domain);
        dest.writeString(this.pre_activity);
        dest.writeString(this.total);
        dest.writeString(this.type);
        dest.writeString(this.editor);
        dest.writeString(this.cost);
        dest.writeString(this.status);
        dest.writeString(this.prise);
        dest.writeString(this.isrecommend);
        dest.writeString(this.area);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.sort);
        dest.writeString(this.modtime);
        dest.writeString(this.isdel);
        dest.writeString(this.area_title);
        dest.writeString(this.com_title);
        dest.writeStringList(this.img);
        dest.writeString(this.thumb);
        dest.writeString(this.top);
        dest.writeString(this.description);
        dest.writeString(this.url);
    }

    public ActivityBean() {
    }

    protected ActivityBean(Parcel in) {
        this.activity_id = in.readString();
        this.solevar = in.readString();
        this.cp_id = in.readString();
        this.pid = in.readString();
        this.title = in.readString();
        this.server = in.readString();
        this.pre_domain = in.readString();
        this.pre_activity = in.readString();
        this.total = in.readString();
        this.type = in.readString();
        this.editor = in.readString();
        this.cost = in.readString();
        this.status = in.readString();
        this.prise = in.readString();
        this.isrecommend = in.readString();
        this.area = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.sort = in.readString();
        this.modtime = in.readString();
        this.isdel = in.readString();
        this.area_title = in.readString();
        this.com_title = in.readString();
        this.img = in.createStringArrayList();
        this.thumb = in.readString();
        this.top = in.readString();
        this.description = in.readString();
        this.url = in.readString();
    }

    public static final Creator<ActivityBean> CREATOR = new Creator<ActivityBean>() {
        @Override
        public ActivityBean createFromParcel(Parcel source) {
            return new ActivityBean(source);
        }

        @Override
        public ActivityBean[] newArray(int size) {
            return new ActivityBean[size];
        }
    };
}
