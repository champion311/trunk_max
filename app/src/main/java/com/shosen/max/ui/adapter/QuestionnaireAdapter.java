package com.shosen.max.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.QuestionnaireBean;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.widget.MultiRadioGroup;

import java.util.List;

import butterknife.BindView;
import retrofit2.http.PATCH;

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireHolder> {


    private Context mContext;

    private List<QuestionnaireBean> mData;

    public static final int SINGLE = 0;

    public static final int MULTI = 1;

    public QuestionnaireAdapter(Context mContext, List<QuestionnaireBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public QuestionnaireHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = null;
        if (getItemViewType(position) == SINGLE) {
            view = View.inflate(mContext, R.layout.item_questionnaire, null);
        } else if (getItemViewType(position) == MULTI) {
            view = View.inflate(mContext, R.layout.item_question_muti, null);
        }
        return new QuestionnaireHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireHolder holder, int position) {
        QuestionnaireBean bean = mData.get(position);
        holder.tvTitle.setText(bean.getTitle());
        if (getItemViewType(position) == SINGLE) {
            addSingleType(holder.raMuti, bean);
        }

    }

    public void addSingleType(MultiRadioGroup radioGroup, QuestionnaireBean bean) {
        int spanCount = bean.getSpanCount();
        LinearLayout linearLayout = null;
        for (int i = 0; i < bean.getData().size(); i++) {
            if ((i + 1) % spanCount == 0) {
                linearLayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(params);
                radioGroup.addView(linearLayout);
            }
            if (linearLayout != null) {
                linearLayout.addView(getRadioButton(bean.getData().get(i)));
            }
        }
    }

    public RadioButton getRadioButton(String text) {
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setTextColor(ContextCompat.getColor(mContext, R.color.black_text_color));
        radioButton.setTextSize(15);
        radioButton.setText(text);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.questionaire_single);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        radioButton.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 7));
        radioButton.setCompoundDrawables(drawable, null, null, null);
        return radioButton;

    }


    @Override
    public int getItemViewType(int position) {
        QuestionnaireBean bean = mData.get(position);
        if (bean.getType() == 0) {
            return SINGLE;
        } else if (bean.getType() == 1) {
            return MULTI;
        }
        return SINGLE;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class QuestionnaireHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.ra_muti)
        MultiRadioGroup raMuti;

        @Nullable
        @BindView(R.id.ll_check_box)
        LinearLayout llCheckBox;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public QuestionnaireHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
