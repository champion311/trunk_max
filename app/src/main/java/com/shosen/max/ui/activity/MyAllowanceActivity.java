package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.RewardTotalResponse;
import com.shosen.max.presenter.MyAllowancePresenter;
import com.shosen.max.presenter.contract.MyAllowanceContract;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAllowanceActivity extends BaseActivity implements MyAllowanceContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_car_owner)
    TextView tvCarOwner;
    @BindView(R.id.tv_reward_permonth)
    TextView tvRewardPermonth;
    @BindView(R.id.tv_to_month)
    TextView tvToMonth;
    @BindView(R.id.tv_left_money)
    TextView tvLeftMoney;
    @BindView(R.id.tv_get_money)
    TextView tvGetMoney;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.tv_allo_intro)
    TextView tvAlloIntro;
    @BindView(R.id.tv_my_allowance_money)
    TextView tvMyAllowanceMoney;
    @BindView(R.id.tv_reward_permonth_print)
    TextView tvRewardPermonthPrint;
    @BindView(R.id.tv_to_month_print)
    TextView tvToMonthPrint;
    @BindView(R.id.tv_left_money_print)
    TextView tvLeftMoneyPrint;
    @BindView(R.id.tv_car_owner_print)
    TextView tvCarOwnerPrint;

    private MyAllowancePresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyAllowancePresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("我的津贴");
        setContentData();
        mPresenter.getMyAllowanceDetail();

    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_allowance;
    }

    @OnClick({R.id.iv_back, R.id.tv_get_money, R.id.tv_allo_intro, R.id.tv_my_allowance_money})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_money:
                break;
            case R.id.tv_allo_intro:
                startActivity(new Intent(this, MyAllowanceDetailActivity.class));
                break;
            case R.id.tv_my_allowance_money:
                startActivity(new Intent(this, MyAllowanceMoneyDetailsActivity.class));
                break;
            default:
                break;
        }
    }

    public void setContentData() {
        String text = getResources().getString(R.string.my_allowance_intro);
        SpannableString spannableString = new SpannableString(text);
        MineSpan colorSpan = new MineSpan(17, true);
        spannableString.setSpan(colorSpan, text.length() - 4, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAlloIntro.setText(spannableString);
    }


    @Override
    public void dataSuccess(RewardTotalResponse response) {
        if (response == null) {
            return;
        }
        //邀请人数
        tvCarOwnerPrint.setText(response.getInvitSum());
        //待发月份
        tvToMonthPrint.setText(response.getRewardMonth());
        //每月奖励
        tvRewardPermonthPrint.setText(response.getReward());
        //剩余值
        tvLeftMoneyPrint.setText(response.getRewardedMoney());


    }

    @Override
    public void dataError(String message) {
        //ToastUtils.show(this, message);
    }

    public class MineSpan extends AbsoluteSizeSpan {

        public MineSpan(int size) {
            super(size);
        }

        public MineSpan(int size, boolean dip) {
            super(size, dip);
        }


        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.black_text_color));
        }
    }

}
