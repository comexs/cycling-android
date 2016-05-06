package com.alex.cycling.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.alex.cycling.R;
import com.alex.cycling.utils.SystemBarTintManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    protected DialogHelper dialogHelper;

    /************************
     * Activity LifeCycle For Debug
     *******************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOverflowMenu();
        initWindow();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getRootView().setFitsSystemWindows(true);
        }
        mToolbar = (Toolbar) findViewById(R.id.common_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        initActionBar();
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getStatusBarColor());
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    public int getStatusBarColor() {
        return getColorPrimary();
    }

    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    protected View getRootView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    protected void initActionBar(String title) {
        if (null != mToolbar) {
            ActionBar actionBar = getSupportActionBar();
            if (null != actionBar) {
                int mask = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM;
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, mask);
                actionBar.setDefaultDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setTitle(title);
            }
        }
    }

    //强制出现overflow(三个点的设置)按钮
    protected void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //隐藏在overflow当中的Action按钮的图标显示出来
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    protected void initActionBar() {
        if (null != mToolbar) {
            ActionBar actionBar = getSupportActionBar();
            if (null != actionBar) {
                int mask = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME;
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, mask);
                actionBar.setDefaultDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 当ActionBar图标被点击时调用
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************
     * OpenActivity
     ***********************/

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
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

    public void openActivityForResult(Class<?> pClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * 显示进度对话框
     *
     * @param msg 消息
     */
    public void showProDialog(String msg) {
        if (null == dialogHelper) {
            dialogHelper = new DialogHelper(this);
        }
        dialogHelper.showProgressDialog(msg);
    }

    public void hideProDialog() {
        if (null != dialogHelper) {
            dialogHelper.dismissProgressDialog();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //屏敝掉menu按键点击
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置默认的退出效果
     */
    @Override
    public void finish() {
        super.finish();
    }

}
