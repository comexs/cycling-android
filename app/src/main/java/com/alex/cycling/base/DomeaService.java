package com.alex.cycling.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * DO NOT do anything in this Service!<br/>
 * <p/>
 * Created by Mars on 12/24/15.
 */
public class DomeaService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }
}
