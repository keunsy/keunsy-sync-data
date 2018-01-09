package com.keunsy.syncdata.main;

import com.keunsy.syncdata.service.BasicService;
import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.utils.CommonLogFactory;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class BasicThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("infoLog");

    //SQL语句
    private String beanColumn;

    private String beanTable;

    private String beanName;

    public String getBeanColumn() {
        return beanColumn;
    }

    public void setBeanColumn(String beanColumn) {
        this.beanColumn = beanColumn;
    }

    public String getBeanTable() {
        return beanTable;
    }

    public void setBeanTable(String beanTable) {
        this.beanTable = beanTable;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    private boolean isRunnable = false;//控制运行情况

    @Resource
    private BasicService basicService;

    public void setBasicService(BasicService basicService) {
        this.basicService = basicService;
    }

    public BasicThread() {
        this.isRunnable = true;
    }

    /**
     * 
     * 创建一个新的实例 BasicThread.    
     *    
     * @param beanName
     * @param beanTable
     * @param beanColumn
     */
    public BasicThread(String beanName, String beanTable, String beanColumn) {
        super();
        this.beanColumn = beanColumn;
        this.beanTable = beanTable;
        this.beanName = beanName;
        this.isRunnable = true;
    }

    @Override
    public void run() {

        log.info(" Start!");
        long time = System.currentTimeMillis();
        String sql = basicService.createSelectSql(beanTable, beanColumn);

        while (isRunnable) {
            try {
                //读取数据
                List<Map<String, Object>> list = basicService.readInfoList(beanName, sql);
                //添加到队列
                basicService.addToRedisQueue(list, beanName);
                //间隔时间
                long intervalTime = basicService.getIntervalTime(beanName);
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
