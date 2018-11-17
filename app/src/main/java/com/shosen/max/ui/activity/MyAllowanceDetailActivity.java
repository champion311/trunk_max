package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.TableBean;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAllowanceDetailActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("详情");
        initTableData();
        setContentData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_allowance_detail;
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void initTableData() {
        table.getConfig().setTableTitleStyle(
                new FontStyle(DensityUtils.dip2px(this, 13), getResources().getColor(R.color.seleted_text_color)));
        table.getConfig().setContentStyle(new FontStyle(DensityUtils.dip2px(this, 13), getResources().getColor(R.color.black_text_color)));
        //普通列
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowTableTitle(false);
        table.getConfig().setColumnTitleStyle
                (new FontStyle(DensityUtils.dip2px(this, 13), getResources().getColor(R.color.seleted_text_color)));

        Column<String> column1 = new Column<>("日期", "date");

        Column<String> column2 = new Column<>("好友名称", "friendName");

        Column<String> column3 = new Column<>("联系方式", "contactMethod");

        Column<String> column4 = new Column<>("已支付金额 ", "payMoney");

        List<TableBean> tableBeanList = new ArrayList<>();
        TableBean bean = new TableBean("2018-08",
                "迈凯思", "联系方式", "支付金额");
        tableBeanList.add(bean);
        TableData<TableBean> tableData = new TableData<TableBean>("allowance_table", tableBeanList, column1, column2, column3, column4);
        table.setTableData(tableData);
    }


    /**
     * 设置文字内容
     */
    public void setContentData() {
        if (tvContent == null) {
            return;
        }
        String[] counts = getResources().getStringArray(R.array.counts);
        String[] moneys = getResources().getStringArray(R.array.moneys);
        String[] months = getResources().getStringArray(R.array.months);
        SpannableString spannableString;
        MineSpan mineSpan;
        for (int i = 0; i < counts.length; i++) {
            String text
                    = "分享" + counts[i] + "台，车补" + moneys[i] + "元/月，最高可领" + months[i] + "\n";
            spannableString = new SpannableString(text);
            mineSpan = new MineSpan(15 + i, true);
            spannableString.setSpan(mineSpan, 7, 7 + moneys.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.append(spannableString);
        }
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
            ds.setColor(getResources().getColor(R.color.seleted_text_color));
        }
    }


}
