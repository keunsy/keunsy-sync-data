/**    
 * 文件名：RedisConfigCenter.java    
 *    
 * 版本信息：    
 * 日期：2015-9-24    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.subscriber.dataCenter;

import com.keunsy.syncdata.subscriber.entity.SignInfo;
import com.keunsy.syncdata.subscriber.entity.TdInfo;
import com.keunsy.syncdata.subscriber.entity.TdSignInfo;
import com.keunsy.syncdata.subscriber.entity.ThreadController;
import com.keunsy.syncdata.subscriber.entity.UserBalanceInfo;
import com.keunsy.syncdata.subscriber.entity.UserCheckType;
import com.keunsy.syncdata.subscriber.entity.UserCountryPhoneCodePrice;
import com.keunsy.syncdata.subscriber.entity.UserInfo;
import com.keunsy.syncdata.subscriber.entity.UserServiceInfo;
import com.keunsy.syncdata.subscriber.entity.UserServiceParam;
import com.keunsy.syncdata.subscriber.service.RedisCfgService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**    
 *     
 * 项目名称：syncPlateDataTool    
 * 类名称：RedisConfigCenter    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-24 上午10:06:28    
 * 修改人：chenrong1    
 * 修改时间：2015-9-24 上午10:06:28    
 * 修改备注：    
 * @version     
 *     
 */
public class RedisCfgCenter {

    @Resource
    private RedisCfgService redisCfgService;

    /*
     * 配置信息
     */
    private static Map<String, String> configMap = new LinkedHashMap<String, String>();//配置信息
    public static List<String> modelList = new ArrayList<String>();//对象名称列表
    public final static String SYNC_CONFIG_HASH_KEY = "h:sync:subscriber:cfg";//配置信息 key名称

    public final static String READ_COUNT_KEY_PRE = "readCount_";//读取间隔
    public final static String READ_INTERVAL_KEY_PRE = "readInterval_";//读取间隔
    public final static String SYNC_SERVER_KEY_PRE = "syncServer_";//需要同步的节点key

    public final static String READ_COUNT = "500";//读取redis 队列数据数量
    public final static String READ_INTERVAL = "180000";//读取数据时间间隔 默认3分钟

    public static String SYNC_SERVER;//需要同步的节点,多个以逗号隔开,订阅端只要一个

    //便于注入
    public void setSYNC_SERVER(String sYNC_SERVER) {
        SYNC_SERVER = sYNC_SERVER;
    }

    /*
     * redis刷新map配置
     */
    public final static String RELOAD_INTERVAL = "60000";//重加载redis配置时间 默认1分钟，时间短些，防止被覆盖导致配置不生效
    public final static String RELOAD_INTERVAL_KEY = "reload_interval_key";//重加载redis配置时间 默认1分钟，时间短些，防止被覆盖导致配置不生效

    /*
     * key后缀
     */
    public final static String KEY_POST_USERINFO = UserInfo.class.getSimpleName();
    public final static String KEY_POST_SIGNINFO = SignInfo.class.getSimpleName();
    public final static String KEY_POST_TDINFO = TdInfo.class.getSimpleName();
    public final static String KEY_POST_TDSIGNINFO = TdSignInfo.class.getSimpleName();
    public final static String KEY_POST_THREADCONTROLLER = ThreadController.class.getSimpleName();
    public final static String KEY_POST_USERBALANCEINFO = UserBalanceInfo.class.getSimpleName();
    public final static String KEY_POST_USERCHECKTYPE = UserCheckType.class.getSimpleName();
    public final static String KEY_POST_USERCOUNTRYPHONECODEPRICE = UserCountryPhoneCodePrice.class.getSimpleName();
    public final static String KEY_POST_USERSERVICEINFO = UserServiceInfo.class.getSimpleName();
    public final static String KEY_POST_USERSERVICEPARAM = UserServiceParam.class.getSimpleName();
    /*
     * redis 数据队列key 最终形式如：q:sync:UserInfo:cmpp_75
     */
    public final static String QUEUE_SYNC_KEY_PRE = "q:sync:";

    /*
     * 对象删除映射表,解决部分数据需要删除同步的问题
     */
    public final static String SYNC_DEL_SN_KEY = "h:sync:del:sn";//sn串数据 key
    public final static String DEL_INTERVAL_TIME = "360000";//删除数据线程执行时间，尽可能的长 避免添加的数据与原有冲突
    public final static String DEL_INTERVAL_KEY = "del_interval_key";//线程执行间隔时间
    public final static String DEL_OBJECT_KEY_PRE = "del_object_key_";//删除对象hash  field 前缀

    public static Map<String, String> getConfigMap() {
        return configMap;
    }

    public static void setConfigMap(Map<String, String> configMap) {
        RedisCfgCenter.configMap = configMap;
    }

    /**
     * 初始化配置
     * @method initCfg         
     * @return void
     */
    void initCfg() {

        //initSyncServer();
        initDelTableMap();
        initCfgMap();
        //加载配置到redis中
        redisCfgService.initConfigMapToRedis();
    }

    /** 
     * 初始化同步数据库名称(暂不用)
     * @method initSyncServer         
     * @return void 
     */
    //    private void initSyncServer() {
    //        try {
    //            /*
    //             * 获取数据库名称
    //             */
    //            int beginIndex = SYNC_SERVER.lastIndexOf("/");
    //            int endIndex = SYNC_SERVER.lastIndexOf("?") == -1 ? SYNC_SERVER.length() : SYNC_SERVER.lastIndexOf("?");
    //            SYNC_SERVER = SYNC_SERVER.substring(beginIndex + 1, endIndex);
    //
    //            if (SYNC_SERVER.equals("")) {
    //                SYNC_SERVER = "cluster";
    //            }
    //            //去除数据库名后缀 如：cluster_server 去除后 变成：cluster
    //            SYNC_SERVER = SYNC_SERVER.substring(0, SYNC_SERVER.indexOf("_") != -1 ? SYNC_SERVER.indexOf("_")
    //                    : SYNC_SERVER.length());
    //        } catch (Exception e) {
    //            SYNC_SERVER = "cluster";
    //        }
    //    }

    private void initCfgMap() {
        //加载后缀(需要同步的对象)
        modelList.add(KEY_POST_USERINFO);
        modelList.add(KEY_POST_SIGNINFO);
        modelList.add(KEY_POST_TDINFO);
        modelList.add(KEY_POST_TDSIGNINFO);
        modelList.add(KEY_POST_THREADCONTROLLER);
        modelList.add(KEY_POST_USERBALANCEINFO);
        modelList.add(KEY_POST_USERCHECKTYPE);
        modelList.add(KEY_POST_USERCOUNTRYPHONECODEPRICE);
        modelList.add(KEY_POST_USERSERVICEINFO);
        modelList.add(KEY_POST_USERSERVICEPARAM);

        //组装各对象对应配置值
        for (String key_post : modelList) {
            configMap.put(READ_INTERVAL_KEY_PRE + key_post, READ_INTERVAL);
            configMap.put(SYNC_SERVER_KEY_PRE + key_post, SYNC_SERVER);
            configMap.put(READ_COUNT_KEY_PRE + key_post, READ_COUNT);
        }
        //刷新重载redis配置
        configMap.put(RELOAD_INTERVAL_KEY, RELOAD_INTERVAL);
        //删除sn执行操作
        configMap.put(READ_INTERVAL_KEY_PRE + DEL_INTERVAL_KEY, DEL_INTERVAL_TIME);
    }

    /**
     * 初始化对象对应表表
     * @method initTableMap         
     * @return void
     */
    private void initDelTableMap() {

        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_SIGNINFO, " sign_info");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_TDSIGNINFO, " td_sign_info");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_USERCHECKTYPE, " user_check_type");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_USERCOUNTRYPHONECODEPRICE, " user_country_phone_code_price");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_USERSERVICEINFO, " user_service_info");
    }

}
