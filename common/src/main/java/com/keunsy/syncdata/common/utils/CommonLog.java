//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keunsy.syncdata.common.utils;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.log4j.PropertyConfigurator;

public class CommonLog implements Log {
  private Log log = null;
  private CommonLogFactory commonLogFactory;

  public CommonLog(Log log, CommonLogFactory commonLogFactory) {
    this.log = log;
    this.commonLogFactory = commonLogFactory;
    String base_path = System.getProperty("user.dir") + "/config/log4j.properties";
    File file = new File(base_path);
    boolean flag = true;
    if(!file.exists()) {
      flag = false;
    }

    if(flag) {
      PropertyConfigurator.configure(base_path);
    } else {
      System.err.println(base_path + " 日志配置文件不存在！");
    }

  }

  public void debug(Object arg0) {
    if(this.commonLogFactory.isDebugEnabled()) {
      this.log.debug(arg0);
    }

  }

  public void debug(Object arg0, Throwable arg1) {
    if(this.commonLogFactory.isDebugEnabled()) {
      this.log.debug(arg0, arg1);
    }

  }

  public void error(Object arg0) {
    if(this.commonLogFactory.isErrorEnabled()) {
      this.log.error(arg0);
    }

  }

  public void error(Object arg0, Throwable arg1) {
    if(this.commonLogFactory.isErrorEnabled()) {
      this.log.error(arg0, arg1);
    }

  }

  public void fatal(Object arg0) {
    if(this.commonLogFactory.isFatalEnabled()) {
      this.log.fatal(arg0);
    }

  }

  public void fatal(Object arg0, Throwable arg1) {
    if(this.commonLogFactory.isFatalEnabled()) {
      this.log.fatal(arg0, arg1);
    }

  }

  public void info(Object arg0) {
    if(this.commonLogFactory.isInfoEnabled()) {
      this.log.info(arg0);
    }

  }

  public void info(Object arg0, Throwable arg1) {
    if(this.commonLogFactory.isInfoEnabled()) {
      this.log.info(arg0, arg1);
    }

  }

  public boolean isDebugEnabled() {
    return this.log.isDebugEnabled();
  }

  public boolean isErrorEnabled() {
    return this.log.isErrorEnabled();
  }

  public boolean isFatalEnabled() {
    return this.log.isFatalEnabled();
  }

  public boolean isInfoEnabled() {
    return this.log.isInfoEnabled();
  }

  public boolean isTraceEnabled() {
    return this.log.isTraceEnabled();
  }

  public boolean isWarnEnabled() {
    return this.log.isWarnEnabled();
  }

  public void trace(Object arg0) {
    if(this.commonLogFactory.isTraceEnabled()) {
      this.log.trace(arg0);
    }

  }

  public void trace(Object arg0, Throwable arg1) {
    if(this.commonLogFactory.isTraceEnabled()) {
      this.log.trace(arg0, arg1);
    }

  }

  public void warn(Object arg0) {
    if(this.commonLogFactory.isWarnEnabled()) {
      this.log.warn(arg0);
    }

  }

  public void warn(Object arg0, Throwable arg1) {
    if(this.commonLogFactory.isWarnEnabled()) {
      this.log.warn(arg0, arg1);
    }

  }
}
