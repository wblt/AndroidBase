package cn.tthud.taitian.bean;

/**
 * Created by wb on 2017/12/28.
 */

public class WeiXinBean {
    /*
    {"nickname":"冷婷","headimgurl":"https://qiniu.hnltou.com/self-headpic/414027cbf8447d55a591",
    "openid":"oJ_kvwqZAg0VENUjHXeHYr4EIoS4","sex":"2",
    "deviceid":"136ED171-35EA-4CE2-82D8-64839BDB6D5D","ub_id":"931ef9418ac8b64d5e44"}
     */
    private String nickname;
    private String headimgurl;
    private String openid;
    private String sex;
    private String deviceid;
    private String ub_id;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getUb_id() {
        return ub_id;
    }

    public void setUb_id(String ub_id) {
        this.ub_id = ub_id;
    }
}
