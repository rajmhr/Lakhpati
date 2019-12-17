package com.lakhpati.Services;

import com.lakhpati.models.GroupMembersViewModel;
import com.lakhpati.models.LotteryUserGroupViewModel;
import com.lakhpati.models.Pagination.CampaignGroupPaginationRequestModel;
import com.lakhpati.models.Pagination.ChatGroupPaginationRequestModel;
import com.lakhpati.models.PurchaseTicketViewModel;
import com.lakhpati.models.ReturnModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LotteryGroupApiInterface {

    @POST("GroupMembers/GetGroupMembers")
    Call<ReturnModel> getGroupMembers(@Body int groupId);

    @POST("GroupMembers/AssignAdmin")
    Call<ReturnModel> assignAdmin(@Body List<GroupMembersViewModel> model);

    @POST("GroupMembers/FindUserByEmailId")
    Call<ReturnModel> findUserByEmailId(@Body LotteryUserGroupViewModel groupId);

    @POST("GroupMembers/AddUserToGroup")
    Call<ReturnModel> addUserToGroup(@Body LotteryUserGroupViewModel model);

    @POST("GroupMembers/GetGroupChatMessage")
    public Call<ReturnModel> getGroupChatMessage(@Body ChatGroupPaginationRequestModel model);
}
