package com.shosen.max.bean;

public class RewardListBean {

    private String userPhone;//13366100567;,
    private String reward;
    private String rewardRemark;
    private String invitSum;
    private String createTime;
    private String updateTime;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRewardRemark() {
        return rewardRemark;
    }

    public void setRewardRemark(String rewardRemark) {
        this.rewardRemark = rewardRemark;
    }

    public String getInvitSum() {
        return invitSum;
    }

    public void setInvitSum(String invitSum) {
        this.invitSum = invitSum;
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
}
