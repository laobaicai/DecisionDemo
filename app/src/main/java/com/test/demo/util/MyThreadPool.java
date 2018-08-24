package com.test.demo.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gehengmin on 2017/8/18.
 */

public class MyThreadPool {
    private static MyThreadPool mInstance = new MyThreadPool();
    private int corePoolSize; // 核心线程池的数量，同时能够执行的线程数量
    private int maxPoolSize; // 最大线程池数量
    private long keepAliveTime = 1; // 存活时间
    private TimeUnit unit = TimeUnit.HOURS;
    private ThreadPoolExecutor executor;

    private MyThreadPool() {
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1; // 当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行（有研究论证的）
        maxPoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit,
                new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    }

    public static MyThreadPool getInstance() {
        return mInstance;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    public void remove(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.remove(runnable);
    }
}
