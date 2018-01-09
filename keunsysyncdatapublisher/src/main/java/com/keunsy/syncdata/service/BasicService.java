package com.keunsy.syncdata.service;

import com.keunsy.syncdata.dao.BasicDAO;
import com.keunsy.syncdata.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.redis.RedisDcAdapter;
import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.common.utils.DateUtil;
import com.keunsy.syncdata.common.utils.SeparatorUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * service基类
 */
@Service
public class BasicService {

    protected Log log = CommonLogFactory.getLog("infoLog");

    @Resource
    protected BasicDAO basicDAO;
    @Resource
    private RedisDcAdapter redisSource;
    @Resource
    private RedisCfgService redisCfgService;

    /** 
     * 读取信息
     * @method readInfoList
     * @return List<T>
     */
    public List<Map<String, Object>> readInfoList(String key_post, String sql) {

        List<Map<String, Object>> list = null;
        String isReadAll_key = RedisCfgCenter.IS_READ_ALL_KEY_PRE + key_post;
        String updateTime_key = RedisCfgCenter.UPDATE_TIME_KEY_PRE + key_post;

        String isReadAll = RedisCfgCenter.getConfigMap().get(isReadAll_key);
        String update_time_now = DateUtil.getDateStr24();//读取前 获取当期时间
        // 读取
        if (isReadAll != null && isReadAll.equals("1")) {//全部读取
            list = basicDAO.queryMapData(sql);
            //只操作一次  更新redis及map
            redisCfgService.updateConfigAndRedis(RedisCfgCenter.SYNC_CONFIG_HASH_KEY, isReadAll_key, "-1");
        } else {
            String update_time = dealUpdateTime(updateTime_key);
            //限定上限时间 避免未来较大时间造成 多次读取
            sql += "  where  update_time >= '" + update_time + "' and update_time <= '" + update_time_now + "'";
            list = basicDAO.queryMapData(sql);
        }
        //读取后更新读取时间
        redisCfgService.updateConfigAndRedis(RedisCfgCenter.SYNC_CONFIG_HASH_KEY, updateTime_key, update_time_now);

        return list;
    }

    /** 
     * 单添加到 redis 队列
     * @param <T>
     * @method addRedisQueue         
     * @return boolean 
     */
    public <T> boolean addToRedisQueue(T t, String key_post) {

        boolean result = false;
        //获取同步服务器信息
        String[] server_array = dealSyncServer(RedisCfgCenter.SYNC_SERVER_KEY_PRE + key_post);
        String queueName = RedisCfgCenter.QUEUE_SYNC_KEY_PRE + key_post;//队列前缀    e:  q:sync:UserInfo
        //有多少个服务器则添加多少个队列
        for (String server : server_array) {
            result = redisSource.addQueueElement(queueName + SeparatorUtil.COLON + server, t);
        }

        return result;
    }

    /**
     * list到redis 队列
     * @method addToRedisQueue         
     * @return boolean
     */
    public <T> boolean addToRedisQueue(List<T> list, String key_post) {

        boolean result = false;
        //获取同步服务器信息
        String[] server_array = dealSyncServer(RedisCfgCenter.SYNC_SERVER_KEY_PRE + key_post);
        String queueName = RedisCfgCenter.QUEUE_SYNC_KEY_PRE + key_post;//队列前缀    e:  q:sync:UserInfo
        //有多少个服务器则添加多少个队列
        for (String server : server_array) {
            result = redisSource.addQueueElements(queueName + SeparatorUtil.COLON + server, list);
        }

        return result;
    }

    /** 
     * 处理同步服务器
     * @method dealSyncServer         
     * @return String[] 
     */
    protected String[] dealSyncServer(String syncServer_key) {

        String[] server_array = null;
        //获取同步服务器
        String sync_server = RedisCfgCenter.getConfigMap().get(syncServer_key);
        if (sync_server != null && !"".equals(sync_server)) {
            server_array = sync_server.split(SeparatorUtil.COMMA);
        } else {
            server_array = new String[] { "cluster" };//默认只传输给集群
        }
        return server_array;
    }

    /** 
     * 处理睡眠时间
     * @method dealSleepTime         
     * @return void 
     */
    public Long getIntervalTime(String key_post) {

        String readInterval_key = RedisCfgCenter.READ_INTERVAL_KEY_PRE + key_post;
        String sleepTime = RedisCfgCenter.getConfigMap().get(readInterval_key);
        if (StringUtils.isBlank(sleepTime)) {
            sleepTime = RedisCfgCenter.READ_INTERVAL;
        }
        return Long.valueOf(sleepTime);
    }

    /** 
     * 更新时间处理
     * @method dealUpdateTime         
     * @return String 
     */
    private String dealUpdateTime(String updateTime_key) {

        String update_time = RedisCfgCenter.getConfigMap().get(updateTime_key);
        //时间为空或不合法  (未来时间是否校验？)
        if (StringUtils.isBlank(update_time) || !DateUtil.isLegalDate(update_time, "yyyy-MM-dd HH:mm:ss")) {
            update_time = DateUtil.getDateStrDay() + " 00:00:00";
        }
        return update_time;
    }

    /** 
     * @method createSelectSql         
     * @return String 
    */
    public String createSelectSql(String tableName, String column) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select ").append(StringUtils.isNotBlank(column) ? column : " * ").append(" from ")
                .append(tableName);
        return sBuffer.toString();
    }

}
