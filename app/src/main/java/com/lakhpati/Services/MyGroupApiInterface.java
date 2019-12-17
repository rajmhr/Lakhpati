package com.lakhpati.Services;

import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.PurchaseTicketViewModel;
import com.lakhpati.models.ReturnModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyGroupApiInterface {
    @POST("MyGroup/GetGroupInfoById")
    Call<ReturnModel> getGroupInfoById(@Body LotteryGroupModel model);

    @POST("MyGroup/SaveMyGroup")
    Call<ReturnModel> createMyGroup(@Body LotteryGroupModel model);

    @POST("MyGroup/GetUserRelatedGroups")
    Call<ReturnModel> getAllMyGroup(@Body int userDetailId);

    @POST("MyGroup/DeleteGroup")
    Call<ReturnModel> deleteGroup(@Body int groupId);

    @POST("MyGroup/DeleteInviteFriend")
    Call<ReturnModel> leaveGroup(@Body int userGroupId);

    @POST("MyGroup/GetSummaryLotteryByUserId")
    Call<ReturnModel> getSummaryLotteryByUserId(@Body int userDetailId);

}
