package com.alex.cycling.ui.main;

import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.main.fragment.CyclingFragment;
import com.alex.cycling.ui.main.fragment.PersonFragment;
import com.alex.cycling.ui.main.fragment.RecordFragment;
import com.alex.cycling.ui.widget.FragmentTabHost;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(android.R.id.tabhost)
    FragmentTabHost tabHost;

    private String[] tags = {"记录", "跑步", "个人"};

    private int[] imageArray = new int[]{R.drawable.bottom_menu_1, R.drawable.bottom_menu_2,
            R.drawable.bottom_menu_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        getSupportActionBar().setTitle("");
        initView();
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabHost.getTabWidget().setDividerDrawable(null); //去分割线
        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec(tags[0]).setIndicator(getTabItemView(0));
        tabHost.addTab(spec, RecordFragment.class, null);

        spec = tabHost.newTabSpec(tags[1]).setIndicator(getTabItemView(1));
        tabHost.addTab(spec, CyclingFragment.class, null);

        spec = tabHost.newTabSpec(tags[2]).setIndicator(getTabItemView(2));
        tabHost.addTab(spec, PersonFragment.class, null);  //

        tabHost.setCurrentTab(1);
//        initTab();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            }
        });

//        Sensor.

    }

    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = getLayoutInflater().inflate(R.layout.item_main_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(imageArray[index]);
//        TextView textView = (TextView) view.findViewById(R.id.textview);
//        textView.setText(tags[index]);
        return view;
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
