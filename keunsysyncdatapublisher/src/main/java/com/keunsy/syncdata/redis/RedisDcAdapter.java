package com.keunsy.syncdata.redis;

import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.common.utils.GeneralUtil;
import com.keunsy.syncdata.common.utils.SeriUtil;

import org.apache.commons.logging.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.util.Pool;

public abstract class RedisDcAdapter {

    protected Log log = CommonLogFactory.getLog("redisLog");
    protected Pool<Jedis> pool = null;

    public RedisDcAdapter() {
        //initPool();//测试使用
    }

    public void initPool() {
        pool = getPool();
    }

    /**
     * 获取连接池
     * @method getPool         
     * @return JedisSentinelPool
     */
    public abstract Pool<Jedis> getPool();

    public void setPool(Pool<Jedis> pool) {
        this.pool = pool;
    }

    /**
     * 发布更新list
     * @method publishUpdate         
     * @return void
     */
    public <T> void publishUpdate(String channels, List<T> list) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                jedis.publish(channels, SeriUtil.toString(obj, "ISO-8859-1"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 发布更新 单条
     * @method publishUpdate         
     * @return void
     */
    public <T> void publishUpdate(String channel, T t) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, SeriUtil.toString(t, "ISO-8859-1"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 订阅消息
     * @method subscribeUpdate         
     * @return void
     */
    public <T> void subscribeUpdate(String channels, JedisPubSub listener) {
        Jedis jedis = pool.getResource();
        jedis.subscribe(listener, channels);
    }

    /**
     * 单对象添加到队列
     * @method addQueueElement         
     * @return boolean
     */
    public <T> boolean addQueueElement(String queueName, T element) {
        boolean result = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(bos).writeObject(element);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            long r = jedis.lpush(queueName.getBytes(), bos.toByteArray());
            if (r > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 多对象添加到队列
     * @method addQueueElements         
     * @return boolean
     */
    public <T> boolean addQueueElements(String queueName, List<T> elements) {
        boolean result = false;
        if (elements != null && elements.size() > 0) {
            byte[][] total = new byte[elements.size()][];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 0; i < elements.size(); i++) {
                Object obj = elements.get(i);
                try {
                    new ObjectOutputStream(bos).writeObject(obj);
                    byte[] t = bos.toByteArray();
                    bos.flush();
                    bos.reset();
                    total[i] = t;
                } catch (IOException e) {
                    log.error(queueName, e);
                    e.printStackTrace();
                }
            }
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                long r = jedis.lpush(queueName.getBytes(), total);
                if (r > 0) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(queueName, e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
                if (result) {
                    log.info(queueName + " : Success!");
                } else {
                    log.info(queueName + " : Fail!");
                }
            }
        }
        return result;
    }

    /**
     * 从队列中获取单元素对象
     * @method getQueueElement         
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getQueueElement(String queueName) {
        T result = null;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] tmp = jedis.rpop(queueName.getBytes());
            if (tmp != null) {
                try {
                    result = (T) new ObjectInputStream(new ByteArrayInputStream(tmp)).readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 从队列中获取多对象，限定数量(object)
     * @method getQueueElements         
     * @return List<Object>
     */
    public List<Object> getQueueElements(String queueName, int number) {

        List<Object> result = new ArrayList<Object>();
        List<Object> tmp_list = new ArrayList<Object>();

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            long length = jedis.llen(queueName.getBytes());
            if (length > 0) {
                long getNumber = (length > number ? number : length);
                Pipeline pipeline = jedis.pipelined();
                for (int i = 0; i < getNumber; i++) {
                    pipeline.rpop(queueName.getBytes());
                }
                tmp_list = pipeline.syncAndReturnAll();

            }
            for (int i = 0; tmp_list != null && i < tmp_list.size(); i++) {
                if (tmp_list.get(i) != null) {
                    byte[] tmp = (byte[]) tmp_list.get(i);
                    Object obj = SeriUtil.toObject(tmp);
                    if (obj != null) {
                        result.add(obj);
                    }
                }
            }
        } catch (Exception e) {
            log.error("get from " + queueName, e);
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    /**
     * hash添加元素
     * @method addHashQueueElements         
     * @return boolean
     */
    public <T> boolean addHashQueueElements(String queueName, Map<String, T> map) {
        boolean result = false;
        if (queueName != null && map != null && map.size() > 0) {
            Map<byte[], byte[]> tmp = new HashMap<byte[], byte[]>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                try {
                    new ObjectOutputStream(bos).writeObject(map.get(key));
                    byte[] t = bos.toByteArray();
                    bos.flush();
                    bos.reset();
                    tmp.put(key.getBytes(), t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                String r = jedis.hmset(queueName.getBytes(), tmp);
                if (r != null && r.equalsIgnoreCase("OK")) {
                    result = true;
                    log.info(queueName + ":addHashQueueElements Success!");
                } else {
                    log.info(queueName + ":addHashQueueElements Fail!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return result;
    }

    /**
     * hash添加字符串元素（按顺序）
     * @method addHashQueueElements         
     * @return boolean
     */
    public boolean addHashStringElements(String queueName, Map<String, String> map) {
        boolean result = false;
        if (queueName != null && map != null && map.size() > 0) {
            Map<byte[], byte[]> tmp = new LinkedHashMap<byte[], byte[]>();//顺序
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                byte[] t = map.get(key).getBytes();
                tmp.put(key.getBytes(), t);
            }
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                String r = jedis.hmset(queueName.getBytes(), tmp);
                if (r != null && r.equalsIgnoreCase("OK")) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return result;
    }

    /**
     * 获取hash所有元素
     * @method getALLHashElements         
     * @return Map<String,T>
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getALLHashElements(String hashKey) {

        Map<byte[], byte[]> map = null;
        Map<String, T> resultMap = null;
        if (hashKey != null) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                map = jedis.hgetAll(hashKey.getBytes());
                resultMap = new HashMap<String, T>();
                if (map != null && map.size() > 0) {
                    Set<byte[]> set = map.keySet();
                    for (byte[] key : set) {
                        byte[] value = map.get(key);
                        if (value != null) {
                            resultMap.put(new String(key), (T) SeriUtil.toObject(value));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();

                }
            }
        }
        return resultMap;
    }

    /**
     * 获取所有哈希字符元素
     * @method getALLHashStringElements         
     * @return Map<String,String>
     */
    public Map<String, String> getALLHashStringElements(String hashKey) {

        Map<byte[], byte[]> map = null;
        Map<String, String> resultMap = null;
        if (hashKey != null) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                map = jedis.hgetAll(hashKey.getBytes());
                resultMap = new HashMap<String, String>();
                if (map != null && map.size() > 0) {
                    Set<byte[]> set = map.keySet();
                    for (byte[] key : set) {
                        byte[] value = map.get(key);
                        if (value != null) {
                            resultMap.put(new String(key), new String(value));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();

                }
            }
        }
        return resultMap;
    }

    /**
     * 更新哈希字符元素值
     * @method updateHashElement         
     * @return boolean
     */
    public boolean updateHashStringElement(String hashKey, String field, String value) {
        boolean result = false;
        if (hashKey != null) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                jedis.hset(hashKey.getBytes(), field.getBytes(), value.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return result;
    }

    public boolean delHashQueueElements(String queueName, Set<String> fields) {
        boolean result = false;
        if (queueName != null && fields != null && fields.size() > 0) {
            byte[][] tmp = new byte[fields.size()][];
            int i = 0;
            for (String each : fields) {
                tmp[i] = each.getBytes();
                i++;
            }
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                long r = jedis.hdel(queueName.getBytes(), tmp);
                if (r == fields.size()) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return result;
    }

    /**
     * 
     * @method getHashQueueValues         
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getHashQueueValues(String queueName, Set<String> fields) {
        List<T> result = new ArrayList<T>();
        if (queueName != null && fields != null && fields.size() > 0) {
            byte[][] tmp = new byte[fields.size()][];
            int i = 0;
            for (String each : fields) {
                tmp[i] = each.getBytes();
                i++;
            }
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                List<byte[]> r = jedis.hmget(queueName.getBytes(), tmp);
                if (r != null) {
                    for (byte[] each : r) {
                        result.add((T) SeriUtil.toObject(each));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();

                }
            }
        }
        return result;
    }

    public long getQueueSize(String queueName) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            result = jedis.llen(queueName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    public Object getCacheData(String key) {
        Object result = null;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] tmp = jedis.get(key.getBytes());
            if (tmp != null) {
                try {
                    result = new ObjectInputStream(new ByteArrayInputStream(tmp)).readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    public List<Object> getCacheData(List<String> keys) {
        List<Object> result = new ArrayList<Object>();
        Jedis jedis = null;

        byte[][] total = new byte[keys.size()][];
        //		for(Object obj : elements){
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            total[i] = key.getBytes();
        }
        try {
            jedis = pool.getResource();
            //			System.out.println(total);
            List<byte[]> tmp = jedis.mget(total);
            for (byte[] each : tmp) {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(each));
                Object tmpObj = ois.readObject();
                result.add(tmpObj);
                ois.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    public boolean putCacheData(Map<String, ?> cacheMap) {
        boolean result = false;
        Jedis jedis = null;

        byte[][] total = new byte[cacheMap.size() * 2][];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int i = 0;
        //		for(int i = 0; i < cacheMap.size(); i++){
        for (String eachKey : cacheMap.keySet()) {
            Object cache = cacheMap.get(eachKey);
            try {
                new ObjectOutputStream(bos).writeObject(cache);
                byte[] t = bos.toByteArray();
                bos.flush();
                bos.reset();
                total[i++] = eachKey.getBytes();
                total[i++] = t;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            jedis = pool.getResource();
            String resp = jedis.mset(total);
            if ("OK".equals(resp)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    public <T> List<T> getRWCacheInStringMode(String keyPattern, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            ScanParams sp = new ScanParams();
            sp.match(keyPattern);
            sp.count(1000);
            ScanResult<String> rs = jedis.sscan("HashCacheKeysSet", "0", sp);
            List<String> keySet = rs.getResult();

            Pipeline pipeline = jedis.pipelined();

            for (String each : keySet) {
                pipeline.hgetAll(each);
            }
            List<Object> tmpList = pipeline.syncAndReturnAll();
            for (Object each : tmpList) {
                @SuppressWarnings("unchecked")
                Map<String, String> tmp = (Map<String, String>) each;
                T o = clazz.newInstance();
                for (String field : tmp.keySet()) {
                    String str = tmp.get(field);
                    Method m = clazz.getMethod("set" + GeneralUtil.FirstUpperCase(new String(field)), String.class);
                    m.invoke(o, str);
                }
                result.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    public <T> List<T> getRWCache(String keyPattern, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            ScanParams sp = new ScanParams();
            sp.match(keyPattern);
            sp.count(1000);
            ScanResult<String> rs = jedis.sscan("HashCacheKeysSet", "0", sp);
            List<String> keySet = rs.getResult();

            Pipeline pipeline = jedis.pipelined();

            for (String each : keySet) {
                pipeline.hgetAll(each.getBytes());
            }
            List<Object> tmpList = pipeline.syncAndReturnAll();
            for (Object each : tmpList) {
                @SuppressWarnings("unchecked")
                Map<byte[], byte[]> tmp = (Map<byte[], byte[]>) each;
                T o = clazz.newInstance();
                Method[] ms = clazz.getMethods();
                for (byte[] field : tmp.keySet()) {

                    Object obj = SeriUtil.toObject(tmp.get(field));
                    for (Method m : ms) {
                        if (m.getName().equals("set" + GeneralUtil.FirstUpperCase(new String(field)))) {
                            m.invoke(o, obj);
                        }
                    }
                }
                result.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
        return result;
    }

    public void removeCacheKey(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.srem("HashCacheKeysSet", key);
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();

            }
        }
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public <T> boolean addStringValue(String prefix, Map<String, T> keyValues) {
        boolean result = false;
        if (keyValues != null && keyValues.size() > 0) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                List<byte[]> tmpList = new ArrayList<byte[]>();
                for (String each : keyValues.keySet()) {
                    T t = keyValues.get(each);
                    tmpList.add((prefix + each).getBytes());
                    tmpList.add(SeriUtil.toByteArray(t));
                }
                byte[][] bytes = tmpList.toArray(new byte[tmpList.size()][]);
                String response = jedis.mset(bytes);
                if ("OK".equals(response)) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();

                }
            }
        }
        return result;
    }

    public List<Object> getStringTypeValues(List<String> keys) {
        List<Object> result = new ArrayList<Object>();
        Jedis jedis = null;
        if (keys != null && keys.size() > 0) {
            try {
                byte[][] byteKeys = new byte[keys.size()][];
                jedis = pool.getResource();
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    byteKeys[i] = key.getBytes();
                }

                List<byte[]> byteValues = jedis.mget(byteKeys);
                for (byte[] eachValue : byteValues) {
                    Object obj = SeriUtil.toObject(eachValue);
                    result.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();

                }
            }
        }
        return result;
    }

}
