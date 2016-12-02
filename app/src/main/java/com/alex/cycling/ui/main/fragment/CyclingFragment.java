package com.alex.cycling.ui.main.fragment;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.client.TrackClient;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.ui.main.MapActivity;
import com.alex.cycling.utils.MathUtil;
import com.jni.ActInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by comexs on 16/3/27.
 */
public class CyclingFragment extends BaseFragment {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.speed)
    TextView speed;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.start)
    FloatingActionButton start;
    //    @BindView(R.id.map)
//    FloatingActionButton map;
//    @BindView(R.id.end)
//    FloatingActionButton end;
    @BindView(R.id.aveage_speed)
    TextView aveageSpeed;
//    @Bind(R.id.climbup)
//    TextView climbup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_run, null);
        ButterKnife.bind(this, main);
        init();
        return main;
    }

    private void init() {
//        WorkPoint workPoint = DbUtil.creTrackDb("default10").queryAll().get(0);
//        BaiduTool.geoAddress(new LatLng(workPoint.getLat(), workPoint.getLon()));
    }


    private TrackClient.OnCyclingListener onCyclingListener = new TrackClient.OnCyclingListener() {

        @Override
        public void onCycling(ActInfo actInfo) {
            if (null == actInfo) {
                return;
            }
            distance.setText(String.format("%.2f", actInfo.getDistance()));
            aveageSpeed.setText(String.format("%.2f", actInfo.getAverSpeed()));
//            climbup.setText(String.format("%.2f", actInfo.getClimbed()));
        }

        @Override
        public void onNewLocation(Location pt, long time, int signal) {
            CyclingFragment.this.time.setText(MathUtil.getTimeIntervalFormat(time));
            if (null == pt) {
                return;
            }
            speed.setText(String.format("%.2f", pt.getSpeed() * 3.6));
        }
    };

    @OnClick({R.id.start})
    void click(View v) {
        switch (v.getId()) {
            case R.id.start:
                //可以恢复
                if (TrackManager.hasRevovery()) {
                    showRecoveryDialoag();
                } else {
                    if (TrackClient.getInstance().isRun()) {
                        showSaveDialog();
                    } else {
                        TrackClient.getInstance().start(getActivity());
                        start.setImageResource(R.mipmap.ic_pause);
                        start.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cmm_main_red)));
                    }
                }
                break;
//            case R.id.map:
//                openActivity(getActivity(), MapActivity.class);
//                break;
//            case R.id.end:
//                TrackClient.getInstance().saveTrack(getContext());
//                break;
        }
    }


    public void showRecoveryDialoag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否继续上次未完成的跑步记录");
        builder.setPositiveButton("继续", dialog_click);
        builder.setNegativeButton("保存", dialog_click);
        builder.setCancelable(false);
        builder.show();
    }

    public void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否保存跑步记录");
        builder.setPositiveButton("继续", save_dialog_click);
        builder.setNegativeButton("保存", save_dialog_click);
        builder.setCancelable(false);
        builder.show();

        // TrackClient.getInstance().saveTrack(getContext());
    }

    private DialogInterface.OnClickListener dialog_click = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    TrackClient.getInstance().recoveryTrack(getContext());
                    start.setImageResource(R.mipmap.ic_pause);
                    start.setBackgroundColor(getResources().getColor(R.color.cmm_main_red));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    TrackClient.getInstance().saveTrack(getContext());
                    break;
                default:
                    break;
            }
        }
    };

    private DialogInterface.OnClickListener save_dialog_click = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    TrackClient.getInstance().recoveryTrack(getContext());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    TrackClient.getInstance().saveTrack(getContext());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onPageStart() {
        super.onPageStart();
        TrackClient.getInstance().addTrackListener(onCyclingListener);
    }

    @Override
    public void onPageEnd() {
        super.onPageEnd();
        TrackClient.getInstance().removeTranckListener(onCyclingListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_comm, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem setting = menu.findItem(R.id.menu_common);
        setting.setTitle("地图");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_common:
//                openActivity(getActivity(), MapActivity.class);
                TrackClient.getInstance().pause(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
