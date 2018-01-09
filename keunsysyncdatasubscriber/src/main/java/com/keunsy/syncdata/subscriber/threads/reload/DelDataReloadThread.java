package com.keunsy.syncdata.subscriber.threads.reload;

import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.subscriber.dao.BasicDAO;
import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.service.DelDataReloadService;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


@Service
public class DelDataReloadThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("infoLog");

    private boolean isRunnable = false;//控制运行情况

    @Resource
    private DelDataReloadService delDataReloadService;
    @Resource
    private BasicDAO basicDAO;

    public DelDataReloadThread() {
        this.isRunnable = true;
    }

    @Override
    public void run() {

        log.info(" Start!");
        long time = System.currentTimeMillis();

        while (isRunnable) {
            try {
                //从redis中读取数据
                Map<String, List<Integer>> map = delDataReloadService
                        .getAllHashElements(RedisCfgCenter.SYNC_DEL_SN_KEY);

                //循环数据 获取对应对象的表名
                if (map != null && !map.isEmpty()) {
                    //读取后进行删除，防止数据添加同步完成而redis数据未进行更新
                    delDataReloadService.deldelHashAllElements(RedisCfgCenter.SYNC_DEL_SN_KEY);
                    //删除多余数据
                    delDataReloadService.delExtraData(map);
                }
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
