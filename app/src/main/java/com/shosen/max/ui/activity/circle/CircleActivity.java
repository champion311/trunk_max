package com.shosen.max.ui.activity.circle;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.presenter.CirclePresenter;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.activity.circle.adapter.CircleFragmentAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class CircleActivity extends BaseActivity implements
        TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tl_top_tab)
    TabLayout tlTopTab;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.fa_button_submit)
    FloatingActionButton faButtonSubmit;

    public static boolean isForeground = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new CirclePresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        ivRight.setImageResource(R.drawable.user_icon);
        ivRight.setVisibility(View.VISIBLE);
        tlTopTab.addOnTabSelectedListener(this);
        tvHeadTitle.setText("圈子");
        for (int i = 0; i < 2; i++) {
            String text = "";
            switch (i) {
                case 0:
                    text = "圈子";
                    break;
                case 1:
                    text = "发现";
                    break;
            }
            tlTopTab.addTab(tlTopTab.newTab().setText(text));
        }
        vpContainer.setAdapter(new CircleFragmentAdapter(getSupportFragmentManager(),
                DataGenerator.getCircleFragments()));
        vpContainer.addOnPageChangeListener(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_circle_home;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        vpContainer.setCurrentItem(position);
        if (position == 0) {
            faButtonSubmit.setVisibility(View.VISIBLE);
        } else {
            faButtonSubmit.setVisibility(View.GONE);
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick({R.id.iv_back, R.id.iv_right, R.id.fa_button_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                ActivityUtils.startActivity(CircleUserMineActivity.class);
                break;
            case R.id.fa_button_submit:
                //TODO 发布状态
                ActivityUtils.startActivity(SubmitActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        tlTopTab.getTabAt(i).select();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
