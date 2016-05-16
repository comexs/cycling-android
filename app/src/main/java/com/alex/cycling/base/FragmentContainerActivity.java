package com.alex.cycling.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alex.cycling.R;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2016/5/16.
 */
public class FragmentContainerActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected int setContainerId() {
        return R.id.fl_container;
    }
}
