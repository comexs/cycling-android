package com.alex.cycling.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.bean.ActInfo;
import com.alex.cycling.client.TrackClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by comexs on 16/3/27.
 */
public class CyclingFragment extends BaseFragment {

    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.speed)
    TextView speed;
    @Bind(R.id.distance)
    TextView distance;
    @Bind(R.id.start)
    FloatingActionButton start;
    @Bind(R.id.map)
    FloatingActionButton map;
    @Bind(R.id.end)
    FloatingActionButton end;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackClient.getInstance().addTrackListener(onCyclingListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_record, null);
        ButterKnife.bind(this, main);
        return main;
    }


    private TrackClient.OnCyclingListener onCyclingListener = new TrackClient.OnCyclingListener() {

        @Override
        public void onCycling(ActInfo actInfo) {

        }

        @Override
        public void onNewLocation(Location pt, long time, int signal) {
            CyclingFragment.this.time.setText(getTimeIntervalFormat(time));
            if (null == pt) {
                return;
            }
            speed.setText(String.format("%.2f", pt.getSpeed() * 3.6));
            distance.setText(String.format("%.2f", pt.getAltitude()));
        }
    };

//    private String getTime(long diff) {
//        long hour = (diff / (60 * 60));             //以小时为单位取整
//        long min = ((diff / (60)) - hour * 60);    //以分钟为单位取整
//        long second = (diff - hour * 60 * 60 - min * 60);
//        return String.format("%s:%s:%s", hour, min, second);
//    }

    private String getTimeIntervalFormat(long diff) {
        long time = diff;
        int h = (int) (time / (60 * 60));
        time = time % (60 * 60);
        int m = (int) (time / (60));
        time = time % (60);
        int s = (int) time;
        return String.format("%02d:%02d:%02d", h, m, s);
    }


    @OnClick({R.id.start, R.id.map, R.id.end})
    void click(View v) {
        switch (v.getId()) {
            case R.id.start:
                TrackClient.getInstance().start(getActivity());
                break;
            case R.id.map:
                openActivity(getActivity(), MapActivity.class);
                break;
            case R.id.end:
                TrackClient.getInstance().saveTrack();
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        TrackClient.getInstance().removeTranckListener(onCyclingListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
