package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.QuestionnaireBean;
import com.shosen.max.presenter.contract.QuestionnaireContract;
import com.shosen.max.utils.FileUtils;

import java.util.List;

public class QuestionnairePresenter extends RxPresenter<QuestionnaireContract.View> implements
        QuestionnaireContract.Presenter {

    private Context mContext;

    public QuestionnairePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void getData() {
        String data = FileUtils.getFakeData(mContext, "questionnaire.json");
        if (!TextUtils.isEmpty(data)) {
            List<QuestionnaireBean> ret = new Gson().fromJson(data, new
                    TypeToken<List<QuestionnaireBean>>() {
                    }.getType());
            if (ret != null) {
                if (mView != null) {
                    mView.showQuestionData(ret);
                } else {
                    mView.showErrorMessage("json error");
                }
            }
        } else {
            if (mView != null) {
                mView.showErrorMessage("error");
            }
        }

    }
}
