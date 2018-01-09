package com.keunsy.syncdata.subscriber.threads.read;

import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.subscriber.dataCenter.QueueCenter;
import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.entity.UserInfo;
import com.keunsy.syncdata.subscriber.service.BasicService;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;


@Service
public class UserInfoReadThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("infoLog");

    private boolean isRunnable = false;//控制运行情况

    @Resource
    private BasicService basicService;

    public UserInfoReadThread() {
        this.isRunnable = true;
    }

    @Override
    public void run() {

        log.info(" Start!");
        long time = System.currentTimeMillis();

        while (isRunnable) {
            try {
                //读取数据
                List<UserInfo> list = basicService.readInfoList(RedisCfgCenter.KEY_POST_USERINFO);
                //添加到队列
                basicService.addToQueue(list, QueueCenter.getUserInfoQueue(), RedisCfgCenter.KEY_POST_USERINFO);
                //间隔时间
                long intervalTime = basicService.getIntervalTime(RedisCfgCenter.KEY_POST_USERINFO);

                this.noExceptionSleep(intervalTime);
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                this.noExceptionSleep(500);
            }
        }
        log.info(" Run Time = " + (System.currentTimeMillis() - time));
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
