package com.alex.cycling.base;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseFragment extends SupportFragment {

    private boolean isVisibleToUser;
    private boolean isPrepare;
    private boolean isViewPage;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public ActionBar getSupportActionBar() {
        Activity act = getActivity();
        if (AppCompatActivity.class.isInstance(act)) {
            return ((AppCompatActivity) act).getSupportActionBar();
        }
        return null;
    }

    public void onPageStart() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisibleToUser == isVisibleToUser) {
            return;
        }
        this.isVisibleToUser = isVisibleToUser;
        if (!isPrepare) {
            return;
        }
        if (isVisibleToUser) {
            onPageStart();
        } else {
            onPageEnd();
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPage = ViewPager.class.isInstance(view.getParent());
    }


    @Override
    public void onResume() {
        super.onResume();
        isPrepare = true;
        if (!isViewPage) {
            onPageStart();
        } else if (isVisibleToUser) {
            onPageStart();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPrepare = false;
        if (!isViewPage) {
            onPageEnd();
        } else if (isVisibleToUser) {
            onPageEnd();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isVisibleToUser = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void onPageEnd() {

    }

    //********************************* OpenActivity ***********************/

    protected void openActivity(Activity activity, Class<?> pClass) {
        openActivity(activity, pClass, null);
    }

    protected void openActivity(Activity activity, Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(activity, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public void openActivityForResult(Activity activity, Class<?> pClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
