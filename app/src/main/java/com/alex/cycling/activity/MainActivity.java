package com.alex.cycling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.db.DbUtil;
import com.alex.cycling.fragment.CyclingFragment;
import com.alex.cycling.fragment.MapActivity;
import com.alex.cycling.utils.LogUtils;
import com.alex.greendao.TrackInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private TabFragmentPagerAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTabAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mTabAdapter.addTab(new CyclingFragment(), "记录");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(mTabAdapter);
        tab.setupWithViewPager(viewPager);
        tab.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.cmm_main_red));


        
//        List<TrackInfo> list = DbUtil.getTrackInfoService().queryAll();
//        LogUtils.e(list.size() + "");
//        for (TrackInfo trackInfo : list) {
//            LogUtils.e(trackInfo.getTrackUUID());
//        }
    }

}
