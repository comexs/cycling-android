package com.alex.cycling.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alex.cycling.R;
import com.alex.cycling.ui.setting.SettingFragment;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2016/5/16.
 */
public class FragmentContainerActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        if (savedInstanceState == null) {

        }
    }

}
