package com.alex.cycling.service;

import android.location.Location;

import com.jni.ActInfo;

/**
 * Created by zhong on 2016/11/18.
 */

public interface ILocationBack {

    void onDataChange(Location location, long time, int signal, ActInfo actInfo);

}
