package com.alex.cycling.base;

import android.app.Application;

import com.alex.cycling.db.DbCore;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by comexs on 16/3/27.
 */
public class CSApplication extends Application {


    private static final CSApplication application = new CSApplication();


    @Override
    public void onCreate() {
        super.onCreate();
        DbCore.init(this);
        DbCore.enableQueryBuilderLog();
        SDKInitializer.initialize(this);
    }


    public static Application getInstance() {
        return application;
    }

}
