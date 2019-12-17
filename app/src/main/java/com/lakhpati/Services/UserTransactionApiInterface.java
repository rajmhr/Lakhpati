package com.lakhpati.Services;

import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.Pagination.CoinHistoryPaginationModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserCoinsModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserTransactionApiInterface {
    @POST("UserCoins/AddCoinToUser")
    Call<ReturnModel> addCoinToUser(@Body UserCoinsModel model);

    @POST("UserCoins/TransferCoinToUser")
    Call<ReturnModel> transferCoinToUser(@Body UserCoinsModel model);

    @POST("UserCoins/GetCoinTransactionHistory")
    Call<ReturnModel> getCoinTransactionHistory(@Body CoinHistoryPaginationModel model);


    @POST("UserCoins/ViewTransactionHistoryByUser")
    Call<ReturnModel> viewTransactionHistoryByUser(@Body UserCoinsModel model);

    @POST("UserCoins/GetAvailableCoinsByUserId")
    Call<ReturnModel> getAvailableCoinsByUserId(@Body UserCoinsModel model);
}
