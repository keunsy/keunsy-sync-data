package com.keunsy.syncdata.service;

import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.redis.RedisDcAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

@Service
public class RedisCfgService extends BasicService {

  private final Log log = CommonLogFactory.getLog("infoLog");

  @Resource
  private RedisDcAdapter redisSource;

  /**
   * 添加配置到redis中
   *
   * @return boolean
   * @method initConfigMapToRedis
   */
  public boolean initConfigMapToRedis() {

    //获取上次更新时间
    uptTimeBeforeInit();
    //存入redis
    log.info("initConfigMapToRedis is Called!");
    return redisSource.addHashStringElements(RedisCfgCenter.SYNC_CONFIG_HASH_KEY, RedisCfgCenter.getConfigMap());
  }

  /**
   * 更新redis以及map数据
   *
   * @return boolean
   * @method updateConfigAndRedis
   */
  public boolean updateConfigAndRedis(String key, String field, String value) {

    boolean result = redisSource.updateHashStringElement(key, field, value);
    RedisCfgCenter.getConfigMap().put(field, value);
    return result;
  }

  /**
   * 从redis中更新数据到map
   *
   * @return void
   * @method uptConfigMapFromRedis
   */
  public void uptConfigMapFromRedis() {

    Map<String, String> map = redisSource.getALLHashStringElements(RedisCfgCenter.SYNC_CONFIG_HASH_KEY);
    if (map != null && map.size() > 0) {
      //防止redis中的配置被置为空导致map变空
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
   * 初始化到redis前更新时间值，以便记录上次更新时间
   *
   * @return void
   * @method uptTimeBeforeInit
   */
  private void uptTimeBeforeInit() {
    //读取redis 时间配置 ，更新到map中
    Map<String, String> redisMap = redisSource.getALLHashStringElements(RedisCfgCenter.SYNC_CONFIG_HASH_KEY);
    if (redisMap != null && redisMap.size() > 0) {
      Set<String> set = redisMap.keySet();
      for (String keyStr : set) {
        //包含更新时间前缀的值 进行configMap更新
        if (keyStr.contains(RedisCfgCenter.UPDATE_TIME_KEY_PRE) && redisMap.get(keyStr) != null
                && !redisMap.get(keyStr).equals("")) {
          RedisCfgCenter.getConfigMap().put(keyStr, redisMap.get(keyStr));
        }
      }
    }
  }

  /**
   * 获取redis刷新配置间隔时间
   *
   * @return void
   * @method getReloadIntervalTime
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
