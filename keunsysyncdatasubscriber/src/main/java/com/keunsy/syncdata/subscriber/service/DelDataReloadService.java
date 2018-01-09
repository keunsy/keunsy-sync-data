package com.keunsy.syncdata.subscriber.service;


import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.common.utils.SeparatorUtil;
import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.redis.RedisDcAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 删除多余的数据
     * @method delExtraData         
     * @return void 
     */
    public void delExtraData(Map<String, List<Integer>> map) {

        if (map != null && !map.isEmpty()) {
            //设定步长（需根据sn串，以及sql 执行效率来设定 ）
            int step = 300;

            //提取删除对象map
            Map<String, String> delTableMap = getDelTableMap();

            //循环待处理sn数据
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                //获取表名
                String tableName = delTableMap.get(RedisCfgCenter.DEL_OBJECT_KEY_PRE + entry.getKey());
                if (StringUtils.isNotBlank(tableName)) {

                    List<Integer> snList = entry.getValue();//（list 必须排序）
                    if (snList != null && snList.size() > 0) {
                        //进行删除
                        doSplitSnToDel(snList, tableName, step);
                    }
                }
            }
        }

    }

    /** 
     * 拆分 snList算法， 避免sn串过长
     * @method doSplitSnToDel         
     * @return void 
     */
    private void doSplitSnToDel(List<Integer> snList, String tableName, int step) {

        int start = 0;
        int length = snList.size();
        int maxSn = 0;
        String sql = null;
        List<Integer> tempList = new ArrayList<Integer>();
        while (start < length) {
            try {
                //list数据分割
                if (length < step + start) {
                    tempList = snList.subList(start, length);
                } else {
                    tempList = snList.subList(start, start + step);
                }
                start += step;//累加起始值
                //获取sql
                sql = createDelPreSql(tableName, tempList, maxSn);
                if (length >= start) {
                    maxSn = tempList.get(tempList.size() - 1);//小到大排序最后一个最大
                    sql += " and sn <= " + maxSn;
                }
                //待删除
                //System.out.println(sql);
                int result = basicDAO.executeUpdate(sql, null);
                log.info("excute sql:" + sql + ";delete count:" + result);
            } catch (Exception e) {
                log.error("excute sql fail :" + sql, e);
                e.printStackTrace();
            }
        }

    }

    /** 
     * 生成删除sql
     * @method createDelPreSql         
     * @return String 
    */
    private String createDelPreSql(String tableName, List<Integer> snsList, int minSn) {

        String snStr = StringUtils.join(snsList, SeparatorUtil.COMMA);//sn字符串

        String sql = "delete from " + tableName + " where sn not in (" + snStr + ") and sn > " + minSn;

        return sql;
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
}
