package com.alex.cycling.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;

import com.alex.cycling.R;
import com.alex.cycling.ui.main.MainActivity;
import com.alex.cycling.ui.main.fragment.CyclingFragment;
import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.MathUtil;
import com.alex.cycling.utils.ToastUtil;
import com.jni.ActInfo;

/**
 * Created by comexs on 16/3/28.
 */
public class LocationService extends Service implements TrackWorkThread.OnHandlerListener {

    private static final String ACTION_PLAY_TOGGLE = "io.github.ryanhoo.music.ACTION.PLAY_TOGGLE";
    private static final String ACTION_PLAY_LAST = "io.github.ryanhoo.music.ACTION.PLAY_LAST";
    private static final String ACTION_PLAY_NEXT = "io.github.ryanhoo.music.ACTION.PLAY_NEXT";
    private static final String ACTION_STOP_SERVICE = "io.github.ryanhoo.music.ACTION.STOP_SERVICE";

    private final static String TAG_POWER = "power_cs";
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private TrackWorkThread workHandler;
    private Button wwButton;

    private RemoteViews mContentViewBig, mContentViewSmall;
    private static final int NOTIFICATION_ID = 1;
    private long time;
    private ActInfo actInfo;

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
            workHandler.setOnHandlerListener(this);
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

    private void showNotification() {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(getSmallContentView())
                .setCustomBigContentView(getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        // Send the notification.
        startForeground(NOTIFICATION_ID, notification);
    }


    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_player);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);

        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
    }

    private void updateRemoteViews(RemoteViews remoteView) {
//        Song currentSong = mPlayer.getPlayingSong();
//        if (currentSong != null) {
//            remoteView.setTextViewText(R.id.text_view_name, currentSong.getDisplayName());
//            remoteView.setTextViewText(R.id.text_view_artist, currentSong.getArtist());
//        }
//        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlaying()
//                ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
//        Bitmap album = AlbumUtils.parseAlbum(getPlayingSong());
//        if (album == null) {
//            remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
//        } else {
//            remoteView.setImageViewBitmap(R.id.image_view_album, album);
//        }

        remoteView.setImageViewResource(R.id.image_view_play_toggle, R.drawable.ic_remote_view_pause);
        remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
        remoteView.setTextViewText(R.id.text_view_name, MathUtil.getTimeIntervalFormat(time));
        if (actInfo == null) {
            LogUtil.e("actInfo is null");
            remoteView.setTextViewText(R.id.text_view_artist, "距离：0km");
        } else {
            remoteView.setTextViewText(R.id.text_view_artist, String.format("距离：%.2f", actInfo.getDistance()));
        }
    }

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }


    @Override
    public void onPostData(Location location, long time, int signal, ActInfo actInfo) {
        this.time = time;
        this.actInfo = actInfo;
        showNotification();
    }
}
