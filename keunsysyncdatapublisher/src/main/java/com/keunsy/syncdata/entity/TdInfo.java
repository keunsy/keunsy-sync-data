package com.keunsy.syncdata.entity;

import java.io.Serializable;

public class TdInfo implements Serializable {

    private static final long serialVersionUID = -1131873321731692735L;

    private int sn;
    private String td_name;//'通道名字'
    private String td_code;//'通道代码'
    private int status;//'0开启1关闭'
    private int td_type;//'1移动 2联通  3电信  4国际短信'
    private String td_sp_number;//'通道原始接入号'
    private int filter_flag;//'通道审核级别'
    private int submit_type;//'提交方式 1：拆分提价2:仅支持整条提交3:全部'
    private int msg_count_cn;//'普通短信中文计费字数'
    private int long_charge_count_cn;//'长短信最后一条中文计费字数'
    private int long_charge_count_pre_cn;//'长短信非最后一条中文计费字数'
    private int msg_count_en;//'普通短信英文计费字数'
    private int long_charge_count_en;//'长短信最后一条英文计费字数'
    private int long_charge_count_pre_en;//'长短信非最后一条英文计费字数'
    private int msg_count_all_cn;//'长短信整条提交中文计费字数'
    private int msg_count_all_en;//'长短信整条提交英文计费字数'
    private int with_gate_sign;//'是否是运营商加签名，0：平台加，1：运营商加'
    private int sign_type;//'签名类型 1=前置 2=后置'
    private String reserve_td_code;//'备用通道代码'
    private String update_time;//

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getTd_name() {
        return td_name;
    }

    public void setTd_name(String td_name) {
        this.td_name = td_name;
    }

    public String getTd_code() {
        return td_code;
    }

    public void setTd_code(String td_code) {
        this.td_code = td_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTd_type() {
        return td_type;
    }

    public void setTd_type(int td_type) {
        this.td_type = td_type;
    }

    public String getTd_sp_number() {
        return td_sp_number;
    }

    public void setTd_sp_number(String td_sp_number) {
        this.td_sp_number = td_sp_number;
    }

    public int getFilter_flag() {
        return filter_flag;
    }

    public void setFilter_flag(int filter_flag) {
        this.filter_flag = filter_flag;
    }

    public int getSubmit_type() {
        return submit_type;
    }

    public void setSubmit_type(int submit_type) {
        this.submit_type = submit_type;
    }

    public int getMsg_count_cn() {
        return msg_count_cn;
    }

    public void setMsg_count_cn(int msg_count_cn) {
        this.msg_count_cn = msg_count_cn;
    }

    public int getLong_charge_count_cn() {
        return long_charge_count_cn;
    }

    public void setLong_charge_count_cn(int long_charge_count_cn) {
        this.long_charge_count_cn = long_charge_count_cn;
    }

    public int getLong_charge_count_pre_cn() {
        return long_charge_count_pre_cn;
    }

    public void setLong_charge_count_pre_cn(int long_charge_count_pre_cn) {
        this.long_charge_count_pre_cn = long_charge_count_pre_cn;
    }

    public int getMsg_count_en() {
        return msg_count_en;
    }

    public void setMsg_count_en(int msg_count_en) {
        this.msg_count_en = msg_count_en;
    }

    public int getLong_charge_count_en() {
        return long_charge_count_en;
    }

    public void setLong_charge_count_en(int long_charge_count_en) {
        this.long_charge_count_en = long_charge_count_en;
    }

    public int getLong_charge_count_pre_en() {
        return long_charge_count_pre_en;
    }

    public void setLong_charge_count_pre_en(int long_charge_count_pre_en) {
        this.long_charge_count_pre_en = long_charge_count_pre_en;
    }

    public int getMsg_count_all_cn() {
        return msg_count_all_cn;
    }

    public void setMsg_count_all_cn(int msg_count_all_cn) {
        this.msg_count_all_cn = msg_count_all_cn;
    }

    public int getMsg_count_all_en() {
        return msg_count_all_en;
    }

    public void setMsg_count_all_en(int msg_count_all_en) {
        this.msg_count_all_en = msg_count_all_en;
    }

    public int getWith_gate_sign() {
        return with_gate_sign;
    }

    public void setWith_gate_sign(int with_gate_sign) {
        this.with_gate_sign = with_gate_sign;
    }

    public int getSign_type() {
        return sign_type;
    }

    public void setSign_type(int sign_type) {
        this.sign_type = sign_type;
    }

    public String getReserve_td_code() {
        return reserve_td_code;
    }

    public void setReserve_td_code(String reserve_td_code) {
        this.reserve_td_code = reserve_td_code;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}
