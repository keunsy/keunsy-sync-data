package com.keunsy.syncdata.subscriber.service;

import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.common.utils.SeparatorUtil;
import com.keunsy.syncdata.common.utils.SqlUtil;
import com.keunsy.syncdata.subscriber.dao.BasicDAO;
import com.keunsy.syncdata.subscriber.dataCenter.QueueCenter;
import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.redis.RedisDcAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

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
     * @param <T>
     * @method readInfoList         
     * @return List<T>
     */
    public <T> List<T> readInfoList(String key_post) {

        String queueName_pre = RedisCfgCenter.QUEUE_SYNC_KEY_PRE + key_post;
        //获取读取服务器标识  一般只有一个
        String[] server_array = dealSyncServer(RedisCfgCenter.SYNC_SERVER_KEY_PRE + key_post);
        //获取读取数量
        int readCount = dealReadCount(key_post);

        List<Object> list = new ArrayList<Object>();
        if (server_array != null && server_array.length > 0) {
            //一般情况下为一个
            for (String server : server_array) {
                list.addAll(redisSource.getQueueElements(queueName_pre + SeparatorUtil.COLON + server, readCount));
            }
        }

        return toTList(list);
    }

    /** 
     * 将List<Object>转化为 List<T>
     * @param <T>
     * @method toTList         
     * @return List<T> 
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> toTList(List<Object> list) {

        List<T> tlist = new ArrayList<T>();
        if (list != null && list.size() > 0) {
            for (Object object : list) {
                tlist.add((T) object);
            }
        }
        return tlist;
    }

    /** 
     * 处理读取数量
     * @method dealReadCount         
     * @return int 
     */
    private int dealReadCount(String key_post) {

        String readCount_key = RedisCfgCenter.READ_COUNT_KEY_PRE + key_post;
        String readCount = RedisCfgCenter.getConfigMap().get(readCount_key);
        if (StringUtils.isBlank(readCount) || Integer.parseInt(readCount) > 4000) {
            readCount = RedisCfgCenter.READ_COUNT;
        }
        return Integer.parseInt(readCount);
    }

    /** 
     * 添加到队列中
     * @param <T>
     * @method addToQueue         
     * @return Boolean 
     */
    public <T> Boolean addToQueue(List<T> list, ArrayBlockingQueue<T> queue, String key_post) {

        boolean result = true;
        //存入队列
        if (list != null && list.size() != 0) {
            result = QueueCenter.addListToQueue(list, queue);
            if (!result) {//失败的情况 暂不处理
                log.info(" Add List To Queue FAIL , Some Info Losted!");
                log.error(" Add List To Queue FAIL , Some Info Losted!");
            }
            log.info(" Read Count:" + list.size() + "; Is Add Queue Success:" + result);
        } else {
            log.info(" Have No New Update Info!");
        }
        return result;
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
            server_array = new String[] { RedisCfgCenter.SYNC_SERVER };//默认值
        }
        return server_array;
    }

    /**
     * 从队列中获取单信息
     * @param <T>
     * @method getInfoFromQueue         
     * @return T
     */
    public <T> T getInfoFromQueue(ArrayBlockingQueue<T> queue) {

        return QueueCenter.getInfoFromQueue(queue);
    }

    /** 
     * 进行数据更新(常规情况下)
     * @method updateInfo         
     * @return int 
     */
    public int updateDatabase(String table_name, String column, String where_column, Object object) {

        int result = 0;
        String select_sql = SqlUtil.createSelectSql(table_name, column, where_column);
        //System.out.println("select_sql=====" + select_sql);
        Object[] select_array = SqlUtil.getParamObject(where_column, object);
        int count = basicDAO.queryCount(select_sql, select_array);
        //System.out.println("count======" + count);
        //非空则更新，否则插入
        if (count > 0) {
            //进行更新
            Object[] upt_array = SqlUtil.getParamObject(column + SeparatorUtil.COMMA + where_column, object);
            String update_sql = SqlUtil.createUpdateSql(table_name, column, where_column);
            //System.out.println("update_sql=====" + update_sql);
            result = basicDAO.executeUpdate(update_sql, upt_array);
        } else {
            //进行插入
            Object[] insert_array = SqlUtil.getParamObject(column, object);
            String insert_sql = SqlUtil.createInsertSql(table_name, column);
            //System.out.println("insert_sql=====" + insert_sql);
            result = basicDAO.executeUpdate(insert_sql, insert_array);
        }
        return result;
    }

    /** 
     * 获取某hash里所有元素
     * @param <T>
     * @method getAllHashElements         
     * @return void 
     */
    public <T> Map<String, T> getAllHashElements(String hashKey) {

        return redisSource.getALLHashElements(hashKey);

    }

    /** 
     * 删除某hash里所有元素
     * @method deldelHashAllElements         
     * @return void
     */
    public void deldelHashAllElements(String hashKey) {

        redisSource.delHashAllElements(hashKey);

    }
}
