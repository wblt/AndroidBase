package cn.tthud.taitian.bean;

import java.util.List;

/**
 * Created by bopeng on 2017/11/20.
 */

public class CompanyBean {
    private int company_id;                 // 公司ID
    private String solevar;         // 公司唯一标识
    private String title;           // 公司标题
    private String abbtion;         // 公司标题简称
    private String mobile;          // 公司联系方式
    private String content;         // 公司简介
    private String trade;           // 公司所属行业
    private String geohash;         // 公司CEO
    private String area;            // 公司所属地区
    private String address;         // 公司省市县名称
    private String detail;          // 公司详细地址
    private String collect;         // 公司关注人数
    private String act_num;         // 公司活动数量
    private String sort;            // 公司排序号
    private String addtime;         // 公司添加时间
    private String isdel;           // 公司是否删除[1是，2否]
    private String thumb;           // 公司缩略图
    private List<String> img;       // 公司Banner

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getSolevar() {
        return solevar;
    }

    public void setSolevar(String solevar) {
        this.solevar = solevar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbbtion() {
        return abbtion;
    }

    public void setAbbtion(String abbtion) {
        this.abbtion = abbtion;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getAct_num() {
        return act_num;
    }

    public void setAct_num(String act_num) {
        this.act_num = act_num;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
