package com.keunsy.syncdata.subscriber.redis;

import com.keunsy.syncdata.common.utils.CommonLogFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashSet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;


/**    
 *     
 * 项目名称：syncPlateDataTool    
 * 类名称：RedisSentialAdapter    
 * 类描述：    redis哨兵适配
 * 创建人：chenrong1    
 * 创建时间：2015-9-22 下午2:40:37    
 * 修改人：chenrong1    
 * 修改时间：2015-9-22 下午2:40:37    
 * 修改备注：    
 * @version     
 *     
 */
public class RedisSentinelAdapter extends RedisDcAdapter {

    private final Log log = CommonLogFactory.getLog("redisLog");

    private String redis_host;//redis连接串，多个";"隔开
    private int redis_time_out;//超时时间
    private String master_name;//主名称
    private HashSet<String> redis_server = new HashSet<String>();//redis sentinel host

    /* (non-Javadoc)    
     * @see com.RedisDcAdapter#getPool()
     */
    @Override
    public Pool<Jedis> getPool() {
        try {
            //销毁已有pool
            if (pool != null) {
                pool.destroy();
                pool = null;
            }
            //属性值设置
            initProperty();
            /*
             * 测试使用
             */
            if (redis_server == null || redis_server.size() < 1) {
                redis_server = new HashSet<String>();
                redis_server.add("192.168.60.102:23456");
                redis_server.add("192.168.60.102:23457");
                redis_server.add("192.168.60.101:23456");
            }
            //初始化连接池
            pool = new JedisSentinelPool(master_name, redis_server, new GenericObjectPoolConfig(), redis_time_out);
            log.info("JedisSentinelPool init success!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis sentinel 连接异常！", e);
        }
        return pool;
    }

    /**
     * 参数设置处理
     * @method initProperty         
     * @return void
     */
    private void initProperty() {
        //添加哨兵host
        if (redis_host != null && !redis_host.equals("")) {
            String[] conn_array = redis_host.split(";");
            for (int i = 0, length = conn_array.length; i < length; i++) {
                redis_server.add(conn_array[i]);
            }
        }
        if (null == master_name || "".equals(master_name)) {
            master_name = "mymaster";
        }
        if (redis_time_out == 0) {
            redis_time_out = 20000;
        }
    }

    public String getRedis_host() {
        return redis_host;
    }

    public void setRedis_host(String redis_host) {
        this.redis_host = redis_host;
    }

    public int getRedis_time_out() {
        return redis_time_out;
    }

    public void setRedis_time_out(int redis_time_out) {
        this.redis_time_out = redis_time_out;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

}
