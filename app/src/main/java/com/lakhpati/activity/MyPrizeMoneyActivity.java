package com.lakhpati.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.LotteryGroupApiInterface;
import com.lakhpati.Services.MyGroupApiInterface;
import com.lakhpati.Services.UserApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.adapters.MyGroupAdapter;
import com.lakhpati.adapters.MyPrizeMoneyAdapter;
import com.lakhpati.customControl.ProgressLoader;
import com.lakhpati.models.LotteryGroupModel;
import com.lakhpati.models.Pagination.PaginationResponseModel;
import com.lakhpati.models.Pagination.PrizeMoneyPaginationRequestModel;
import com.lakhpati.models.PrizeMoneyModel;
import com.lakhpati.models.RelatedLotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserLotterySummaryModel;
import com.lakhpati.models.UserProfileViewModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPrizeMoneyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    @BindView(R.id.txt_totalGamePlayed)
    TextView txt_totalGamePlayed;

    @BindView(R.id.txt_totalCoinPlayed)
    TextView txt_totalCoinPlayed;

    @BindView(R.id.txt_totalGameWon)
    TextView txt_totalGameWon;

    @BindView(R.id.txt_totalCoinWon)
    TextView txt_totalCoinWon;

    @BindView(R.id.txt_totalCoinLost)
    TextView txt_totalCoinLost;

    @BindView(R.id.txt_TotalGameLost)
    TextView txt_TotalGameLost;

    AlertDialog alertDialog;
    MyPrizeMoneyAdapter myPrizeMoneyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_my_prize_money);
        ButterKnife.bind(this);

        alertDialog = Dialogs.getInstance().initLoaderDialog(this);
        getMyPrizeMoneyData();
    }

    private void getMyPrizeMoneyData() {
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
        txt_totalGamePlayed.setText("You have played : " + myGroupModel.getTotalGamePlayed() + " times.");
        txt_totalCoinPlayed.setText("Total coin played : " + myGroupModel.getTotalCoinPlayed() + " coins.");
        txt_totalCoinWon.setText("You have won : " + myGroupModel.getTotalCoinWon() + " times.");
        txt_totalGameWon.setText("Total coin earn : " + myGroupModel.getTotalGameWon() + " coins.");
        txt_totalCoinLost.setText("Total game played : " + myGroupModel.getTotalCoinLost() + " coins.");
        txt_TotalGameLost.setText("You have lost : " + myGroupModel.getTotalGameLost() + " times.");
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
