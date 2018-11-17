package com.shosen.max.bean;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.shosen.max.utils.SpanUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class CommentBean {

    public static final int COMMENT_TYPE = 100;//是评论

    public static final int REPLY_TYPE = 101;//是回复

    public static final int REPLY_TYPE_TO_COMMENT = 201;//回复对评论

    public static final int REPLY_TYPE_TO_REPLY = 202;//回复对回复

    private String id; //1
    private String parentId; //
    private String messId; //1
    private String userId; //3
    private String content; //今天星期一;,
    private String conStatus; //;,
    private String createTime; //1539568301563;,
    private String name; //啊木;,
    private String headImg; //http://api.shosen.cn:8080/file/download/a822f07fd5664e2aaf2901ff641bbb3e.png;,
    private String parentUserId;
    private String parentRootId = "";//自己设置 根评论的时候为空
    private String parentName;//只有是REPLY_TYPE存在
    private String markStatus;

    private List<CommentBean> commontList;

    public String getParentRootId() {
        return parentRootId;
    }

    public void setReplysRoot() {
        if (getCommentOrReplayType() != COMMENT_TYPE) {
            return;
        }
        if (commontList == null || commontList.size() == 0) {
            return;
        }
        //根节点 设置子集的ParentRootId
        for (CommentBean replayBean : commontList) {
            replayBean.setParentRootId(this.id);
        }
    }


    /**
     * @return 判断属于评论还是回复
     */
    public int getCommentOrReplayType() {
        //如果parentUserId为空则为根评论 或者 parentId为0的时候
        if (TextUtils.isEmpty(parentUserId) || "0".equals(parentId)) {
            parentRootId = "";
            return COMMENT_TYPE;
        } else {
            return REPLY_TYPE;
        }
    }

    /**
     * 判断回复的类型
     *
     * @return
     */
    public int getReplyType() {
        if (getCommentOrReplayType() == COMMENT_TYPE) {
            //为评论
            return -1;
        }
        /**
         * 若parentRootId equals parentId是对评论的回复 否则是对回复的回复
         */
        if (parentRootId.equals(parentId)) {
            return REPLY_TYPE_TO_COMMENT;
        } else {
            return REPLY_TYPE_TO_REPLY;
        }
    }


    /**
     * 富文本内容
     */
    private SpannableStringBuilder commentContentSpan;

    public SpannableStringBuilder getCommentContentSpan() {
        return commentContentSpan;
    }

    /**
     * build 回复
     *
     * @param context
     */
    public void build(Context context) {
        if (getReplyType() == REPLY_TYPE_TO_COMMENT) {
            commentContentSpan = SpanUtils.makeSingleCommentSpan(context, name, content);
        } else if (getReplyType() == REPLY_TYPE_TO_REPLY) {
            commentContentSpan = SpanUtils.makeReplyCommentSpan(context, name, parentName, content);
        }
        setReplysRoot();
    }


    // GETER SETER START
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConStatus() {
        return conStatus;
    }

    public void setConStatus(String conStatus) {
        this.conStatus = conStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<CommentBean> getCommontList() {
        return commontList;
    }

    public void setCommontList(List<CommentBean> commontList) {
        this.commontList = commontList;
    }

    public void setParentRootId(String parentRootId) {
        this.parentRootId = parentRootId;
    }

    public String getMarkStatus() {
        return markStatus;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }
    // GETER SETER END

}
