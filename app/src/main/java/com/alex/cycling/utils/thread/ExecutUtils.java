package com.alex.cycling.utils.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by comexs on 16/4/3.
 */
public class ExecutUtils {

    //多线程读写
    public static final ExecutorService sExecutorService = Executors.newCachedThreadPool();
    public static final Handler handler = new Handler(Looper.getMainLooper());

    public static void executeAsync(Runnable runnable) {
        try {
            sExecutorService.execute(runnable);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
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



}
