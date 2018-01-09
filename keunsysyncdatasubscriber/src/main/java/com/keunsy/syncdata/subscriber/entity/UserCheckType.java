package com.keunsy.syncdata.subscriber.entity;

import java.io.Serializable;

public class UserCheckType implements Serializable {

    private static final long serialVersionUID = -8262597312066814329L;
    private int sn; //主键
    private int user_sn; //用户主键
    private String user_id; //用户ID,登陆用户名
    private String user_name; //用户名称
    private String td_code; //通道业务代码
    private int priority; //用户等级,0为免审,1为关键词,-1为全审
    private String mode; //-1:子账户全部拦截，0:通道白名单，1:黑名单，2：基础关键词，3：信审缓存 4：人工审核关键词，5：全审，6：群发监控，7:号段拦截
    private String fast_mode; //报备内容的快捷审核模式
    private int type; //0:主账号,1:子号扩展
    private String service_code; //服务器代码，作为审核类型的选择条件
    private String update_time;

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public int getUser_sn() {
        return user_sn;
    }

    public void setUser_sn(int user_sn) {
        this.user_sn = user_sn;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTd_code() {
        return td_code;
    }

    public void setTd_code(String td_code) {
        this.td_code = td_code;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFast_mode() {
        return fast_mode;
    }

    public void setFast_mode(String fast_mode) {
        this.fast_mode = fast_mode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}
