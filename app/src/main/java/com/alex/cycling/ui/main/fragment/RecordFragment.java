package com.alex.cycling.ui.main.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.db.DbUtil;
import com.alex.cycling.ui.main.adapter.RecordAdapter;
import com.alex.cycling.utils.adapter.LoadMoreAdapter;
import com.alex.greendao.TrackInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/10.
 */
public class RecordFragment extends BaseFragment {

    @Bind(R.id.record_list)
    RecyclerView recordList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private RecordAdapter recordAdapter;
    private LoadMoreAdapter loadMoreAdapter;

    List<TrackInfo> trackInfos = new ArrayList<TrackInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_record, null);
        ButterKnife.bind(this, mainView);
        init();
        return mainView;
    }

    private void init() {
        trackInfos.clear();
        trackInfos.addAll(DbUtil.getTrackInfoService().queryAll());
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recordList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != swipeRefresh)
                            swipeRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        recordList.setHasFixedSize(true);
        recordList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordAdapter = new RecordAdapter(trackInfos);
        loadMoreAdapter = new LoadMoreAdapter(recordAdapter);
        recordList.setAdapter(loadMoreAdapter);

//        for (TrackInfo trackInfo : trackInfos) {
//            if (TextUtils.isEmpty(trackInfo.getImageUrl())) {
//                TrackManager.vacuate(trackInfo.getTrackUUID());
//            } else {
//                LogUtil.e(TrackManager.getBaiduUrlByDes(trackInfo.getImageUrl(), 100, 100).toString());
//            }
//        }

    }

    private void setRefreshing(boolean refreshing, boolean loadMore) {
        swipeRefresh.setEnabled(refreshing);
        swipeRefresh.setRefreshing(false);
//        ViewUtils.setVisibleOrGone(progress,loadMore);
        loadMoreAdapter.setProgressVisible(loadMore);
    }


    @Override
    public void onPageStart() {
        super.onPageStart();
        setRefreshing(true, false);
    }

    @Override
    public void onPageEnd() {
        super.onPageEnd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
