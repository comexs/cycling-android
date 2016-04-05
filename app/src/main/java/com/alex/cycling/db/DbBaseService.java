package com.alex.cycling.db;

import android.os.Handler;
import android.os.Looper;

import com.alex.cycling.utils.thread.Callback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by comexs on 16/3/30.
 */
public abstract class DbBaseService<T, K> {

    private AbstractDao<T, K> mDao;
    //多线程读写
    private static final ExecutorService sExecutorService = Executors.newCachedThreadPool();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private static void executeAsync(Runnable runnable) {
        try {
            sExecutorService.execute(runnable);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    private static <T> void deliverValue(final Callback<T> callback,
                                         final T value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onValue(value);
            }
        });
    }


    public DbBaseService(AbstractDao dao) {
        mDao = dao;
    }

    public void save(T item) {
        mDao.insert(item);
    }

    public void save(List<T> items) {
        mDao.insertInTx(items);
    }

    public void saveAsync(final T item) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                save(item);
            }
        });
    }

    public void saveOrUpdate(T item) {
        mDao.insertOrReplace(item);
    }

    public void saveOrUpdate(List<T> items) {
        mDao.insertOrReplaceInTx(items);
    }

    public void deleteByKey(K key) {
        mDao.deleteByKey(key);
    }

    public void delete(T item) {
        mDao.delete(item);
    }

    public void delete(T... items) {
        mDao.deleteInTx(items);
    }

    public void delete(List<T> items) {
        mDao.deleteInTx(items);
    }

    public void deleteAll() {
        mDao.deleteAll();
    }

    public void update(T item) {
        mDao.update(item);
    }

    public void update(T... items) {
        mDao.updateInTx(items);
    }

    public void update(List<T> items) {
        mDao.updateInTx(items);
    }

    public T query(K key) {
        return mDao.load(key);
    }

    public void queryAsync(final K key, final Callback<T> callback) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                T result = query(key);
                deliverValue(callback, result);
            }
        });
    }


    public List<T> queryAll() {
        return mDao.loadAll();
    }

    public List<T> query(String where, String... params) {

        return mDao.queryRaw(where, params);
    }

    public QueryBuilder<T> queryBuilder() {

        return mDao.queryBuilder();
    }

    public long count() {
        return mDao.count();
    }

    public void refresh(T item) {
        mDao.refresh(item);

    }

    public void detach(T item) {
        mDao.detach(item);
    }
}
