package cn.tthud.taitian.bean;

/**
 * Created by bopeng on 2017/11/29.
 */

public class RechargeBean {
    private int id;                 // 充值ID
    private String solevar;         // 充值标识
    private String title;           // 标题
    private String remark;          // 充值备注
    private String icon;            // 图标
    private int money_total;        // 原价
    private int money_real;         // 实际价格
    private int jifen;              // 所兑换积分
    private int isdel;              // 是否显示[1开启，2关闭，3其他]
    private int addtime;            // 添加时间
    private int sort;               // 排序

    private boolean isSelect;       // 是否选中(客户端自定义)

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
