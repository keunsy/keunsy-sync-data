package com.keunsy.syncdata.subscriber.service;

import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.entity.TdInfo;
import com.keunsy.syncdata.subscriber.entity.gateway.GateUser;

import org.springframework.stereotype.Service;


/**    
 *     
 * 项目名称：syncDataSubscriber    
 * 类名称：UserInfoService    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-29 下午4:50:49    
 * 修改人：chenrong1    
 * 修改时间：2015-9-29 下午4:50:49    
 * 修改备注：    
 * @version     
 *     
 */
@Service
public class TdInfoService extends BasicService {

    private final String TABLE_CLUSTER = " td_info ";
    private final String TABLE_GATEWAY_CMPP = TABLE_CLUSTER;
    private final String TABLE_GATEWAY_SGIP = TABLE_CLUSTER;
    private final String TABLE_GATEWAY_SMGP = TABLE_CLUSTER;

    private final String WHERE_COLUMN_CLUSTER = "sn";//where条件字段，多个逗号分开，其值自动映射
    private final String WHERE_COLUMN_GATEWAY = WHERE_COLUMN_CLUSTER;

    //字段基本值(不含更新时间)
    private final String COLUMN_CLUSTER = "sn,td_name,td_code,status,td_type,td_sp_number,"
            + "filter_flag,submit_type,msg_count_cn,long_charge_count_cn,long_charge_count_pre_cn,"
            + "msg_count_en,long_charge_count_en,long_charge_count_pre_en,msg_count_all_cn,msg_count_all_en,"
            + "with_gate_sign,sign_type,reserve_td_code";

    private final String COLUMN_GATEWAY_CMPP = COLUMN_CLUSTER;
    private final String COLUMN_GATEWAY_SGIP = COLUMN_CLUSTER;
    private final String COLUMN_GATEWAY_SMGP = COLUMN_CLUSTER;

    /** 
     * 更新数据库
     * @method uptToDataSource         
     * @return boolean 
     */
    public int uptToDataSource(TdInfo tdInfo, String key_post) {

        String[] server_array = dealSyncServer(RedisCfgCenter.SYNC_SERVER_KEY_PRE + key_post);
        int result = 0;
        //集群
        if (server_array[0].contains("cluster")) {
            //进行更新
            result = updateDatabase(TABLE_CLUSTER, COLUMN_CLUSTER, WHERE_COLUMN_CLUSTER, tdInfo);
        } else if (server_array[0].contains("cmpp")) {
            //进行数据转化，装填（集群无需转化，其他需要）
            GateUser gateUser = transformBean(tdInfo, "cmpp");
            //进行更新
            result = updateDatabase(TABLE_GATEWAY_CMPP, COLUMN_GATEWAY_CMPP, WHERE_COLUMN_GATEWAY, gateUser);
        } else if (server_array[0].contains("sgip")) {
            //进行数据转化，装填（集群无需转化，其他需要）
            GateUser gateUser = transformBean(tdInfo, "sgip");
            //进行更新
            result = updateDatabase(TABLE_GATEWAY_SGIP, COLUMN_GATEWAY_SGIP, WHERE_COLUMN_GATEWAY, gateUser);
        } else if (server_array[0].contains("smgp")) {
            //进行数据转化，装填（集群无需转化，其他需要）
            GateUser gateUser = transformBean(tdInfo, "smgp");
            //进行更新
            result = updateDatabase(TABLE_GATEWAY_SMGP, COLUMN_GATEWAY_SMGP, WHERE_COLUMN_GATEWAY, gateUser);
        }
        return result;
    }

    /** 
     * 数据转化
     * @method transformBean         
     * @return GateUser 
     */
    private GateUser transformBean(TdInfo tdInfo, String server_key) {
        GateUser gateUser = new GateUser();
        /**
         * ?
         */

        return gateUser;
    }

}
