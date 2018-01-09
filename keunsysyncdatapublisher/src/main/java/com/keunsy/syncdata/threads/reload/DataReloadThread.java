package com.keunsy.syncdata.threads.reload;

import com.keunsy.syncdata.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.service.BasicService;
import com.keunsy.syncdata.service.RedisCfgService;
import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.utils.CommonLogFactory;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 *     
 * 项目名称：syncDataPublisher    
 * 类名称：DataReloadThread    
 * 类描述：    全部数据刷新线程
 * 创建人：chenrong1    
 * 创建时间：2015-10-10 下午5:24:28    
 * 修改人：chenrong1    
 * 修改时间：2015-10-10 下午5:24:28    
 * 修改备注：    
 * @version     
 *
 */
@Service
public class DataReloadThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("infoLog");

    private boolean isRunnable = false;//控制运行情况

    @Resource
    private BasicService basicService;
    @Resource
    private RedisCfgService redisCfgService;

    public DataReloadThread() {
        this.isRunnable = true;
    }

    @Override
    public void run() {

        log.info(" Start!");
        long time = System.currentTimeMillis();

        while (isRunnable) {
            try {

                String isNeedReloadAll = RedisCfgCenter.getConfigMap().get(RedisCfgCenter.IS_NEED_RELOAD_ALL_KEY);
                //默认不执行
                if (isNeedReloadAll != null && isNeedReloadAll.equals("1")) {
                    for (String key_post : RedisCfgCenter.modelList) {
                        //更新使之读取所有对象
                        redisCfgService.updateConfigAndRedis(RedisCfgCenter.SYNC_CONFIG_HASH_KEY,
                                RedisCfgCenter.IS_READ_ALL_KEY_PRE + key_post, "1");
                        log.info("Reload All Data For:" + key_post);
                    }
                    //间隔时间
                    long intervalTime = basicService.getIntervalTime(RedisCfgCenter.RELOAD_ALL_DATA_INTERVAL_KEY);
                    this.noExceptionSleep(intervalTime);
                }
                this.noExceptionSleep(10000);
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                this.noExceptionSleep(500);
            }
        }
        log.info(",  Run Time = " + (System.currentTimeMillis() - time));
    }

    /**
     * 关闭线程
     */
    @Override
    public boolean doStop() {
        this.isRunnable = false;
        this.interrupt();
        log.info(" Stop!");
        return true;
    }

    /**
     * 休眠
     * @method noExceptionSleep         
     * @return void
     */
    public void noExceptionSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
