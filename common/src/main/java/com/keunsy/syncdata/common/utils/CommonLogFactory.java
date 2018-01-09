//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keunsy.syncdata.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonLogFactory {
  private static CommonLogFactory commonLogFactory = new CommonLogFactory();
  private long last_load_prop_time = 0L;
  private Properties prop = new Properties();
  private static Map<String, Integer> logLevelMap = new HashMap();
  private Lock lock = new ReentrantLock();

  static {
    logLevelMap.put("ALL", Integer.valueOf(-2));
    logLevelMap.put("all", Integer.valueOf(-2));
    logLevelMap.put("TRACE", Integer.valueOf(-1));
    logLevelMap.put("trace", Integer.valueOf(-1));
    logLevelMap.put("DEBUG", Integer.valueOf(0));
    logLevelMap.put("debug", Integer.valueOf(0));
    logLevelMap.put("INFO", Integer.valueOf(1));
    logLevelMap.put("info", Integer.valueOf(1));
    logLevelMap.put("WARN", Integer.valueOf(2));
    logLevelMap.put("warn", Integer.valueOf(2));
    logLevelMap.put("ERROR", Integer.valueOf(3));
    logLevelMap.put("error", Integer.valueOf(3));
    logLevelMap.put("FATAl", Integer.valueOf(4));
    logLevelMap.put("fatal", Integer.valueOf(4));
    logLevelMap.put("OFF", Integer.valueOf(5));
    logLevelMap.put("off", Integer.valueOf(5));
  }

  public CommonLogFactory() {
  }

  private void loadProperties() {
    if(System.currentTimeMillis() - this.last_load_prop_time > 60000L) {
      this.last_load_prop_time = System.currentTimeMillis();

      try {
        this.lock.lock();
        String path = System.getProperty("user.dir") + "/config/";
        String name = "log_level.properties";
        String filePath = path + name;
        FileInputStream inStream = null;

        try {
          File file = new File(filePath);
          if(!file.exists()) {
            (new File(path, name)).createNewFile();
          }

          inStream = new FileInputStream(file);
          this.prop.load(inStream);
        } catch (Exception var30) {
          var30.printStackTrace();
        } finally {
          try {
            inStream.close();
          } catch (Exception var29) {
            ;
          }

        }
      } catch (Exception var32) {
        var32.printStackTrace();
      } finally {
        try {
          this.lock.unlock();
        } catch (Exception var28) {
          ;
        }

      }
    }

  }

  private int getlogLevelMap(String levelName) {
    int resutl = 0;
    if(logLevelMap.get(levelName) != null) {
      resutl = ((Integer)logLevelMap.get(levelName)).intValue();
    }

    return resutl;
  }

  private boolean isEnable(String levelName) {
    return this.getlogLevelMap(levelName) >= this.getlogLevelMap(this.getLogLevel());
  }

  public boolean isDebugEnabled() {
    return this.isEnable("DEBUG");
  }

  public boolean isErrorEnabled() {
    return this.isEnable("ERROR");
  }

  public boolean isFatalEnabled() {
    return this.isEnable("FATAL");
  }

  public boolean isInfoEnabled() {
    return this.isEnable("INFO");
  }

  public boolean isTraceEnabled() {
    return this.isEnable("TRACE");
  }

  public boolean isWarnEnabled() {
    return this.isEnable("WARN");
  }

  private String getLogLevel() {
    String result = "";
    this.loadProperties();
    result = this.prop.getProperty("log_level", "DEBUG");
    return result;
  }

  public static Log getLog(Class<?> clazz) {
    return new CommonLog(LogFactory.getLog(clazz), commonLogFactory);
  }

  public static Log getLog(String name) {
    return new CommonLog(LogFactory.getLog(name), commonLogFactory);
  }
}
