package com.alex.cycling.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Administrator on 2015/10/14.
 */
public enum BaiduLocationUtils {

    instance;

    private LocationClient locationClient;
    private BDLocationListener exportLocationListener;

    public void init(Context context) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(locationListener);
        locationClient.setLocOption(option);
        locationClient.requestLocation();
    }

    public void start(Context context) {
        init(context);
        locationClient.start();
    }

    public void end() {
        locationClient.stop();
        locationClient.unRegisterLocationListener(locationListener);
        exportLocationListener = null;
    }

    public void setListener(BDLocationListener listener) {
        this.exportLocationListener = listener;
    }


    BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (exportLocationListener != null) {
                exportLocationListener.onReceiveLocation(bdLocation);
            }
            LogUtil.e("requestLocation success !");
        }
    };
}
