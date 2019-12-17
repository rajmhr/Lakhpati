package com.lakhpati.retrofit;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lakhpati.Services.Interceptor.NetworkConnectionInterceptor;
import com.lakhpati.Services.InternetConnectionListener;
import com.lakhpati.Utilities.CheckConnection;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance extends Application {

    private static Retrofit retrofit;
    public static final String BASE_URL = "http://192.168.100.101/Lakhpati.Api/api/";
    public static final String BASE_HUB_URL = "http://192.168.100.101/Lakhpati.Api/";
    /*public static final String BASE_URL = "http://api-lakhpati.kbits.com.np/api/";
    public static final String BASE_HUB_URL = "http://api-lakhpati.kbits.com.np/";*/

    private static InternetConnectionListener mInternetConnectionListener;
    private static RetrofitClientInstance instance;

    public static RetrofitClientInstance getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public void setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }

    public static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String s) {
                Log.d("inter", s);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        NetworkConnectionInterceptor ntInterceptor = new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return RetrofitClientInstance.isInternetAvailable(getContext());
            }

            @Override
            public void onInternetUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addInterceptor(ntInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)

                    .build();
        }
        return retrofit;
    }

    private static boolean isInternetAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
