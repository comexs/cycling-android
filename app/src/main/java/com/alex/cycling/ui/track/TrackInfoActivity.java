package com.alex.cycling.ui.track;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.adapter.TabFragmentPagerAdapter;
import com.alex.cycling.ui.track.fragment.ChartFragment;
import com.alex.cycling.ui.track.fragment.TrackDetailFragment;
import com.alex.cycling.ui.track.fragment.TrackMainFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/20.
 */
public class TrackInfoActivity extends BaseActivity {

    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private TabFragmentPagerAdapter mTabAdapter;

    private String trackUUID;


    public static void newInstance(Context context, String uuid) {
        Intent intent = new Intent(context, TrackInfoActivity.class);
        intent.putExtra("uuid", uuid);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackinfo);
        ButterKnife.bind(this);
        initDate();
        initView();
    }

    private void initDate() {
        trackUUID = getIntent().getStringExtra("uuid");
    }

    private void initView() {
        mTabAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mTabAdapter.addTab(TrackMainFragment.newInstance(trackUUID), "轨迹");
        mTabAdapter.addTab(ChartFragment.newInstance(trackUUID), "图表");
        mTabAdapter.addTab(new TrackDetailFragment(), "数据");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mTabAdapter);
        tab.setupWithViewPager(viewPager);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.cmm_main_red));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
