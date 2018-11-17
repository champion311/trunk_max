package com.shosen.max.bean;

public class RewardTotalResponse {
//    "data":
//
//    {
//        "userPhone":"13366100567", "rewardedMoney":8000, "invitSum":2, "reward":
//        "2000", "rewardMonth":4, "rewardRemark":"", "createTime":"1539163028", "updateTime":
//        "1539163028", "id":32
//    }

    private String userPhone;

    private String rewardedMoney;

    private String invitSum;

    private String reward;

    private String rewardMonth;

    private String rewardRemark;

    private String createTime;

    private String updateTime;

    private String id;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRewardedMoney() {
        return rewardedMoney;
    }

    public void setRewardedMoney(String rewardedMoney) {
        this.rewardedMoney = rewardedMoney;
    }

    public String getInvitSum() {
        return invitSum;
    }

    public void setInvitSum(String invitSum) {
        this.invitSum = invitSum;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRewardMonth() {
        return rewardMonth;
    }

    public void setRewardMonth(String rewardMonth) {
        this.rewardMonth = rewardMonth;
    }

    public String getRewardRemark() {
        return rewardRemark;
    }

    public void setRewardRemark(String rewardRemark) {
        this.rewardRemark = rewardRemark;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
