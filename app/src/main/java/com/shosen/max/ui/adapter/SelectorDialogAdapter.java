package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.widget.dialog.SelectorDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectorDialogAdapter extends RecyclerView.Adapter<SelectorDialogAdapter.SelectorHolder> {


    private List<DictionaryBean> mData;

    private Context mContext;

    public SelectorDialogAdapter(List<DictionaryBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    private SelectorDialog.SelectorClick mClick;


    public void setClick(SelectorDialog.SelectorClick mClick) {
        this.mClick = mClick;
    }

    @NonNull
    @Override
    public SelectorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_selector, viewGroup, false);
        return new SelectorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorHolder selectorHolder, int i) {
        DictionaryBean data = mData.get(i);
        selectorHolder.tvTitle.setText(data.getDicValue());
        selectorHolder.itemView.setOnClickListener(v -> {
            if (mClick != null) {
                mClick.selectorClick(data);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class SelectorHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.view_separtor)
        View viewSepartor;

        public SelectorHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
