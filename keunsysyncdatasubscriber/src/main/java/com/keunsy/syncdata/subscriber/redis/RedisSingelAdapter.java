package com.keunsy.syncdata.subscriber.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * 
 *     
 * 项目名称：syncDataPublisher    
 * 类名称：RedisSingelAdapter    
 * 类描述：    单机redis适配器
 * 创建人：chenrong1    
 * 创建时间：2015-10-9 上午10:11:19    
 * 修改人：chenrong1    
 * 修改时间：2015-10-9 上午10:11:19    
 * 修改备注：    
 * @version     
 *
 */
public class RedisSingelAdapter extends RedisDcAdapter {

    private String redisServerIp;//redisip
    private int redisServerPort;//redis端口
    private int maxTotal;//连接池总量
    private int maxIdle;//最大空闲
    private int maxWaitMillis;//最大等待时间
    private boolean testOnBorrow;

    /* (non-Javadoc)    
     * @see com.RedisDcAdapter#getPool()
     */
    @Override
    public Pool<Jedis> getPool() {

        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(maxTotal);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(maxIdle);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(maxWaitMillis);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(testOnBorrow);
            // pool = new JedisPool(config, redisServerIp, redisServerPort);
            pool = new JedisPool(config, redisServerIp, redisServerPort, maxWaitMillis);
            // pool.returnResource(getJedis());
        }
        return pool;
    }

    public String getRedisServerIp() {
        return redisServerIp;
    }

    public void setRedisServerIp(String redisServerIp) {
        this.redisServerIp = redisServerIp;
    }

    public int getRedisServerPort() {
        return redisServerPort;
    }

    public void setRedisServerPort(int redisServerPort) {
        this.redisServerPort = redisServerPort;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

}
