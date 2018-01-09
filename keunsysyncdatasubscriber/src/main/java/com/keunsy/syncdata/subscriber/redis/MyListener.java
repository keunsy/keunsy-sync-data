/**    
 * 文件名：MyListener.java    
 *    
 * 版本信息：    
 * 日期：2015-9-21    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.subscriber.redis;

import com.keunsy.syncdata.common.utils.SeriUtil;
import com.keunsy.syncdata.subscriber.entity.TestUserInfo;

import java.io.IOException;

import redis.clients.jedis.JedisPubSub;


/**    
 *     
 * 项目名称：dcAdapterProject_sentinel    
 * 类名称：MyListener    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-21 下午4:33:17    
 * 修改人：chenrong1    
 * 修改时间：2015-9-21 下午4:33:17    
 * 修改备注：    
 * @version     
 *
 */
public class MyListener extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {

        TestUserInfo userInfo = null;
        try {
            userInfo = (TestUserInfo) SeriUtil.toObject(message, "ISO-8859-1");
            System.out.println("=========================");
            System.out.println(userInfo.getName());
            System.out.println("=========================");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
