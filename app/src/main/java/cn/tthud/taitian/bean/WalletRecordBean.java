package cn.tthud.taitian.bean;

/**
 * Created by bopeng on 2017/11/28.
 */

public class WalletRecordBean {
    private int id;                 // 充值ID
    private String solevar;         // 充值标识
    private String table;           // 数据名称
    private int dataid;             // 数据ID
    private String ub_id;           // 用户ID
    private String nickname;        // 充值人名称
    private String headpic;         // 充值人头像
    private String mobile;          // 手机号码
    private int status;             // 状态[1充值成功，2充值失败]
    private int source;             // 来源[1现金充值|2抽奖获得]
    private int money;              // 总金额
    private int usefee;             // 获得积分
    private int num;                // 充值数量
    private int addtime;            // 添加时间
    private int modtime;            // 修改时间
    private String adddate;         // 日期
    private int pay_type;           // 支付方式[1微信，2QQ,3微博，4支付宝，5其他]
    private String title;           // 标题
    private String type;               // 充值类型

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSolevar() {
        return solevar;
    }

    public void setSolevar(String solevar) {
        this.solevar = solevar;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getDataid() {
        return dataid;
    }

    public void setDataid(int dataid) {
        this.dataid = dataid;
    }

    public String getUb_id() {
        return ub_id;
    }

    public void setUb_id(String ub_id) {
        this.ub_id = ub_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getUsefee() {
        return usefee;
    }

    public void setUsefee(int usefee) {
        this.usefee = usefee;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public int getModtime() {
        return modtime;
    }

    public void setModtime(int modtime) {
        this.modtime = modtime;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
