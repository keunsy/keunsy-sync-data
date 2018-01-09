package com.keunsy.syncdata.subscriber.main;

import com.keunsy.syncdata.common.control.Stoppable;
import com.keunsy.syncdata.common.spring.SpringContext;
import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.common.utils.IOneObjectInvokable;
import com.keunsy.syncdata.common.utils.OneObjectUtil;
import com.keunsy.syncdata.common.utils.SortMapUtil;
import com.keunsy.syncdata.subscriber.dataCenter.QueueCenter;

import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;


public class MainServer implements IOneObjectInvokable {

    private final Log log = CommonLogFactory.getLog("monitor");
    private final Map<String, Thread> allThreads = new TreeMap<String, Thread>();

    public MainServer() {

    }

    // 程序入口
    public static void main(String[] args) {
        MainServer mainServer = new MainServer();
        OneObjectUtil.listen(mainServer);
    }

    @Override
    public void start() {

        log.info("程序启动");
        //初始化spring容器
        SpringContext.initSpring("com/keunsy/config/bean-config.xml");
        //获取线程配置文件
        Properties threads = (Properties) SpringContext.getApx().getBean("threadsCfg");
        //排序
        Map<String, String> map = getSortedThreadMap(threads);

        /*
         * 需按顺序执行
         */
        if (map != null && !map.isEmpty()) {
            Set<String> keySet = map.keySet();
            //根据线程名称运行线程
            for (String key : keySet) {
                String thread_name = map.get(key);
                Thread t = (Thread) SpringContext.getApx().getBean(thread_name);
                if (t != null) {
                    t.setName(thread_name);
                    t.start();
                    allThreads.put(thread_name, t);
                    log.info(key + ". [" + thread_name + "] Is Started.");
                    sleep(20);
                } else {
                    log.error(thread_name + " Is Null.");
                }
            }
            System.out.println("---------------------< 程序启动完成  >---------------------");
            log.info("------> 程序启动成功  <------");
        } else {
            System.out.println("---------------------< 线程为空,启动失败  >---------------------");
            log.info("------> 程序启动失败  <------");
        }
    }

    /**
     * 关闭所有可关闭的线程
     */
    @Override
    public void stop() {
        for (Thread thread : allThreads.values()) {
            if (thread instanceof Stoppable) {
                Stoppable t = (Stoppable) thread;
                t.doStop();
            }
        }
        QueueCenter.doStop();

        System.out.println("-----> 程序关闭 <------");
        System.exit(0);
    }

    public void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (Exception e) {

        }
    }

    /** 
     * 将配置文件排序
     * @method getSortedThreadMap         
     * @return void 
     */
    private Map<String, String> getSortedThreadMap(Properties threads) {

        Map<String, String> map = new HashMap<String, String>();
        if (threads != null && !threads.isEmpty()) {
            Set<Object> keySet = threads.keySet();
            for (Object object : keySet) {
                String key = (String) object;
                map.put(key, threads.getProperty(key));
            }
        }
        return SortMapUtil.sortMapByIntKey(map);
    }
}
