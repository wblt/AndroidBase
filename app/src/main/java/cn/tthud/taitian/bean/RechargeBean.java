package cn.tthud.taitian.bean;

/**
 * Created by bopeng on 2017/11/29.
 */

public class RechargeBean {
    private int rl_id;
    private String solevar;
    private String title;
    private String remark;
    private String icon;
    private int money_total;
    private int money_real;
    private int jifen;
    private int isdel;              // 是否显示[1开启，2关闭，3其他]
    private int addtime;
    private int sort;

    private boolean isSelect;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public int getRl_id() {
        return rl_id;
    }

    public String getSolevar() {
        return solevar;
    }

    public String getTitle() {
        return title;
    }

    public String getRemark() {
        return remark;
    }

    public String getIcon() {
        return icon;
    }

    public int getMoney_total() {
        return money_total;
    }

    public int getMoney_real() {
        return money_real;
    }

    public int getJifen() {
        return jifen;
    }

    public int getIsdel() {
        return isdel;
    }

    public int getAddtime() {
        return addtime;
    }

    public int getSort() {
        return sort;
    }

    public void setRl_id(int rl_id) {
        this.rl_id = rl_id;
    }

    public void setSolevar(String solevar) {
        this.solevar = solevar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setMoney_total(int money_total) {
        this.money_total = money_total;
    }

    public void setMoney_real(int money_real) {
        this.money_real = money_real;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
