package com.alex.cycling.client;

import android.content.Context;
import android.location.Location;

import com.jni.ActInfo;

/**
 * Created by comexs on 16/3/28.
 */
public interface TClient {

    void start(Context context);

    void pause(Context context);

    void saveTrack(Context context);

    void endTrack(Context context);

    void recoveryTrack(Context context);

    void addTrackListener(TrackClient.OnCyclingListener listener);

    void removeTranckListener(TrackClient.OnCyclingListener listener);

    void onDataChange(Location location, long time, int signal, ActInfo actInfo);

    boolean isRun();

}
