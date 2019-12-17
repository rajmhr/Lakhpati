package com.lakhpati.Services;

import com.lakhpati.models.GroupMembersViewModel;
import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.LotteryUserGroupViewModel;
import com.lakhpati.models.PurchaseTicketViewModel;
import com.lakhpati.models.ReturnModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyTicketsApiInterface {
    @POST("MyTickets/GetAllUserTicketsByGroupCampaignId")
    Call<ReturnModel> getAllUserTicketsByGroupCampaignId(@Body int groupCampaignId);

    @POST("MyTickets/GetMyTicketByGroupCampaignId")
    Call<ReturnModel> getMyTicketByGroupCampaignId(@Body LotteryGroupCampaignModel model);

    @POST("MyTickets/BuyLotteryGroupTickets")
    Call<ReturnModel> buyLotteryGroupTickets(@Body PurchaseTicketViewModel model);

    @POST("LotteryTicket/GetGeneratedTickets")
    Call<ReturnModel> getGeneratedTickets(@Body String noOfTicket);

}
