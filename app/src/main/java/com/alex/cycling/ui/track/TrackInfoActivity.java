package com.alex.cycling.ui.track;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.main.adapter.TabFragmentPagerAdapter;
import com.alex.cycling.ui.track.fragment.TrackChartFragment;
import com.alex.cycling.ui.track.fragment.TrackMainFragment;
import com.alex.cycling.ui.widget.CustomViewPager;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/20.
 */
public class TrackInfoActivity extends BaseActivity implements OnMenuItemClickListener {

    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.viewPager)
    CustomViewPager viewPager;

    private TabFragmentPagerAdapter mTabAdapter;
    private String trackUUID;
    private ContextMenuDialogFragment mMenuDialogFragment;

    public static void newInstance(Context context, String uuid) {
        Intent intent = new Intent(context, TrackInfoActivity.class);
        intent.putExtra("uuid", uuid);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackinfo);
        ButterKnife.bind(this);
        initDate();
        initView();
        initMenuFragment();
    }

    private void initDate() {
        trackUUID = getIntent().getStringExtra("uuid");
    }

    private void initView() {
        mTabAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mTabAdapter.addTab(TrackMainFragment.newInstance(trackUUID), "轨迹");
        mTabAdapter.addTab(TrackChartFragment.newInstance(trackUUID), "图表");
//        mTabAdapter.addTab(TrackDetailFragment.newInstance(trackUUID), "数据");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(mTabAdapter);
        tab.setupWithViewPager(viewPager);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.cmm_main_red));
        viewPager.setNoScroll(true);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.toolbar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject("关闭");
        close.setColor(R.color.cmm_main_red);
        close.setResource(R.mipmap.ic_share_close);

        MenuObject shareQQ = new MenuObject("QQ");
        shareQQ.setResource(R.mipmap.ic_share_qq);

        MenuObject shareQZone = new MenuObject("QQ空间");
        shareQZone.setResource(R.mipmap.ic_share_qzone);

        MenuObject shareFriend = new MenuObject("朋友圈");
        shareFriend.setResource(R.mipmap.ic_share_friend);

        MenuObject shareWchat = new MenuObject("微信");
        shareWchat.setResource(R.mipmap.ic_share_wchat);

        MenuObject shareWeibo = new MenuObject("微博");
        shareWeibo.setResource(R.mipmap.ic_share_weibo);

        menuObjects.add(close);
        menuObjects.add(shareQQ);
        menuObjects.add(shareQZone);
        menuObjects.add(shareFriend);
        menuObjects.add(shareWchat);
        menuObjects.add(shareWeibo);
        return menuObjects;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comm, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_common);
        menuItem.setTitle("分享");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_common:
                mMenuDialogFragment.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {

        }
    }
}
