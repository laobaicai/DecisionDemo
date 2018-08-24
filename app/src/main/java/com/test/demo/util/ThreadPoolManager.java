package com.test.demo.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by gehengmin on 2017/8/18.
 */

public class ThreadPoolManager {
    private static final String TAG = ThreadPoolManager.class.getSimpleName();

    private static ThreadPoolManager instance = new ThreadPoolManager();

    private ThreadPoolManager() {

    }

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public void execute(Runnable runnable) {
        if (null != cachedThreadPool) {
            cachedThreadPool.submit(runnable);
        }
    }

    public Future execute(Callable callable) {
        if (null != cachedThreadPool) {
            return cachedThreadPool.submit(callable);
        }
        return null;
    }

    public void cancel(Future task) {
        if (null != task && !cachedThreadPool.isShutdown()) {
            task.cancel(true);
        }
    }
}
