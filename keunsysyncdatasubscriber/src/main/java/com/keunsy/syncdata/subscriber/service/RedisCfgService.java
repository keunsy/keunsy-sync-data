package com.keunsy.syncdata.subscriber.service;


import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.redis.RedisDcAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

@Service
public class RedisCfgService extends BasicService {

    private final Log log = CommonLogFactory.getLog("infoLog");

    @Resource
    private RedisDcAdapter redisSource;

    /** 
     * 添加配置到redis中
     * @method initConfigMapToRedis         
     * @return boolean 
     */
    public boolean initConfigMapToRedis() {
        //存入redis
        log.info("initConfigMapToRedis is Called!");
        return redisSource.addHashStringElements(RedisCfgCenter.SYNC_CONFIG_HASH_KEY, RedisCfgCenter.getConfigMap());
    }

    /**
     * 更新redis以及map数据
     * @method updateConfigAndRedis         
     * @return boolean
     */
    public boolean updateConfigAndRedis(String key, String field, String value) {

        boolean result = redisSource.updateHashStringElement(key, field, value);
        RedisCfgCenter.getConfigMap().put(field, value);
        return result;
    }

    /** 
     * 从redis中更新数据到map
     * @method uptConfigMapFromRedis         
     * @return void 
     */
    public void uptConfigMapFromRedis() {

        Map<String, String> map = redisSource.getALLHashStringElements(RedisCfgCenter.SYNC_CONFIG_HASH_KEY);
        if (map != null && map.size() > 0) {
            //防止redis中的配置被置为空
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && !"".equals(value) && RedisCfgCenter.getConfigMap().containsKey(key)) {
                    RedisCfgCenter.getConfigMap().put(key, value);
                }
            }
            log.info("ConfigMap Is Changed!");
        } else {
            log.info("Config From Redis Is Empty!");
        }
    }

    /** 
     * 获取redis刷新配置间隔时间
     * @method getReloadIntervalTime         
     * @return void 
     */
    public Long getReloadIntervalTime() {

        String reload_interval_key = RedisCfgCenter.RELOAD_INTERVAL_KEY;
        String reloadTime = RedisCfgCenter.getConfigMap().get(reload_interval_key);
        if (StringUtils.isBlank(reloadTime)) {
            reloadTime = RedisCfgCenter.RELOAD_INTERVAL;
        }
        return Long.valueOf(reloadTime);
    }

}
