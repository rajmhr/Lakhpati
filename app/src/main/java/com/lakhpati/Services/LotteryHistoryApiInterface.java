package com.lakhpati.Services;

import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.Pagination.CampaignGroupPaginationRequestModel;
import com.lakhpati.models.ReturnModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LotteryHistoryApiInterface {
    @POST("LotteryHistory/GetGroupLotteryHistory")
    Call<ReturnModel> getLotteryHistory(@Body CampaignGroupPaginationRequestModel model);

}
