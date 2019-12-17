package com.lakhpati.Services;

import com.lakhpati.models.LoginModel;
import com.lakhpati.models.Pagination.NotificationPaginationRequestModel;
import com.lakhpati.models.ReturnModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserMessageApiInterface {
    @POST("Notification/GetAllNotification")
    Call<ReturnModel> getAllNotification(@Body NotificationPaginationRequestModel model);

    @POST("Notification/ClearAllNotificationByUserId")
    Call<ReturnModel> clearAllNotificationByUserId(@Body int userDetailId);

}
