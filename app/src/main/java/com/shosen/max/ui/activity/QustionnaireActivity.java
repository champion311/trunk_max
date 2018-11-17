package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.QuestionnaireBean;
import com.shosen.max.presenter.QuestionnairePresenter;
import com.shosen.max.presenter.contract.QuestionnaireContract;
import com.shosen.max.ui.adapter.QuestionnaireAdapter;
import com.shosen.max.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QustionnaireActivity extends BaseActivity implements QuestionnaireContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.rc_questions)
    RecyclerView rcQuestions;

    private QuestionnairePresenter mPresenter;

    private QuestionnaireAdapter mAdapter;

    private List<QuestionnaireBean> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new QuestionnairePresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("问卷调查");
        mData = new ArrayList<>();
        mAdapter = new QuestionnaireAdapter(this, mData);
        rcQuestions.setLayoutManager(new LinearLayoutManager(this));
        rcQuestions.setAdapter(mAdapter);
        mPresenter.getData();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_questionnaire;
    }

    @Override
    public void showQuestionData(List<QuestionnaireBean> mData) {
        if (mData != null) {
            this.mData.addAll(mData);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrorMessage(String message) {

    }
}
