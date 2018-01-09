package com.keunsy.syncdata.threads.reload;

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
 * 类名称：RedisConfigReloadThread    
 * 类描述：  redis 配置重写读取类
 * 创建人：chenrong1    
 * 创建时间：2015-9-28 下午1:36:43    
 * 修改人：chenrong1    
 * 修改时间：2015-9-28 下午1:36:43    
 * 修改备注：    
 * @version     
 *
 */
@Service
public class RedisConfigReloadThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("infoLog");
    private final String LOG_INFO_PRE = "[" + this.getClass().getSimpleName() + "]";

    private boolean isRunnable = false;//运行标识

    @Resource
    private RedisCfgService redisCfgService;

    public RedisConfigReloadThread() {
        this.isRunnable = true;
    }

    @Override
    public void run() {

        log.info(LOG_INFO_PRE + " Start!");
        this.noExceptionSleep(60000);//刚传输入值 延迟更新

        long time = System.currentTimeMillis();
        while (isRunnable) {
            try {
                //更新redis到map
                redisCfgService.uptConfigMapFromRedis();
                //获取刷新redis配置时间
                long reload_time = redisCfgService.getReloadIntervalTime();
                //刷新时间
                this.noExceptionSleep(reload_time);

            } catch (Exception e) {
                log.error(LOG_INFO_PRE, e);
                e.printStackTrace();
                this.noExceptionSleep(500);
            }
        }
        log.info(LOG_INFO_PRE + ",  Run Time = " + (System.currentTimeMillis() - time));
    }

    /**
     * 关闭线程
     */
    @Override
    public boolean doStop() {
        this.isRunnable = false;
        this.interrupt();
        log.info(LOG_INFO_PRE + " Stop!");
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
