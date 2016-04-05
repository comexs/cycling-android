package com.alex.cycling.client;

import android.content.Context;

/**
 * Created by comexs on 16/3/28.
 */
public interface ITrack {

    void start(Context context); //开启轨迹监听

    void pause(); //暂停

    void saveTrack(); //保存

    void endTrack(); //结束

    void resumeTrack(); //恢复

    void addTrackListener(TrackClient.OnCyclingListener listener);

    void removeTranckListener(TrackClient.OnCyclingListener listener);
}
