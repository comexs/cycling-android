package com.alex.cycling.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.ToastUtil;

/**
 * Created by comexs on 16/3/28.
 */
public class LocationService extends Service {

    private final static String TAG_POWER = "power_cs";
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private TrackWorkThread workHandler;
    private Button wwButton;

    @Override

    public void onCreate() {
        super.onCreate();
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG_POWER);
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        }
        initService(intent);
        LogUtil.e("服务起来了");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dissWW();
        LogUtil.e("服务被销毁");
//        if (null != workHandler) {
//            workHandler.end();
//        }
    }

    private void initService(Intent intent) {
        if (null == intent) {   //恢复时走这里
            if (null == workHandler) {
                workHandler = new TrackWorkThread(getBaseContext());
                workHandler.recoveryTrack();
                LogUtil.e("恢复服务..");
            }
            return;
        }
        if (null == workHandler) {
            workHandler = new TrackWorkThread(getBaseContext());
        }
        if (!TextUtils.isEmpty(intent.getAction()) && intent.getAction().equals(TrackManager.RECOVERY)) {
            workHandler.recoveryTrack();
        } else {
            workHandler.startWork();
        }
        showWW();
    }

    private void showWW() {
        wwButton = new Button(getApplicationContext());
//        wwButton.setBackgroundColor(Color.RED);
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        int floatWindowSize = 1;
        /**
         * 以下都是WindowManager.LayoutParams的相关属性 具体用途请参考SDK文档
         */
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST; // 这里是关键，你也可以试试2003
        wmParams.format = PixelFormat.TRANSPARENT; // 设置图片格式，效果为背景透明
        /**
         * 这里的flags也很关键 代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
         * 40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
         */
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wmParams.width = floatWindowSize;
        wmParams.height = floatWindowSize;
        wmParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        wm.addView(wwButton, wmParams); // 创建View
    }

    private void dissWW() {
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(wwButton);
    }

}
