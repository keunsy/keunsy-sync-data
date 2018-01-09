package com.keunsy.syncdata.subscriber.entity;

import java.io.Serializable;

public class TdSignInfo implements Serializable {

    private static final long serialVersionUID = 5211179271807449099L;

    private int sn;//'表主键'
    private String td_code;//'通道代码'
    private String ext_code;//'通道扩展号码，父通道为空串'
    private int is_ext;//'是否为通道扩展[0=否 1=是]'
    private String sign_chs;//'中文签名'
    private String sign_eng;//'英文签名'
    private String update_time;//
    private String operator;//'操作人'
    private String insert_time;//'插入时间'
    private String charge_term_id;//'计费代码与扩展号唯一对应'

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getTd_code() {
        return td_code;
    }

    public void setTd_code(String td_code) {
        this.td_code = td_code;
    }

    public String getExt_code() {
        return ext_code;
    }

    public void setExt_code(String ext_code) {
        this.ext_code = ext_code;
    }

    public int getIs_ext() {
        return is_ext;
    }

    public void setIs_ext(int is_ext) {
        this.is_ext = is_ext;
    }

    public String getSign_chs() {
        return sign_chs;
    }

    public void setSign_chs(String sign_chs) {
        this.sign_chs = sign_chs;
    }

    public String getSign_eng() {
        return sign_eng;
    }

    public void setSign_eng(String sign_eng) {
        this.sign_eng = sign_eng;
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

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getCharge_term_id() {
        return charge_term_id;
    }

    public void setCharge_term_id(String charge_term_id) {
        this.charge_term_id = charge_term_id;
    }

}
