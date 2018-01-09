package com.keunsy.syncdata.entity;

import java.io.Serializable;

public class UserBalanceInfo implements Serializable {

    private static final long serialVersionUID = 5304342713778151915L;

    private int sn;//'主键自增'
    private String user_id;//'所属用户id'
    private double user_balance;//'余额'

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

    public double getUser_balance() {
        return user_balance;
    }

    public void setUser_balance(double user_balance) {
        this.user_balance = user_balance;
    }

}
