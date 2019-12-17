package com.lakhpati.Services;

import com.lakhpati.models.LoginModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.Pagination.PrizeMoneyPaginationRequestModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserRegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApiInterface {

    @POST("User/LoginUser")
    Call<ReturnModel> login(@Body LoginModel model);

    @POST("User/GetVerificationCode")
    Call<ReturnModel> getVerificationCode(@Body UserRegisterModel model);

    @POST("User/LoginWithCode")
    Call<ReturnModel> loginWithCode(@Body LoginModel model);

    @POST("User/RegisterUserPublic")
    Call<ReturnModel> registerUserPublic(@Body UserRegisterModel model);


    @POST("UserProfile/GetUserProfile")
    Call<ReturnModel> getUserProfileData(@Body int userDetailId);


}
