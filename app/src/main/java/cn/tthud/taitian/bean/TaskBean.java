package cn.tthud.taitian.bean;

/**
 * Created by wb on 2017/12/2.
 */
/*
"task_id":2,
"tk_title":"送免费道具",
"tk_des":"第一次送免费道具",
"icon":"appreciatefill",
"plan":1,
"time_cycle":0,
"sort":1,
"award_type":1,
"tl_id":"",
"tl_plan":"",
"tl_award":"",
"status":""
 */
public class TaskBean {
    private String task_id;
    private String tk_title;
    private String tk_des;
    private String icon;
    private int plan;
    private int time_cycle;
    private int sort;
    private int award_type;
    private String tl_id;
    private String tl_plan;
    private String tl_award;
    private String status;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTk_title() {
        return tk_title;
    }

    public void setTk_title(String tk_title) {
        this.tk_title = tk_title;
    }

    public String getTk_des() {
        return tk_des;
    }

    public void setTk_des(String tk_des) {
        this.tk_des = tk_des;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getTime_cycle() {
        return time_cycle;
    }

    public void setTime_cycle(int time_cycle) {
        this.time_cycle = time_cycle;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getAward_type() {
        return award_type;
    }

    public void setAward_type(int award_type) {
        this.award_type = award_type;
    }

    public String getTl_id() {
        return tl_id;
    }

    public void setTl_id(String tl_id) {
        this.tl_id = tl_id;
    }

    public String getTl_plan() {
        return tl_plan;
    }

    public void setTl_plan(String tl_plan) {
        this.tl_plan = tl_plan;
    }

    public String getTl_award() {
        return tl_award;
    }

    public void setTl_award(String tl_award) {
        this.tl_award = tl_award;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
