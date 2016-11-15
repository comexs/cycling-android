package com.alex.cycling.service;


import android.content.Context;
import android.location.Location;
import android.os.Process;

import com.alex.cycling.db.DbUtil;
import com.alex.greendao.WorkPoint;

import java.util.List;

/**
 * Created by comexs on 16/3/28.
 */
public class CacheWorkThread extends Thread {

    private volatile boolean mQuit = false;
    LocationSensor locationSersor;

    List<WorkPoint> list = null;
    Context context;

    public CacheWorkThread(Context context, LocationSensor locationSersor) {
        this.locationSersor = locationSersor;
        this.context = context;
        list = DbUtil.creTrackDb("f0f7aa84-078b-420a-812a-e84f1ce39530").queryAll();  //f0f7aa84-078b-420a-812a-e84f1ce39530.db
    }

    int i = 0;

    public void quit() {
        mQuit = true;
        interrupt();
//        stop();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            synchronized (CacheWorkThread.class) {
//            long startTimeMs = SystemClock.elapsedRealtime();
                try {
                    // Take a request from the queue.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // We may have been interrupted because it was time to quit.
                    if (mQuit) {
                        return;
                    }
                    continue;
                }
                WorkPoint workPoint = list.get(i);
                Location location = new Location("gps");
                location.setLatitude(workPoint.getLat());
                location.setLongitude(workPoint.getLon());
                location.setAltitude(workPoint.getAlt());
                location.setTime(workPoint.getTime());
                location.setSpeed(workPoint.getSpeed());
                locationSersor.onLocationChanged(location);
                i++;
            }
        }
    }
}
