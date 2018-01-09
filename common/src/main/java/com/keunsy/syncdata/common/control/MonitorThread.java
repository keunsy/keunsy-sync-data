package com.keunsy.syncdata.common.control;

import com.keunsy.syncdata.common.utils.CommonLogFactory;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class MonitorThread extends Thread implements Stoppable {

    private final Log log = CommonLogFactory.getLog("monitor");
    private boolean isRunnable = false;

    private Map<String, Thread> allThreads = new HashMap<String, Thread>();

    public MonitorThread() {
        this.isRunnable = true;
        allThreads.put("monitorThread", this);
    }

    @Override
    public void run() {
        while (isRunnable) {
            try {
                for (String key : allThreads.keySet()) {
                    log.info("Running state is : " + allThreads.get(key).getState() + "\t\t" + key);
                }
                log.info("-------------------------" + Calendar.getInstance().getTime() + "--------------------------");
                printMerry();
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
            } finally {
                try {
                    sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printMerry() {
        StringBuffer sb = new StringBuffer();
        sb.append("-------------------------MemorySize↓------------------------------\n\r");
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory();
        long total = run.totalMemory();
        long free = run.freeMemory();
        long usable = max - total + free;

        sb.append("最大内存 Memory.Size=" + max / 1024 / 1024 + " m\n\r")
                .append("已分配内存   Memory.Size=" + total / 1024 / 1024 + " m\n\r")
                .append("已分配内存中的剩余空间  Memory.Size=" + free / 1024 / 1024 + " m\n\r")
                .append("最大可用内存  Memory.Size=" + usable / 1024 / 1024 + " m\n\r");
        sb.append("*************************************************\n\r");
        log.info(sb.toString());
    }

    public void shutdown() {
        this.isRunnable = false;
        this.interrupt();
    }

    public Map<String, Thread> getAllThreads() {
        return allThreads;
    }

    public void setAllThreads(Map<String, Thread> allThreads) {
        this.allThreads = allThreads;
    }

    @Override
    public boolean doStop() {
        isRunnable = false;
        return true;
    }
}
