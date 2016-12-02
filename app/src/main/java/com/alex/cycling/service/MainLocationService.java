package com.alex.cycling.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;

import com.alex.cycling.R;
import com.alex.cycling.client.TClient;
import com.alex.cycling.client.TrackClient;
import com.alex.cycling.ui.main.MainActivity;
import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.MathUtil;
import com.jni.ActInfo;

/**
 * Created by comexs on 16/3/28.
 */
public class MainLocationService extends Service implements ILocationBack {

    public static final String ACTION_START = "alex.ACTION.START";
    public static final String ACTION_STOP = "alex.ACTION.STOP";
    public static final String ACTION_SAVE = "alex.ACTION.SAVE";
    public static final String ACTION_RECOVERY = "alex.ACTION.RECOVERY";

    public static final String TAG = "service_code";
    public static final int CODE_SERVICE_START = 0;
    public static final int CODE_SERVICE_PAUSE = 1;
    public static final int CODE_SERVICE_STOP = -1;
    public static final int CODE_SERVICE_SAVE = 2;
    public static final int CODE_SERVICE_RECOVERY = 3;

    private final static String TAG_POWER = "power_cs";
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private TrackThread workHandler;
    private Button wwButton;
    private TClient mClient;

    private RemoteViews mContentViewBig, mContentViewSmall;
    private static final int NOTIFICATION_ID = 1;
    private long time;

    private final Binder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MainLocationService getService() {
            return MainLocationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e("service is bind..");
        return mBinder;
//        throw new UnsupportedOperationException("Not yet implemented");
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
        LogUtil.e("service has start..");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dissWW();
        LogUtil.e("service has stop..");
    }

    private void initService(Intent intent) {
        showWW();
        if (mClient == null) {
            mClient = TrackClient.getInstance();
        }
        if (null == intent) {   //恢复时走这里
            recoveryTrack();
            return;
        }
        if (intent.getIntExtra(TAG, 0) == CODE_SERVICE_START) {
            startTrack();
            return;
        }
        switch (intent.getAction()) {
            case ACTION_START:
                startTrack();
                break;
            case ACTION_STOP:
                pauseTrack();
                break;
            case ACTION_SAVE:
                saveTrack();
                break;
            case ACTION_RECOVERY:
                recoveryTrack();
                break;
        }
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        return super.stopService(name);
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

        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_START));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_STOP));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_SAVE));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_RECOVERY));
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

        remoteView.setTextViewText(R.id.text_view_artist, "距离：0km");

//        if (actInfo == null) {
//            LogUtil.d("actInfo is null");
//            remoteView.setTextViewText(R.id.text_view_artist, "距离：0km");
//        } else {
//            remoteView.setTextViewText(R.id.text_view_artist, String.format("距离：%.2f", actInfo.getDistance()));
//        }
    }

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }


    public void startTrack() {
        if (null == workHandler) {
            workHandler = new TrackThread(getApplicationContext());
        }
        workHandler.startWork();
        workHandler.setOnMainListener(this);
    }

    public void pauseTrack() {
        LogUtil.e("service is pause..");
//        if (workHandler != null) workHandler
    }

    public void saveTrack() {
        LogUtil.e("service is save..");
        if (workHandler != null) workHandler.saveTrack();
    }

    public void recoveryTrack() {
        if (isTrackRun()) {
            return;
        }
        if (null == workHandler) {
            workHandler = new TrackThread(getApplicationContext());
        }
        workHandler.recoveryTrack();
        LogUtil.e("service has recovery..");
    }

    public boolean isTrackRun() {
        if (workHandler != null && workHandler.isAlive() && !workHandler.isInterrupted()) {
            LogUtil.e("service is run..");
            return true;
        }
        return false;
    }

    @Override
    public void onDataChange(Location location, long time, int signal, ActInfo actInfo) {
        this.time = time;
        showNotification();
        if (mClient != null) mClient.onDataChange(location, time, signal, actInfo);
    }

}
