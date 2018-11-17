package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.GridBean;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemGridAdapter extends RecyclerView.Adapter<ItemGridAdapter.ViewHolder> {


    private List<DictionaryBean> data;

    private String hasSelectedData;

    private Context mContext;

    private int selectedCount = 0;

    //Separator

    public static final String SEPARATOR = ",";


    public ItemGridAdapter(Context mContext, List<DictionaryBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public ItemGridAdapter(Context mContext, List<DictionaryBean> data, String hasSelectedData) {
        this.mContext = mContext;
        this.data = data;
        if (!TextUtils.isEmpty(hasSelectedData)) {
            String[] arrays = hasSelectedData.split(SEPARATOR);
            for (String str : arrays) {
                for (DictionaryBean bean : data) {
                    if (!TextUtils.isEmpty(str) && str.equals(bean.getDicCode())) {
                        bean.setSelected(true);
                    }
                }
            }
            selectedCount = getSelectedCount();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_button, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DictionaryBean bean = data.get(i);
        viewHolder.mTextContent.setText(data.get(i).getDicValue());
        viewHolder.itemView.setSelected(data.get(i).isSelected());
        viewHolder.itemView.setOnClickListener(v -> {
            if (selectedCount == 3 && !bean.isSelected()) {
                ToastUtils.show(mContext, "最多选择三个标签");
                return;
            }
            if (bean.isSelected()) {
                bean.setSelected(false);
                selectedCount--;
            } else {
                bean.setSelected(true);
                selectedCount++;
            }
            notifyItemChanged(i);
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mContexnt)
        TextView mTextContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public String getSelectedData() {
        StringBuffer sb = new StringBuffer();
        for (DictionaryBean bean : data) {
            if (bean.isSelected()) {
                if (sb.length() != 0) {
                    sb.append(SEPARATOR);
                }
                sb.append(bean.getDicCode());
            }
        }
        return sb.toString().trim();
    }

    public int getSelectedCount() {
        int ret = 0;
        for (DictionaryBean bean : data) {
            if (bean.isSelected()) {
                ret++;
            }
        }
        return ret;
    }
}


