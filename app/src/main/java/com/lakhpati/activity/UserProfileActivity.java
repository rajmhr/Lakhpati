package com.lakhpati.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lakhpati.R;
import com.lakhpati.Services.LotteryGroupApiInterface;
import com.lakhpati.Services.MyGroupApiInterface;
import com.lakhpati.Services.UserApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.customControl.ProgressLoader;
import com.lakhpati.models.Pagination.PrizeMoneyPaginationRequestModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserLotterySummaryModel;
import com.lakhpati.models.UserProfileViewModel;
import com.lakhpati.retrofit.RetrofitClientInstance;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    @BindView(R.id.txt_user_profile_name)
    TextView txt_user_profile_name;

    @BindView(R.id.txt_user_profile_email)
    TextView txt_user_profile_email;

    @BindView(R.id.txt_totalGamePlayed)
    TextView txt_totalGamePlayed;

    @BindView(R.id.txt_totalGameWon)
    TextView txt_totalGameWon;

    @BindView(R.id.txt_totalCoinWon)
    TextView txt_totalCoinWon;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setTitle("My Profile");
        alertDialog = Dialogs.getInstance().initLoaderDialog(this);
        getUserProfileData();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void getUserProfileData() {
        alertDialog.show();

        UserApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiInterface.class);

        Call<ReturnModel> callValue = userApiService.getUserProfileData(4);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    Gson gson = new GsonBuilder().create();
                    UserProfileViewModel userProfileViewModel = gson.fromJson(returnData, UserProfileViewModel.class);
                    _userDisplayName.setText(userProfileViewModel.getUserDisplayName());
                    _emailId.setText(userProfileViewModel.getEmailId());
                    _noOfGroups.setText("No of Groups :" + userProfileViewModel.NoOfGroups);
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }*/
    private void getUserProfileData() {
        alertDialog.show();
        MyGroupApiInterface lotteryGroupApiService = RetrofitClientInstance.getRetrofitInstance().create(MyGroupApiInterface.class);

        PrizeMoneyPaginationRequestModel model = new PrizeMoneyPaginationRequestModel();
        Call<ReturnModel> callValue = lotteryGroupApiService.getSummaryLotteryByUserId(DrawerActivity.userCommonModel.getUserDetailId());
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    UserLotterySummaryModel myGroupModel = HelperClass.getSingleModelFromJson(UserLotterySummaryModel.class, returnData);
                    displayData(myGroupModel);
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                alertDialog.cancel();
            }
        });
    }
    private void displayData(UserLotterySummaryModel myGroupModel) {

        txt_user_profile_name.setText(myGroupModel.getDisplayName());
        txt_user_profile_email.setText(myGroupModel.getEmailId());

        txt_totalGamePlayed.setText("Game played : " + myGroupModel.getTotalGamePlayed() + " times.");
        txt_totalGameWon.setText("Total game won : " + myGroupModel.getTotalGameWon() + " times.");
        txt_totalCoinWon.setText("You have won : " + myGroupModel.getTotalCoinWon() + " coins.");
    }
}
