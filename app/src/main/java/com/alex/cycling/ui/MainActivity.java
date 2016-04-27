package com.alex.cycling.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.fragment.CyclingFragment;
import com.alex.cycling.ui.fragment.PersonFragment;
import com.alex.cycling.ui.fragment.RecordFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomBar;


    PersonFragment personFragment;
    CyclingFragment cyclingFragment;
    RecordFragment recordFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        bottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "Home"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_run, "跑步"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bottom_menu, "记录"))
                .initialise();
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar.setMode(BottomNavigationBar.MODE_CLASSIC);
        bottomBar.setActiveColor(R.color.holo_black).setInActiveColor(R.color.colorPrimary);
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                tableSelect(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        bottomBar.selectTab(1);
    }

    public void tableSelect(int position) {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (null == personFragment) {
                    personFragment = new PersonFragment();
                }
                localFragmentTransaction.replace(R.id.realtabcontent, personFragment, "tab_one");
                localFragmentTransaction.commit();
                break;
            case 1:
                if (null == cyclingFragment) {
                    cyclingFragment = new CyclingFragment();
                }
                localFragmentTransaction.replace(R.id.realtabcontent, cyclingFragment, "tab_two");
                localFragmentTransaction.commit();
                break;
            case 2:
                if (null == recordFragment) {
                    recordFragment = new RecordFragment();
                }
                localFragmentTransaction.replace(R.id.realtabcontent, recordFragment, "tab_third");
                localFragmentTransaction.commit();
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
