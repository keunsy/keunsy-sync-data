package com.keunsy.syncdata.common.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

    private static ApplicationContext apx = null;

    public static void initSpring(String configPath) {
        apx = new ClassPathXmlApplicationContext(configPath);
    }

    public static ApplicationContext getApx() {
        return apx;
    }

    public static void setApx(ApplicationContext apx) {
        SpringContext.apx = apx;
    }
}
