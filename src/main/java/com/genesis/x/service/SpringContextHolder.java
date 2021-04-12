package com.genesis.x.service;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: liuxing
 * @Date: 2018/9/13 10:28
 * @Description:
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext = null;
    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

    public SpringContextHolder() {
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static String getRootRealPath() {
        String rootRealPath = "";

        try {
            rootRealPath = getApplicationContext().getResource("").getFile().getAbsolutePath();
        } catch (IOException var2) {
            logger.error("获取系统根目录失败", var2);
        }

        return rootRealPath;
    }

    public static String getResourceRootRealPath() {
        String rootRealPath = "";

        try {
            rootRealPath = (new DefaultResourceLoader()).getResource("").getFile().getAbsolutePath();
        } catch (IOException var2) {
            logger.error("获取资源根目录失败", var2);
        }

        return rootRealPath;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(name, requiredType);
    }

    public static void clearHolder() {
        if(logger.isDebugEnabled()) {
            logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        }

        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        logger.info("注入ApplicationContext到SpringContextHolder:{0}", applicationContext);
        if(SpringContextHolder.applicationContext != null) {
            logger.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        clearHolder();
    }

    private static void assertContextInjected() {
        Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.", new Object[0]);
    }
}
