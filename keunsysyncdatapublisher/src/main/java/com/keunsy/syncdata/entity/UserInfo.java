package com.keunsy.syncdata.entity;

import java.io.Serializable;

/**
 * 账户表信息
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -765058881549517720L;

    private int sn; //
    private String user_id; //用户唯一标示
    private String user_pwd; //用户密码
    private String user_name; //用户名称,展示给管理人员
    private int user_type; //计费类型, 0:预付费，1：后付费, 暂定默认为预付费
    private int charge_type; //计费方式, 0:成功计费，1：提交计费
    private String gate_type; //接口类型cmpp，sgip,smgp,smpp,http
    private String insert_time; //业务开设日期
    private String update_time; //业务最后更改日期
    private int status; //账户状态, 0:开启  1：关闭, 暂定默认为开启
    private int deliver_type; //0:不需要,1：用户自取,2：平台推送
    private int report_type; //0:不需要,1：用户自取,2：平台推送
    private String user_ip;//账户IP
    private int is_filter_repeat; //0:不过滤，1：过滤
    private int filter_cycle; //间隔时间
    private int repeat_times; //重复次数上限
    private int max_mass_num; //账户定制的最大群发数
    private int deliver_version; //0：推送格式老版本，1：推送格式新版本
    private int is_switch_td; //是否允许切换到通道级备用通道 [0_允许 1_不允许]
    private int is_net_switch; //是否使用携号转网功能 0：是，1：否
    private int is_direct; //是否宣称直连 0:宣称直连 1:非直连

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

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(int charge_type) {
        this.charge_type = charge_type;
    }

    public String getGate_type() {
        return gate_type;
    }

    public void setGate_type(String gate_type) {
        this.gate_type = gate_type;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeliver_type() {
        return deliver_type;
    }

    public void setDeliver_type(int deliver_type) {
        this.deliver_type = deliver_type;
    }

    public int getReport_type() {
        return report_type;
    }

    public void setReport_type(int report_type) {
        this.report_type = report_type;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public int getIs_filter_repeat() {
        return is_filter_repeat;
    }

    public void setIs_filter_repeat(int is_filter_repeat) {
        this.is_filter_repeat = is_filter_repeat;
    }

    public int getFilter_cycle() {
        return filter_cycle;
    }

    public void setFilter_cycle(int filter_cycle) {
        this.filter_cycle = filter_cycle;
    }

    public int getRepeat_times() {
        return repeat_times;
    }

    public void setRepeat_times(int repeat_times) {
        this.repeat_times = repeat_times;
    }

    public int getMax_mass_num() {
        return max_mass_num;
    }

    public void setMax_mass_num(int max_mass_num) {
        this.max_mass_num = max_mass_num;
    }

    public int getDeliver_version() {
        return deliver_version;
    }

    public void setDeliver_version(int deliver_version) {
        this.deliver_version = deliver_version;
    }

    public int getIs_switch_td() {
        return is_switch_td;
    }

    public void setIs_switch_td(int is_switch_td) {
        this.is_switch_td = is_switch_td;
    }

    public int getIs_net_switch() {
        return is_net_switch;
    }

    public void setIs_net_switch(int is_net_switch) {
        this.is_net_switch = is_net_switch;
    }

    public int getIs_direct() {
        return is_direct;
    }

    public void setIs_direct(int is_direct) {
        this.is_direct = is_direct;
    }

}
