package com.alex.cycling.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

/**
 * Created by comexs on 16/3/28.
 */
public class LocationService extends Service {

    private final static String TAG_POWER = "power_cs";
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private TrackWorkHandler workHandler;

    @Override

    public void onCreate() {
        super.onCreate();
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG_POWER);
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        }
        initService();
        return START_STICKY;
    }

    private void initService() {
        workHandler = new TrackWorkHandler();
        workHandler.startWork(getBaseContext());
    }

}
