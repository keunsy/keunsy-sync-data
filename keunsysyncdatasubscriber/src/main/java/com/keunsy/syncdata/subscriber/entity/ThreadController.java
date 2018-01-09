package com.keunsy.syncdata.subscriber.entity;

import java.io.Serializable;

public class ThreadController implements Serializable {
    private static final long serialVersionUID = -4804889793242127951L;

    private int sn;
    private String server_ip;//线程在哪台服务器上启动 
    private String thread_name;//线程名 
    private int action;// 0启动  1关闭 
    private int status;// 0未操作  1操作完成 
    private String thread_param;//参数信息 
    private int thread_type;// 0：开启，1：关闭 
    private String group_id;// 通道代码 
    private String app_name;// 应用程序 
    private String update_time;//

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getThread_name() {
        return thread_name;
    }

    public void setThread_name(String thread_name) {
        this.thread_name = thread_name;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThread_param() {
        return thread_param;
    }

    public void setThread_param(String thread_param) {
        this.thread_param = thread_param;
    }

    public int getThread_type() {
        return thread_type;
    }

    public void setThread_type(int thread_type) {
        this.thread_type = thread_type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}
