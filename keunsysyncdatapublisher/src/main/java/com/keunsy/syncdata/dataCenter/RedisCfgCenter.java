/**    
 * 文件名：RedisConfigCenter.java    
 *    
 * 版本信息：    
 * 日期：2015-9-24    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.dataCenter;

import com.keunsy.syncdata.common.utils.DateUtil;
import com.keunsy.syncdata.entity.SignInfo;
import com.keunsy.syncdata.entity.TdInfo;
import com.keunsy.syncdata.entity.TdSignInfo;
import com.keunsy.syncdata.entity.ThreadController;
import com.keunsy.syncdata.entity.UserBalanceInfo;
import com.keunsy.syncdata.entity.UserCheckType;
import com.keunsy.syncdata.entity.UserCountryPhoneCodePrice;
import com.keunsy.syncdata.entity.UserInfo;
import com.keunsy.syncdata.entity.UserServiceInfo;
import com.keunsy.syncdata.entity.UserServiceParam;
import com.keunsy.syncdata.service.RedisCfgService;

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
    public final static String SYNC_CONFIG_HASH_KEY = "h:sync:publisher:cfg";//配置信息 key名称

    public final static String IS_READ_ALL_KEY_PRE = "isReadAll_";
    public final static String READ_INTERVAL_KEY_PRE = "readInterval_";
    public final static String SYNC_SERVER_KEY_PRE = "syncServer_";//需要同步的节点key
    public final static String UPDATE_TIME_KEY_PRE = "updateTime_";

    public final static String IS_READALL = "-1";//是否读取全部 -1：否；1是 
    public final static String READ_INTERVAL = "180000";//读取数据时间间隔 默认3分钟
    public final static String UPDATE_TIME = DateUtil.getDateStr24();//读取
    public static String SYNC_SERVER;//需要同步的节点,多个以逗号隔开

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
    public final static String DEL_INTERVAL_TIME = "90000";//删除数据线程执行时间，根据实际情况设定值   避免添加的数据与原有删除冲突
    public final static String DEL_INTERVAL_KEY = "del_interval_key";//线程执行间隔时间
    public final static String DEL_OBJECT_KEY_PRE = "del_object_key_";//删除对象hash  field 前缀

    /*
     * 是否需要重加载所有数据
     */
    public final static String IS_NEED_RELOAD_ALL = "-1";//是否执行更新所有数据，默认不开启，观察线上情况  -1：否  1是
    public final static String IS_NEED_RELOAD_ALL_KEY = "is_need_reload_all_key";//KEY
    public final static String RELOAD_ALL_DATA_INTERVAL = "3600000";//线程执行间隔时间 一个小时
    public final static String RELOAD_ALL_DATA_INTERVAL_KEY = "reload_all_data_interval_key";//key

    public static Map<String, String> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, String> configMap) {
        RedisCfgCenter.configMap = configMap;
    }

    /**
     * 初始化配置
     * @method initCfg         
     * @return void
     */
    void initCfg() {

        initDelTableMap();
        //
        initCfgMap();
        //加载配置到redis中
        redisCfgService.initConfigMapToRedis();
    }

    private void initCfgMap() {
        //加载后缀(需要同步的对象)
        modelList.add(KEY_POST_USERINFO);
        modelList.add(KEY_POST_SIGNINFO);
        modelList.add(KEY_POST_TDINFO);
        modelList.add(KEY_POST_TDSIGNINFO);
        modelList.add(KEY_POST_THREADCONTROLLER);
        modelList.add(KEY_POST_USERCHECKTYPE);
        modelList.add(KEY_POST_USERCOUNTRYPHONECODEPRICE);
        modelList.add(KEY_POST_USERSERVICEINFO);
        modelList.add(KEY_POST_USERSERVICEPARAM);

        //组装各对象对应配置值
        for (String key_post : modelList) {
            configMap.put(IS_READ_ALL_KEY_PRE + key_post, IS_READALL);
            configMap.put(READ_INTERVAL_KEY_PRE + key_post, READ_INTERVAL);
            configMap.put(SYNC_SERVER_KEY_PRE + key_post, SYNC_SERVER);
            configMap.put(UPDATE_TIME_KEY_PRE + key_post, UPDATE_TIME);
        }
        //刷新redis配置
        configMap.put(RELOAD_INTERVAL_KEY, RELOAD_INTERVAL);
        //删除sn读取操作
        configMap.put(READ_INTERVAL_KEY_PRE + DEL_INTERVAL_KEY, DEL_INTERVAL_TIME);
        //读取全部数据操作
        configMap.put(IS_NEED_RELOAD_ALL_KEY, IS_NEED_RELOAD_ALL);
        configMap.put(READ_INTERVAL_KEY_PRE + RELOAD_ALL_DATA_INTERVAL_KEY, RELOAD_ALL_DATA_INTERVAL);
    }

    /**
     * 初始化对象对应表
     * @method initTableMap         
     * @return void
     */
    private void initDelTableMap() {

        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_SIGNINFO, " local_cluster_sign_info");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_TDSIGNINFO, " td_sign_info");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_USERCHECKTYPE, " local_cluster_user_check_type");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_USERCOUNTRYPHONECODEPRICE, " user_country_phone_code_price");
        configMap.put(DEL_OBJECT_KEY_PRE + KEY_POST_USERSERVICEINFO, " local_cluster_user_service_info");
    }

}
