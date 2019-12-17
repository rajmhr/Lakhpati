package com.lakhpati.Services;

import com.lakhpati.models.LoginModel;
import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.LuckyDrawViewModel;
import com.lakhpati.models.Pagination.CampaignGroupPaginationRequestModel;
import com.lakhpati.models.PurchaseTicketViewModel;
import com.lakhpati.models.ReturnModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GroupCampaignApiInterface {

    @POST("GroupCampaign/StopGroupCampaign")
    Call<ReturnModel> stopGroupCampaign(@Body LotteryGroupCampaignModel model);

    @POST("LuckyDraw/StartDraw")
    Call<ReturnModel> startDraw(@Body LuckyDrawViewModel model);

    @POST("GroupCampaign/SaveGroupCampaign")
    Call<ReturnModel> saveGroupCampaign(@Body LotteryGroupCampaignModel model);

    @POST("GroupCampaign/DeleteGroupCampaign")
    Call<ReturnModel> deleteGroupCampaign(@Body LotteryGroupCampaignModel model);

    @POST("GroupCampaign/StartGroupCampaign")
    Call<ReturnModel> startGroupCampaign(@Body int groupCampaignId);

    @POST("GroupCampaign/GetGroupCampaignDetailById")
    Call<ReturnModel> getGroupCampaignDetailById(@Body LotteryGroupCampaignModel model);

}
