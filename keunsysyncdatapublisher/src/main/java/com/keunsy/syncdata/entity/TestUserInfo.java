/**    
 * 文件名：UserInfo.java    
 *    
 * 版本信息：    
 * 日期：2015-7-27    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.entity;

import java.io.Serializable;

/**    
 *     
 * 项目名称：dcAdapterProject    
 * 类名称：UserInfo    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-7-27 下午4:38:31    
 * 修改人：chenrong1    
 * 修改时间：2015-7-27 下午4:38:31    
 * 修改备注：    
 * @version     
 *     
 */
public class TestUserInfo implements Serializable {

    private static final long serialVersionUID = 4619083732382023404L;
    private int id;
    private int age;
    private String name;
    private String user_id;
    private double charge_sum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getCharge_sum() {
        return charge_sum;
    }

    public void setCharge_sum(double charge_sum) {
        this.charge_sum = charge_sum;
    }

}
