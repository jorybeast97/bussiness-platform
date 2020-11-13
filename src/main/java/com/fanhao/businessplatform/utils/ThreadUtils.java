package com.fanhao.businessplatform.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池封装相关内容
 * @author fanhao
 */
@Component
public class ThreadUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtils.class);

    private static final Integer DEFAULT_CORE_THREAD_SIZE = 2;
    private static final Integer DEFAULT_MAX_THREAD_SIZE = 4;
    private static final Integer DEFAULT_KEEP_ALIVE_TIME = 3000;
    private static ThreadPoolHelper threadPoolHelper;

    public static ThreadPoolHelper getThreadPoolHelper() {
        synchronized (ThreadUtils.class){
            if (threadPoolHelper == null) {
                threadPoolHelper = new ThreadPoolHelper();
            }
        }
        return threadPoolHelper;
    }

    /**
     * 实际线程管理类
     */
    public static class ThreadPoolHelper{
        private ThreadPoolExecutor threadPoolExecutor;
        @Value("${utils.thread-utils.core-thread-size}")
        private Integer coreThreadSize;
        @Value("${utils.thread-utils.max-thread-size}")
        private Integer maxThreadSize;
        @Value("${utils.thread-utils.keep-alive-time}")
        private Integer keepAliveTime;

        public ThreadPoolHelper() {
            if (coreThreadSize  == null) coreThreadSize = ThreadUtils.DEFAULT_CORE_THREAD_SIZE;
            if (maxThreadSize == null) maxThreadSize = ThreadUtils.DEFAULT_MAX_THREAD_SIZE;
            if (keepAliveTime == null) keepAliveTime = ThreadUtils.DEFAULT_KEEP_ALIVE_TIME;
        }

        public void execute(final Runnable task) {
            if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                threadPoolExecutor = new ThreadPoolExecutor(coreThreadSize,
                        maxThreadSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingDeque<>(),
                        Executors.defaultThreadFactory());
            }
            threadPoolExecutor.execute(task);
        }
    }
}
