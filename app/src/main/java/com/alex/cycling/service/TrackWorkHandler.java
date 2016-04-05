package com.alex.cycling.service;


import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alex.cycling.client.TrackClient;
import com.alex.cycling.db.DbUtil;
import com.alex.cycling.db.impl.WorkPointService;
import com.alex.cycling.utils.BaiduTool;
import com.alex.cycling.utils.LogUtils;
import com.alex.greendao.WorkPoint;
import com.baidu.mapapi.model.LatLng;

import java.math.BigDecimal;

/**
 * Created by comexs on 16/3/28.
 */
public class TrackWorkHandler extends Handler {

    //服务发出来的消息
    public final static int MSG_TRACK_START = -1;       //服务启动
    public final static int MSG_TRACK_GIVEUP = -2;      //放弃轨迹
    public final static int MSG_TRACK_SAVED = -3;       //轨迹保存
    public final static int MSG_SERVICE_INIT = -5;      //
    public final static int MSG_TRACK_RESTART = -6;


    //客户端发出来的命令
    public static final int CMM_SAVE_STATE = 1;
    public static final int CMM_START_TRACK = 2;
    public static final int CMM_CANCEL_PAUSE = 3;
    public static final int CMM_SET_AUTO_PAUSE = 4;
    public static final int CMM_PAUSE = 5;
    public static final int CMM_SAVE_TRACK = 6;
    public static final int CMM_END_TRACK = 7;
    public static final int CMM_RECOVERY_TRACK = 8;
    public static final int CMM_INIT = 9;


    private LocationSersor mLocationSersor;
    private OnHandlerListener handlerListener;
    private long startTime = 0; //开始时间
    private boolean isEnd = false; // 是否结束

    public TrackWorkHandler() {
        mLocationSersor = new LocationSersor();
        mLocationSersor.setLocationListener(listener);
        TrackClient.getInstance().setWorkHandler(this);
    }

    public void start(Context context) {
        mLocationSersor.start(context);
    }

    public void end() {
        mLocationSersor.end();

    }

    //保存轨迹
    public void saveTrack() {
        isEnd = true;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    private LocationSersor.LocationListener listener = new LocationSersor.LocationListener() {
        @Override
        public void onLocationChange(Location location, int signal) {
            if (null == location) {
                return;
            }
            handlerLocation(location);
            WorkPoint workPoint = new WorkPoint();
            workPoint.setLat(location.getLatitude());
            workPoint.setLon(location.getLongitude());
            workPoint.setTime(location.getTime());
            workPoint.setSpeed(location.getSpeed());
            workPoint.setAlt(location.getAltitude());
            LogUtils.e(workPoint.toString());
            if (isEnd) {
                TrackManager.closeTrackDB(workPoint);
                end();
                return;
            }
            TrackManager.saveWorkPoint(workPoint);
            location.setTime(location.getTime()-startTime);
            if (null != handlerListener) handlerListener.onPostData(location, signal);
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


    public void setOnHandlerListener(OnHandlerListener listener) {
        this.handlerListener = listener;
    }


    public interface OnHandlerListener {
        void onPostData(Location location, int signal);
    }

}
