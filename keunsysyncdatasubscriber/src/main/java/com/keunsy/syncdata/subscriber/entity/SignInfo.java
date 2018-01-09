package com.keunsy.syncdata.subscriber.entity;

import java.io.Serializable;

public class SignInfo implements Serializable {

    private static final long serialVersionUID = -1397999424409162226L;

    private int sn;//
    private String user_id;//'用户主id'
    private String gate_sp_number;//'用户主叫号码'
    private String td_code;//'通道代码'
    private String sign_chs;//'中文签名'
    private String sign_eng;//'英文签名'
    private int status;//'状态(0：开启;1：关闭)'
    private int sign_type;//'签名类型   1:前置   2:后置'
    private int sign_front_strategy;//'签名前置策略  1:第一条加签名  2:最后一条加签名'
    private String insert_time;//'插入时间'
    private String update_time;//'修改时间'
    private String operator;//'操作人'
    private int flag;//'该字段已废弃，不再使用，签名信息在td_info表中维护'
    private String add_chs_msg;//'追加中文内容'
    private String add_eng_msg;//'追加英文内容'
    private int is_add_msg;//'自有网关是否追加内容：0=不追加1=追加'
    private int is_add_msg_use;//'回复退订生不生效 0生效，1不生效'
    private String unsubscribe_msg;//'退订生效内容，多个值以英文隔开'
    private int isExt;//'是否为扩展（0：否；1：是）'

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

    public String getGate_sp_number() {
        return gate_sp_number;
    }

    public void setGate_sp_number(String gate_sp_number) {
        this.gate_sp_number = gate_sp_number;
    }

    public String getTd_code() {
        return td_code;
    }

    public void setTd_code(String td_code) {
        this.td_code = td_code;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSign_type() {
        return sign_type;
    }

    public void setSign_type(int sign_type) {
        this.sign_type = sign_type;
    }

    public int getSign_front_strategy() {
        return sign_front_strategy;
    }

    public void setSign_front_strategy(int sign_front_strategy) {
        this.sign_front_strategy = sign_front_strategy;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getAdd_chs_msg() {
        return add_chs_msg;
    }

    public void setAdd_chs_msg(String add_chs_msg) {
        this.add_chs_msg = add_chs_msg;
    }

    public String getAdd_eng_msg() {
        return add_eng_msg;
    }

    public void setAdd_eng_msg(String add_eng_msg) {
        this.add_eng_msg = add_eng_msg;
    }

    public int getIs_add_msg() {
        return is_add_msg;
    }

    public void setIs_add_msg(int is_add_msg) {
        this.is_add_msg = is_add_msg;
    }

    public int getIs_add_msg_use() {
        return is_add_msg_use;
    }

    public void setIs_add_msg_use(int is_add_msg_use) {
        this.is_add_msg_use = is_add_msg_use;
    }

    public String getUnsubscribe_msg() {
        return unsubscribe_msg;
    }

    public void setUnsubscribe_msg(String unsubscribe_msg) {
        this.unsubscribe_msg = unsubscribe_msg;
    }

    public int getIsExt() {
        return isExt;
    }

    public void setIsExt(int isExt) {
        this.isExt = isExt;
    }

}
