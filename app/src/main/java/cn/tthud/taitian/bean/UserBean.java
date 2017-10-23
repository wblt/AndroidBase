package cn.tthud.taitian.bean;

/**
 * Created by Administrator on 2016/7/28.
 */
public class UserBean {
    // 用户id
    private String ub_id;
    // 昵称
    private String nickname;
    // 头像
    private String headpic;
    // 性别 1：男  2：女
    private int sex;
    // 账号id
    private String ua_id;
    // 真实姓名
    private String realname;
    // 身份证
    private String idcard;
    // 邮箱
    private String email;
    // 个性签名
    private String stylesig;
    // 详细地址
    private String address;
    // 总积分
    private int totaljifen;

    private String mobile;

    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUb_id(String ub_id) {
        this.ub_id = ub_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setUa_id(String ua_id) {
        this.ua_id = ua_id;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStylesig(String stylesig) {
        this.stylesig = stylesig;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotaljifen(int totaljifen) {
        this.totaljifen = totaljifen;
    }

    public String getUb_id() {
        return ub_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public int getSex() {
        return sex;
    }

    public String getUa_id() {
        return ua_id;
    }

    public String getRealname() {
        return realname;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getEmail() {
        return email;
    }

    public String getStylesig() {
        return stylesig;
    }

    public String getAddress() {
        return address;
    }

    public int getTotaljifen() {
        return totaljifen;
    }
}
