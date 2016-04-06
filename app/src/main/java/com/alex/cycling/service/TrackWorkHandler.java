package com.alex.cycling.service;


import android.content.Context;
import android.location.Location;
import android.os.*;

import com.alex.cycling.bean.ActInfo;
import com.alex.cycling.client.TClient;
import com.alex.cycling.client.TrackClient;
import com.alex.greendao.WorkPoint;

import java.math.BigDecimal;

/**
 * Created by comexs on 16/3/28.
 */
public class TrackWorkHandler extends Thread {

    private LocationSersor mLocationSersor;
    private OnHandlerListener handlerListener;
    private long startTime = 0; //开始时间
    private long tempTime = 0;
    private boolean isEnd = false; // 是否结束
    private Location mLocation;
    private int mSignal;
    private ActInfo actInfo;

    public TrackWorkHandler() {
        mLocationSersor = new LocationSersor();
        mLocationSersor.setLocationListener(listener);
        TrackClient.getInstance().setWorkHandler(this);
    }

    public void startWork(Context context) {
        mLocationSersor.start(context);
        start();
    }

    public void end() {
        mLocationSersor.end();
        interrupt();
    }

    //保存轨迹
    public void saveTrack() {
        isEnd = true;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            synchronized (TrackWorkHandler.class) {
                long minTime = SystemClock.elapsedRealtime();
                if (minTime - tempTime > 1000) {
                    tempTime = minTime;
                    if (startTime == 0) {
                        startTime = minTime;
                    }
                    if (isEnd) {
                        end();
                        return;
                    }
                    if (null != handlerListener)
                        handlerListener.onPostData(mLocation, (tempTime - startTime) / 1000, mSignal, actInfo);
                }
            }
        }
    }

    private LocationSersor.LocationListener listener = new LocationSersor.LocationListener() {
        @Override
        public void onLocationChange(Location location, int signal) {
            if (null == location) {
                return;
            }
            mLocation = location;
            mSignal = signal;
            handlerLocation(location);
            WorkPoint workPoint = new WorkPoint();
            workPoint.setLat(location.getLatitude());
            workPoint.setLon(location.getLongitude());
            workPoint.setTime(location.getTime());
            workPoint.setSpeed(location.getSpeed());
            workPoint.setAlt(location.getAltitude());
//            LogUtils.e(workPoint.toString());
            if (isEnd) {
                TrackManager.closeTrackDB(workPoint);
                end();
                return;
            }
            TrackManager.saveWorkPoint(workPoint);
            location.setTime(location.getTime() - startTime);
        }
    };


    private void handlerLocation(Location location) {
        location.setTime(location.getTime() / 1000);
        if (startTime == 0) {
            startTime = location.getTime();
        }
        location.setLatitude(round(location.getLatitude()));
        location.setLongitude(round(location.getLongitude()));
        location.setAltitude(round(location.getAltitude(), 2));
        location.setSpeed(roundSpeed(location.getSpeed()));

    }

    //保留6位小数
    private double round(double old) {
        BigDecimal bg = new BigDecimal(old);
        return bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //保留6位小数,number表示具体保留几位
    private double round(double old, int number) {
        BigDecimal bg = new BigDecimal(old);
        return bg.setScale(number, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private float roundSpeed(float old) {
        BigDecimal bg = new BigDecimal(old);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    public void setOnHandlerListener(OnHandlerListener listener) {
        this.handlerListener = listener;
    }


    public interface OnHandlerListener {
        void onPostData(Location location, long time, int signal, ActInfo actInfo);
    }

}
