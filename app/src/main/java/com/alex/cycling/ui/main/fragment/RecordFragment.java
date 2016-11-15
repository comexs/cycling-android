package com.alex.cycling.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.db.DbUtil;
import com.alex.cycling.ui.main.adapter.RecordAdapter;
import com.alex.cycling.ui.track.TrackInfoActivity;
import com.alex.cycling.utils.LogUtil;
import com.alex.greendao.TrackInfo;
import com.alex.greendao.TrackInfoDao;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/10.
 */
public class RecordFragment extends BaseFragment {

    @BindView(R.id.record_list)
    RecyclerView mRecyclerView;

    private RecordAdapter recordAdapter;
    private View notLoadingView;

    List<TrackInfo> trackInfos = new ArrayList<TrackInfo>();

    int page = 0;

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

    private List<TrackInfo> queryTrack(int page) {
        return DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("1")).orderDesc(TrackInfoDao.Properties.StartTime).limit(5).offset(page * 5).list();
    }

    private String lastUUID;

    private void init() {
        trackInfos.clear();
        page = 0;
        trackInfos.addAll(queryTrack(page));
//        for (TrackInfo trackInfo : DbUtil.getTrackInfoService().queryAll()) {
//            if (TextUtils.isEmpty(lastUUID)) {
//                lastUUID = trackInfo.getTrackUUID();
//            } else {
//                if (lastUUID.equals(trackInfo.getTrackUUID())) {
//                    DbUtil.getTrackInfoService().delete(trackInfo);
//                }
//            }
//            lastUUID = trackInfo.getTrackUUID();
//        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordAdapter = new RecordAdapter(trackInfos);
        recordAdapter.openLoadMore(5, true);
        mRecyclerView.setAdapter(recordAdapter);
        recordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mRecyclerView == null) {
                    return;
                }
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (queryTrack(page).size() > 0) {
                            trackInfos.addAll(queryTrack(page));
                            recordAdapter.notifyDataChangedAfterLoadMore(true);
                        } else {
                            recordAdapter.showNotLoading(mRecyclerView);
                        }
                    }
                }, 2000);
            }
        });

        recordAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TrackInfoActivity.newInstance(getActivity(), trackInfos.get(position).getTrackUUID());
            }
        });
        recordAdapter.showCommonLoadingView(mRecyclerView);
        LogUtil.e(DbUtil.getTrackInfoService().queryBuilder().orderDesc(TrackInfoDao.Properties.StartTime).list().size() + "");
//        TrackManager.openTrackDb();openTrackDb
    }

    @Override
    public void onPageStart() {
        super.onPageStart();
    }

    @Override
    public void onPageEnd() {
        super.onPageEnd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
