package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.fragment.CommonFragment;
import com.shosen.max.ui.fragment.HomePageFragment;
import com.shosen.max.ui.fragment.MineFragment;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePageActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.home_container)
    FrameLayout homeContainer;
    @BindView(R.id.bottom_tab)
    TabLayout bottomTab;


    private Fragment[] mFragmensts;

    private int currentFragmentPos = 0;


    @Override
    protected int getContentViewID() {
        return R.layout.activity_home_page;
    }


    @Override
    protected void initViewAndEvents() {
        LoginUtils.getUser();//初始化
        mFragmensts = DataGenerator.getFragments("test");
        loadFragments();
        bottomTab.addOnTabSelectedListener(this);
        for (int i = 0; i < 4; i++) {
            bottomTab.addTab(bottomTab.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        onTabItemSelected(tab.getPosition());
        tab.getCustomView().setSelected(true);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getCustomView().setSelected(false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void onTabItemSelected(int position) {
        if (currentFragmentPos != position && mFragmensts[currentFragmentPos] != null) {
            getSupportFragmentManager().beginTransaction().
                    hide(mFragmensts[currentFragmentPos]).show(mFragmensts[position]).commitAllowingStateLoss();

        }
        currentFragmentPos = position;
    }

    /**
     * 转到首页
     */
    public void changeToHomePage() {
        if (currentFragmentPos != 0) {
            getSupportFragmentManager().beginTransaction().
                    hide(mFragmensts[currentFragmentPos]).show(mFragmensts[0]).commitAllowingStateLoss();
        }
        bottomTab.getTabAt(currentFragmentPos).getCustomView().setSelected(false);
        bottomTab.getTabAt(0).getCustomView().setSelected(true);
        currentFragmentPos = 0;
    }


    /**
     * 初始化fragments
     */
    public void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : mFragmensts) {
            transaction.add(R.id.home_container, fragment);
            if (fragment instanceof HomePageFragment) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentFragmentPos == 2) {
                CommonFragment fragment = (CommonFragment) mFragmensts[currentFragmentPos];
                fragment.back();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mFragmensts != null) {
            MineFragment mineFragment = (MineFragment) mFragmensts[3];
            mineFragment.refreshUI();
        }
    }
}
