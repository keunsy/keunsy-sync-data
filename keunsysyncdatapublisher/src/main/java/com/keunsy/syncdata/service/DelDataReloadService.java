package com.keunsy.syncdata.service;

import com.keunsy.syncdata.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.redis.RedisDcAdapter;
import com.keunsy.syncdata.common.utils.CommonLogFactory;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class DelDataReloadService extends BasicService {

    private final Log log = CommonLogFactory.getLog("infoLog");

    @Resource
    private RedisDcAdapter redisSource;

    /** 
     * 获取sn map 如  ("UserInfo",snList)
     * @method readSnList         
     * @return Map<String, List<Integer>> 
     */
    public Map<String, List<Integer>> readSnList(String pre_sql) {

        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        //提取删除对象map
        Map<String, String> delTableMap = getDelTableMap();

        if (delTableMap != null && !delTableMap.isEmpty()) {
            //获取表名
            for (Map.Entry<String, String> entry : delTableMap.entrySet()) {
                String tableName = null;
                String sql = null;
                List<Integer> snList = null;
                try {
                    tableName = entry.getValue();
                    //组装sql，排序
                    sql = pre_sql + " " + tableName + " order by sn";
                    //查询
                    snList = basicDAO.querySnList(sql);
                    if (snList != null && snList.size() > 0) {
                        map.put(entry.getKey(), snList);
                    }
                } catch (Exception e) {
                    log.error("Query Sn Error :" + sql);
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    /** 
     * 提取删除对象map
     * @method getDelTableMap         
     * @return Map<String,String> 
     */
    private Map<String, String> getDelTableMap() {

        Map<String, String> delTableMap = new HashMap<String, String>();
        if (RedisCfgCenter.getConfigMap() != null && !RedisCfgCenter.getConfigMap().isEmpty()) {
            for (Map.Entry<String, String> entry : RedisCfgCenter.getConfigMap().entrySet()) {
                String key = entry.getKey();
                if (key != null && key.indexOf(RedisCfgCenter.DEL_OBJECT_KEY_PRE) == 0) {
                    delTableMap.put(key, entry.getValue());
                }
            }
        }

        return delTableMap;
    }

    /** 
     * 添加到redis hash中
     * @method addToRedisHash         
     * @return void 
    */
    public void addToRedisHash(Map<String, List<Integer>> map) {

        if (map != null && !map.isEmpty()) {
            //添加到redis队列中
            boolean result = redisSource.addHashQueueElements(RedisCfgCenter.SYNC_DEL_SN_KEY, map);
            if (result) {
                log.info("addToRedisHash Success!");
            } else {
                log.info("addToRedisHash Fail!");
            }
        }

    }

}
