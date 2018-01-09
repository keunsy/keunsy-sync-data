package com.keunsy.syncdata.threads.reload;

import com.keunsy.syncdata.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.service.DelDataReloadService;
import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.utils.CommonLogFactory;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class DelDataReloadThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("infoLog");

    //SQL语句
    private final String PRE_SQL = "select sn from ";

    private boolean isRunnable = false;//控制运行情况

    @Resource
    private DelDataReloadService delDataReloadService;

    public DelDataReloadThread() {
        this.isRunnable = true;
    }

    @Override
    public void run() {

        log.info(" Start!");
        long time = System.currentTimeMillis();

        while (isRunnable) {
            try {
                //读取数据
                Map<String, List<Integer>> map = delDataReloadService.readSnList(PRE_SQL);
                //添加到redis
                delDataReloadService.addToRedisHash(map);
                //间隔时间
                long intervalTime = delDataReloadService.getIntervalTime(RedisCfgCenter.DEL_INTERVAL_KEY);
                this.noExceptionSleep(intervalTime);

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
