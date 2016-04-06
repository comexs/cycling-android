package com.alex.cycling.client;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.alex.cycling.bean.ActInfo;
import com.alex.cycling.service.LocationService;
import com.alex.cycling.service.TrackWorkHandler;
import com.alex.cycling.utils.thread.ExecutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comexs on 16/3/28.
 */
public class TrackClient implements TClient {


    private static final TrackClient client = new TrackClient();

    TrackWorkHandler trackWorkHandler;

    List<OnCyclingListener> listenerList = new ArrayList<OnCyclingListener>();


    private TrackClient() {

    }


    public static TrackClient getInstance() {
        return client;
    }

    @Override
    public void start(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }

    @Override
    public void pause() {
//        if (null != trackWorkHandler) trackWorkHandler.p
//        listenerList.clear();
    }

    @Override
    public void saveTrack() {
        if (null != trackWorkHandler) trackWorkHandler.saveTrack();
    }

    @Override
    public void endTrack() {
        listenerList.clear();
    }

    @Override
    public void resumeTrack() {

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

    public void setWorkHandler(TrackWorkHandler handler) {
        this.trackWorkHandler = handler;
        trackWorkHandler.setOnHandlerListener(handlerListener);
    }

    private TrackWorkHandler.OnHandlerListener handlerListener = new TrackWorkHandler.OnHandlerListener() {
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
