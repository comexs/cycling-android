package com.alex.cycling.utils.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by comexs on 16/4/3.
 */
public class ExecutUtils {

    //多线程读写
    private static final ExecutorService sExecutorService = Executors.newCachedThreadPool();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    //单延迟线程池
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void executeAsync(Runnable runnable) {
        try {
            sExecutorService.execute(runnable);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    public static Handler getMainHandler() {
        return handler;
    }

    public static <T> void deliverValue(final Callback<T> callback,
                                        final T value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onValue(value);
            }
        });
    }

    public static void runInMain(Runnable runnable) {
        handler.post(runnable);
    }

    public static void runInBack(Runnable runnable) {
        executeAsync(runnable);
    }

    public static <T> void runInBack(final Runnable runnable, final Callback<T> callback) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                runInBack(runnable);
                deliverValue(callback, null);
            }
        });
    }

    public static void runInBack(Runnable runnable, int delayTime) {
        scheduler.schedule(runnable, delayTime, TimeUnit.MILLISECONDS);
    }

}
