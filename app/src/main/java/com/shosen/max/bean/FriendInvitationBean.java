package com.shosen.max.bean;

public class FriendInvitationBean {

    //"headImg":"db2f98340ef54b738fb5290e268f78b4.jpg","name":"这","id":"3"

    private String headImg;

    private String name;

    private String id;

    private String followerId;

    private String markStatus;

    //我的粉丝使用
    private String eachOther; //"0" 不是互相关注 "1"为互相关注

    private String userId;

    private String followedUser;

    private String createTime;

    private String updateTime;


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getMarkStatus() {
        return markStatus;
    }


    public String getEachOther() {
        return eachOther;
    }

    public void setEachOther(String eachOther) {
        this.eachOther = eachOther;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(String followedUser) {
        this.followedUser = followedUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }
}
