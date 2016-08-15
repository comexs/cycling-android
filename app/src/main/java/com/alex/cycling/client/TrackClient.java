package com.alex.cycling.client;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.SystemUtil;
import com.jni.ActInfo;
import com.alex.cycling.service.LocationService;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.service.TrackWorkThread;
import com.alex.cycling.utils.thread.ExecutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comexs on 16/3/28.
 */
public class TrackClient implements TClient {


    private static TrackClient client = null;

    TrackWorkThread trackWorkHandler;

    List<OnCyclingListener> listenerList = new ArrayList<OnCyclingListener>();

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
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
//        if (!SystemUtil.hasLocationServiceRun(context)) {
//            Intent intent = new Intent(context, LocationService.class);
//            context.startService(intent);
//        } else {
//
//            LogUtil.e("服务已启动");
//        }
    }

    @Override
    public void pause() {
//        if (null != trackWorkHandler) trackWorkHandler.p
//        listenerList.clear();
    }

    @Override
    public void saveTrack(Context context) {
        if (null != trackWorkHandler) {
            trackWorkHandler.saveTrack();
        } else {
            TrackManager.closeTrackDB();
        }
    }

    @Override
    public void endTrack(Context context) {
        listenerList.clear();
    }

    @Override
    public void recoveryTrack(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        intent.setAction(TrackManager.RECOVERY);
        context.startService(intent);
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
    public boolean isRun(Context context) {
        return SystemUtil.hasLocationServiceRun(context);
    }

    public void setWorkHandler(TrackWorkThread handler) {
        this.trackWorkHandler = handler;
        trackWorkHandler.setOnHandlerListener(handlerListener);
    }

    private TrackWorkThread.OnHandlerListener handlerListener = new TrackWorkThread.OnHandlerListener() {
        @Override
        public void onPostData(final Location location, final long time, final int signal, final ActInfo actInfo) {
            for (final OnCyclingListener listener : listenerList) {
                if (null == listener) {
                    return;
                }
                ExecutUtils.runInMain(new Runnable() {
                    @Override
                    public void run() {
                        listener.onNewLocation(location, time, signal);
                        listener.onCycling(actInfo);
                    }
                });
            }
        }
    };


    public interface OnCyclingListener {

        void onCycling(ActInfo actInfo);

        void onNewLocation(Location pt, long time, int signal);
    }

}
