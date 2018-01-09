package com.keunsy.syncdata.subscriber.threads.write;

import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.subscriber.dataCenter.QueueCenter;
import com.keunsy.syncdata.subscriber.dataCenter.RedisCfgCenter;
import com.keunsy.syncdata.subscriber.entity.UserServiceInfo;
import com.keunsy.syncdata.subscriber.service.BasicService;
import com.keunsy.syncdata.subscriber.service.UserServiceInfoService;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 
 *     
 * 项目名称：syncDataPublisher    
 * 类名称：UserServiceInfoWriteThread    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-28 下午2:08:30    
 * 修改人：chenrong1    
 * 修改时间：2015-9-28 下午2:08:30    
 * 修改备注：    
 * @version     
 *
 */
@Service
public class UserServiceInfoWriteThread extends Thread implements Stoppable {

    @Resource
    private BasicService basicService;
    @Resource
    private UserServiceInfoService userServiceInfoService;

    private final Log log = CommonLogFactory.getLog("infoLog");

    private boolean isRunnable = false;

    public UserServiceInfoWriteThread() {
        this.isRunnable = true;
    }

    @Override
    public void run() {
        log.info(" Start!");
        long time = System.currentTimeMillis();
        while (isRunnable) {
            try {
                // 队列获取数据
                UserServiceInfo userServiceInfo = basicService.getInfoFromQueue(QueueCenter.getUserServiceInfoQueue());
                if (userServiceInfo != null) {
                    int result = userServiceInfoService.uptToDataSource(userServiceInfo,
                            RedisCfgCenter.KEY_POST_USERSERVICEINFO);
                    if (result < 1) {
                        log.error("Execute Sql Fail");
                        this.noExceptionSleep(500);
                    } else {
                        log.info("Execute Sql Success!");
                    }
                } else {
                    this.noExceptionSleep(1000);//队列为空，休眠
                }
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                this.noExceptionSleep(500);
            }
        }
        log.info("  Run Time = " + (System.currentTimeMillis() - time));
    }

    @Override
    public boolean doStop() {
        this.isRunnable = false;
        this.interrupt();
        log.info(" Stop!");
        return true;
    }

    public void noExceptionSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
