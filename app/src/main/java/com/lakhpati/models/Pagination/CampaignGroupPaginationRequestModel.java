package com.lakhpati.models.Pagination;

public class CampaignGroupPaginationRequestModel extends PaginationRequestModel {
    private int groupId;
    private int userDetailId;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int setUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        this.userDetailId = userDetailId;
    }


}
