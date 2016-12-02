package com.alex.cycling.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;

import com.alex.cycling.base.CSApplication;
import com.alex.cycling.utils.SystemUtil;
import com.jni.ActInfo;
import com.alex.cycling.service.MainLocationService;
import com.alex.cycling.utils.thread.ExecutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comexs on 16/3/28.
 */
public class TrackClient implements TClient {

    private static TrackClient client = null;

    List<OnCyclingListener> listenerList = new ArrayList<>(2);

    private MainLocationService mPlaybackService;
    private boolean mIsServiceBound;

    private TrackClient() {

    }

    /**
     * 非线程安全的
     *
     * @return
     */
    public static TrackClient getInstance() {
        if (null == client) {
            synchronized (TrackClient.class) {
                return client = new TrackClient();
            }
        }
        return client;
    }

    @Override
    public void start(Context context) {
        Intent intent = new Intent(context, MainLocationService.class);
        intent.putExtra(MainLocationService.TAG, MainLocationService.CODE_SERVICE_START);
        context.startService(intent);
        if (isRun()) {
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            mIsServiceBound = true;
        }
    }

    @Override
    public void pause(Context context) {
        if (mPlaybackService != null) mPlaybackService.pauseTrack();
    }

    @Override
    public void saveTrack(Context context) {
        if (mPlaybackService != null) mPlaybackService.saveTrack();
    }

    @Override
    public void endTrack(Context context) {
        if (mPlaybackService != null) mPlaybackService.pauseTrack();
        if (mIsServiceBound) {
            context.unbindService(serviceConnection);
        }
        listenerList.clear();
    }

    @Override
    public void recoveryTrack(Context context) {
        if (isRun()) {
            if (mPlaybackService != null) mPlaybackService.recoveryTrack();
        } else {
            Intent intent = new Intent(context, MainLocationService.class);
            intent.putExtra(MainLocationService.TAG, MainLocationService.CODE_SERVICE_RECOVERY);
            context.startService(intent);
            if (isRun()) {
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                mIsServiceBound = true;
            }
        }
    }

    @Override
    public void addTrackListener(OnCyclingListener listener) {
        if (null == listener) {
            return;
        }
        listenerList.add(listener);
    }

    @Override
    public void removeTranckListener(OnCyclingListener listener) {
        listenerList.remove(listener);
    }

    @Override
    public void onDataChange(final Location location, final long time, final int signal, final ActInfo actInfo) {
        for (final OnCyclingListener listener : listenerList) {
            if (null != listener) {
                ExecutUtils.runInMain(new Runnable() {
                    @Override
                    public void run() {
                        listener.onNewLocation(location, time, signal);
                        listener.onCycling(actInfo);
                    }
                });
            }
        }
    }

    @Override
    public boolean isRun() {
        return SystemUtil.isServiceRunning(CSApplication.getInstance(), MainLocationService.class);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlaybackService = ((MainLocationService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlaybackService = null;
        }
    };


    public interface OnCyclingListener {

        void onCycling(ActInfo actInfo);

        void onNewLocation(Location pt, long time, int signal);
    }

}
