package com.alex.cycling.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.utils.SystemUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhong on 2016/8/10.
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.version_info)
    TextView versionInfo;
    @Bind(R.id.about_person)
    FrameLayout aboutPerson;
    @Bind(R.id.about_comment)
    FrameLayout aboutComment;
    @Bind(R.id.about_connect)
    FrameLayout aboutConnect;
    @Bind(R.id.about_share)
    FrameLayout aboutShare;
    @Bind(R.id.act_about_main)
    FrameLayout actAboutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        versionInfo.setText("V " + SystemUtil.getVersionName());
    }

    @OnClick({R.id.about_person, R.id.about_comment, R.id.about_connect, R.id.about_share})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.about_person:
                openActivity(PersonActivity.class);
                break;
            case R.id.about_comment:

                break;

            case R.id.about_connect:

                break;

            case R.id.about_share:

                break;
        }
    }
}
