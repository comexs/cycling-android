package com.alex.cycling.client;

import android.content.Context;

/**
 * Created by comexs on 16/3/28.
 */
public interface TClient {

    void start(Context context); //开启轨迹监听

    void pause(); //暂停

    void saveTrack(Context context); //保存

    void endTrack(Context context); //结束

    void recoveryTrack(Context context); //恢复

    void addTrackListener(TrackClient.OnCyclingListener listener);

    void removeTranckListener(TrackClient.OnCyclingListener listener);

    boolean isRun(Context context);  //是否在运行中
}
