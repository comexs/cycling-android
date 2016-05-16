package com.alex.cycling.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.ui.camera.CameraActivity;
import com.alex.cycling.ui.person.PersonActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by comexs on 16/4/10.
 */
public class PersonFragment extends BaseFragment {


//    @Bind(R.id.button)
//    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_person, null);
        ButterKnife.bind(this, main);
        return main;
    }

    @OnClick({R.id.statics, R.id.person_info})
    void click(View v) {
        switch (v.getId()) {
            case R.id.person_info:
                openActivity(getActivity(), PersonActivity.class);
                break;
            case R.id.statics:
                openActivity(getActivity(), CameraActivity.class);
                break;
        }
//        TestFragment testFragment = new TestFragment();
//        testFragment.show(getFragmentManager(), "aaa");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
