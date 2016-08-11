package com.alex.cycling.ui.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.main.fragment.CyclingFragment;
import com.alex.cycling.ui.main.fragment.PersonFragment;
import com.alex.cycling.ui.main.fragment.RecordFragment;
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        getSupportActionBar().setTitle("");
        bottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_person, "").setInactiveIconResource(R.mipmap.ic_person))
                .addItem(new BottomNavigationItem(R.mipmap.ic_run, "").setInactiveIconResource(R.mipmap.ic_run))
                .addItem(new BottomNavigationItem(R.mipmap.ic_record, "").setInactiveIconResource(R.mipmap.ic_record))
                .initialise();
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
//        bottomBar.setActiveColor("#000000")
//                .setInActiveColor("#000000")
//                .setBarBackgroundColor("#000000");
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.actionbar_setting:
////                openActivity(SettingActivity.class);
//                break;
//            case R.id.actionbar_about:
////                openActivity(AboutActivity.class);
//
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
