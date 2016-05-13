package com.alex.cycling.ui.track.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.db.DbUtil;
import com.alex.cycling.service.TrackManager;
import com.alex.greendao.TrackInfo;
import com.alex.greendao.TrackInfoDao;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/24.
 */
public class TrackDetailFragment extends BaseFragment {

    @Bind(R.id.max_climb)
    TextView maxClimb;
    @Bind(R.id.climbup)
    TextView climbup;
    @Bind(R.id.climb_distance)
    TextView climbDistance;
    @Bind(R.id.most_speed)
    TextView mostSpeed;
    @Bind(R.id.avage_speed)
    TextView avageSpeed;
    @Bind(R.id.start_time)
    TextView startTime;
    @Bind(R.id.stop_time)
    TextView stopTime;
    @Bind(R.id.colaire)
    TextView colaire;

    private String trackUUID;
    TrackInfo trackInfo;


    public static Fragment newInstance(String trackUUID) {
        TrackDetailFragment fragment = new TrackDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", trackUUID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_data, null, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        trackUUID = getArguments().getString("uuid");
        trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackUUID)).unique();
        if (null == trackInfo) {
            return;
        }
//        maxClimb.setText(trackInfo.get);
//        climbup.setTex
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
