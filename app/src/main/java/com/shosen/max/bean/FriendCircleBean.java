package com.shosen.max.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendCircleBean implements Parcelable {
    private String id; //1,
    private String userId; //12,
    private String content; //今天星期一;,
    private String name; //;,
    private String headImg; //;,
    private String picture; //http://www.baidu.com;,
    private String messStatus;//1,
    private String createTime; //1539567613651;,
    private String location; //Pw==;,
    private String comCount; //2;,
    private String markCount; //0;
    private String markStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMessStatus() {
        return messStatus;
    }

    public void setMessStatus(String messStatus) {
        this.messStatus = messStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComCount() {
        return comCount;
    }

    public void setComCount(String comCount) {
        this.comCount = comCount;
    }

    public String getMarkCount() {
        return markCount;
    }

    public void setMarkCount(String markCount) {
        this.markCount = markCount;
    }

    public String getMarkStatus() {
        return markStatus;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(content);
        dest.writeString(name);
        dest.writeString(headImg);
        dest.writeString(picture);
        dest.writeString(messStatus);
        dest.writeString(createTime);
        dest.writeString(location);
        dest.writeString(comCount);
        dest.writeString(markCount);
        dest.writeString(markStatus);

    }

    public static final Parcelable.Creator<FriendCircleBean> CREATOR = new Parcelable.Creator<FriendCircleBean>() {
        @Override
        public FriendCircleBean createFromParcel(Parcel source) {
            return new FriendCircleBean(source);
        }

        @Override
        public FriendCircleBean[] newArray(int size) {
            return new FriendCircleBean[size];
        }
    };

    public FriendCircleBean(Parcel source) {
        this.id = source.readString();
        this.userId = source.readString();
        this.content = source.readString();
        this.name = source.readString();
        this.headImg = source.readString();
        this.picture = source.readString();
        this.messStatus = source.readString();
        this.createTime = source.readString();
        this.location = source.readString();
        this.comCount = source.readString();
        this.markCount = source.readString();
        this.markStatus = source.readString();
    }

    //Test Only
    public FriendCircleBean(String id) {
        this.id = id;
    }


    public FriendCircleBean(MyRessMessBean bean) {
        //this.id=bean.get
        this.id = bean.getMessId();
        this.userId = bean.getUserId();
        this.picture = bean.getPicture();
        this.name = bean.getUserName();
        this.content = bean.getContent();
        this.headImg = bean.getHeadImg();
        this.createTime = bean.getCreateTime();
        this.content = bean.getContent();

    }
}
