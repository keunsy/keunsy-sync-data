package com.keunsy.syncdata.entity;

import java.io.Serializable;

public class UserServiceInfo implements Serializable {

    private static final long serialVersionUID = 7023675724894389074L;

    private int sn; //
    private String user_id; //用户表id
    private String td_code; //'通道代码',
    private String service_code; //业务代码用于http接口
    private String ext_code; //扩展号
    private double price; //业务通道单价
    private String type; //global' COMMENT '分流类型，包括全局使用，按特定号段使用
    private String td_type; //通道类型 1移动 2联通 3电信 4国际业务
    private int priority; //优先级，主用通道=0，地区通道=-1，号段和比例分流业务为-2，备用通道依照次序从1开始递增，通道级备用通道为100
    private int status; //使用状态 0=开启，1=关闭（如果某通道挂了，直接根据通知来关闭该状态即可，恢复反之）
    private int level; //通道策略(级别) 主用、号段分流=0，比例分流=-2，账户级备用=1，通道机备用=2
    private String insert_time; //记录插入时间
    private String update_time; //修改时间
    private String operator; //操作人
    private int ratio; //分流比例

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTd_code() {
        return td_code;
    }

    public void setTd_code(String td_code) {
        this.td_code = td_code;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getExt_code() {
        return ext_code;
    }

    public void setExt_code(String ext_code) {
        this.ext_code = ext_code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTd_type() {
        return td_type;
    }

    public void setTd_type(String td_type) {
        this.td_type = td_type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

}
