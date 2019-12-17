package com.lakhpati.Services;

import com.lakhpati.models.LoginModel;
import com.lakhpati.models.MyValues;
import com.lakhpati.models.ReturnModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.*;

public interface ApiInterface {

    @POST("User/Login")
    Call<ReturnModel> login(@Field("lmodel") LoginModel model);

    @GET("values/ArrayString")
    Call<List<MyValues>> getValue();

    @GET("values/SingleString")
    Call<String> getValueById(@Query("id") int a);

    @POST("values/PostUser")
    Call<List<MyValues>> PostUser(@Body LoginModel values);
}