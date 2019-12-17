package com.lakhpati.Utilities;

import android.app.Activity;
import android.os.AsyncTask;

import com.lakhpati.Services.GroupCampaignApiInterface;
import com.lakhpati.Services.MyGroupApiInterface;
import com.lakhpati.Services.MyTicketsApiInterface;
import com.lakhpati.activity.DrawerActivity;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;
import com.lakhpati.models.LotteryGroupCampaignModel;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.RelatedLotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

public class HelperAsyncClass {

    public class RetrieveGroupInfoTask extends AsyncTask<LotteryGroupModel, Void, String> {

        @Override
        protected String doInBackground(LotteryGroupModel... params) {
            MyGroupApiInterface lotteryGroupCampaignApi = RetrofitClientInstance.getRetrofitInstance().create(MyGroupApiInterface.class);

            Call<ReturnModel> callValue = lotteryGroupCampaignApi.getGroupInfoById(params[0]);
            String resultData = "";
            try {
                Response<ReturnModel> response = callValue.execute();
                if (response.isSuccessful()) {
                    resultData = response.body().getReturnData();
                } else {
                    resultData = "Error";
                }

            } catch (IOException e) {
                resultData = "Error";
            }
            return resultData;
        }
    }


    public static LotteryGroupCampaignDetailModel loadGroupInfoByGroupId(LotteryGroupModel lmodel, Activity activity) {
        LotteryGroupCampaignDetailModel model = null;
        try {
            String result = new HelperAsyncClass().new RetrieveGroupInfoTask().execute(lmodel).get();
            if (result == "Error") {
                MessageDisplay.getInstance().showSuccessToast("Please check your connectivity. Cannot load data.", activity);
            } else {
                model = HelperClass.getSingleModelFromJson(LotteryGroupCampaignDetailModel.class, result);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return model;
    }
}
