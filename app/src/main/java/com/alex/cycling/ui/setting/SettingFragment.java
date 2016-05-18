package com.alex.cycling.ui.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.alex.cycling.R;

/**
 * Created by Administrator on 2016/5/17.
 */
public class SettingFragment extends PreferenceFragment {

    public static Fragment newInstance() {
        SettingFragment settingFragment = new SettingFragment();
        return settingFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.ui_settings);
    }

}
