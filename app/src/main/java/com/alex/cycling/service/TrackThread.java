package com.alex.cycling.service;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.*;

import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.thread.ExecutUtils;
import com.alex.greendao.WorkPoint;
import com.jni.ActInfo;
import com.alex.cycling.client.TrackClient;
import com.alex.cycling.utils.BaiduTool;
import com.baidu.mapapi.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by comexs on 16/3/28.
 */
public class TrackThread extends Thread {

    private LocationSensor mLocationSersor;
    private List<ILocationBack> handlerListener;
    private long startTime = 0; //开始时间
    private long tempTime = 0;  //间隔时间
    private long lastTime = 0;  //恢复的时间

    private AtomicBoolean isEnd = new AtomicBoolean(); // 是否结束
    private Location mLocation;
    private int mSignal;
    private ActInfo actInfo;
    private Context context;
    private TTSUtils ttsUtils;

    public TrackThread(Context context) {
        mLocationSersor = new LocationSensor();
        mLocationSersor.setLocationListener(listener);
        this.context = context;
    }

    public void startWork() {
        mLocationSersor.start(context);
        if (getState() != State.RUNNABLE) {
            start();
        }
        isEnd.set(false);
        if (null == ttsUtils) {
            ttsUtils = new TTSUtils(context, new TTSUtils.InitSuccess() {
                @Override
                public void init() {
//                    ttsUtils.speek(WorkStatus.start);
                }
            });
        }
    }

    public void end() {
//        ttsUtils.speek(WorkStatus.end);
        mLocationSersor.end();
        interrupt();
        ExecutUtils.runInBack(new Runnable() {
            @Override
            public void run() {
                ttsUtils.stop();
                context.stopService(new Intent(context, MainLocationService.class));
            }
        }, 3000);
    }

    //保存轨迹
    public void saveTrack() {
        isEnd.set(true);
        TrackManager.closeTrackDB();
    }

    //最后一个点的时间开始恢复
    public void recoveryTrack() {
        WorkPoint workPoint = TrackManager.recoveryLastPoint();
        if (null == workPoint) {
            LogUtil.e("not can recovery , point is empty..");
            return;
        }
        mLocation = new Location("gps");
        mLocation.setLatitude(workPoint.lat);
        mLocation.setLongitude(workPoint.lon);
        mLocation.setAltitude(workPoint.alt);
        mLocation.setTime(workPoint.time);
        mLocation.setSpeed(workPoint.speed);
        lastTime = TrackManager.getLastTrackMillTime();
        startTime = SystemClock.elapsedRealtime();
        startWork();
    }


    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            synchronized (TrackThread.class) {
                long minTime = SystemClock.elapsedRealtime() + lastTime;
                if (minTime - tempTime > 1000) {
                    tempTime = minTime;
                    if (startTime == 0) {
                        startTime = tempTime;
                    }
                    if (isEnd.get()) {
                        end();
                        return;
                    }
                    if (null != handlerListener) {
                        for (ILocationBack listener : handlerListener) {
                            listener.onDataChange(mLocation, (tempTime - startTime) / 1000, mSignal, actInfo);
                        }
                    }
                }
            }
        }
    }

    private LocationSensor.LocationListener listener = new LocationSensor.LocationListener() {
        @Override
        public void onLocationChange(Location location, int signal) {
            if (null == location) {
                return;
            }
            handlerLocation(location);
            handlerAccount(location);
            mLocation = location;
            mSignal = signal;
            TrackManager.saveWorkPoint(location, isEnd.get());
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

    private void handlerAccount(Location location) {
        if (null == actInfo) {
            actInfo = new ActInfo();
        }
        if (null == mLocation) {
            return;
        }
        double distance = BaiduTool.getDis(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(location.getLatitude(), location.getLongitude()));
        distance = distance / 1000;
        actInfo.setDistance((actInfo.getDistance() + distance));
        actInfo.setClimbed(actInfo.getClimbed() + Math.abs(actInfo.getClimbed() - location.getAltitude()));
        if (0 != (tempTime - startTime) / 1000) {
            double aSpeed = actInfo.getDistance() / ((tempTime - startTime) / 1000) * 1000;
            actInfo.setAverSpeed(aSpeed * 3.6);
        }
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


    public void setOnMainListener(ILocationBack listener) {
        if (this.handlerListener == null) {
            this.handlerListener = new ArrayList<>();
        }
        this.handlerListener.add(listener);
    }

}
