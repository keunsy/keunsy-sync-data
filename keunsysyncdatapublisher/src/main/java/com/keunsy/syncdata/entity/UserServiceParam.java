/**    
 * 文件名：UserServiceParam.java    
 *    
 * 版本信息：    
 * 日期：2015-9-22    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.entity;

import java.io.Serializable;

public class UserServiceParam implements Serializable {

    private static final long serialVersionUID = -3529186534484704285L;

    private int sn;// '用户业务主键，自动递增'
    private String user_id;//  '用户唯一标示,关联账户表'
    private String param_key; //'参数字段名'
    private String param_value;// '参数字段值'
    private String update_time;// 

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

    public String getParam_key() {
        return param_key;
    }

    public void setParam_key(String param_key) {
        this.param_key = param_key;
    }

    public String getParam_value() {
        return param_value;
    }

    public void setParam_value(String param_value) {
        this.param_value = param_value;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}
