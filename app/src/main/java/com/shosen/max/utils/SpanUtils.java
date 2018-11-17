package com.shosen.max.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.shosen.max.R;
import com.shosen.max.others.span.TextClickSpan;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class SpanUtils {


    public static SpannableStringBuilder
    makeColorSpan(Context mContext, String startStr, String endStr, String insertNumber) {
        if (startStr == null || insertNumber == null) {
            return null;
        }
        String text = startStr + insertNumber + endStr;
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.seleted_text_color));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(colorSpan, startStr.length(),
                startStr.length() + insertNumber.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder makeSingleCommentSpan(Context context, String childUserName, String commentContent) {
        String decodeContent = getUrlDecodeContent(commentContent);
        String richText = String.format("%s: %s", childUserName, decodeContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        if (!TextUtils.isEmpty(childUserName)) {
            builder.setSpan(new TextClickSpan(context, childUserName), 0, childUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static SpannableStringBuilder makeReplyCommentSpan(Context context, String parentUserName, String childUserName, String commentContent) {
        String decodeContent = getUrlDecodeContent(commentContent);
        String richText = String.format("%s 回复 %s: %s", parentUserName, childUserName, decodeContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        int parentEnd = 0;
        if (!TextUtils.isEmpty(parentUserName)) {
            parentEnd = parentUserName.length();
            builder.setSpan(new TextClickSpan(context, parentUserName), 0, parentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(childUserName)) {
            int childStart = parentEnd + 2;
            int childEnd = childStart + childUserName.length();
            builder.setSpan(new TextClickSpan(context, childUserName), childStart, childEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }


    public static String getUrlDecodeContent(String content) {
        if (content != null) {
            try {
                content = URLDecoder.decode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return content;
            }
        }
        return content;
    }

    public static String getUrlEncodeContent(String content) {
        if (content != null) {
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return content;
            }
        }
        return content;
    }
}
