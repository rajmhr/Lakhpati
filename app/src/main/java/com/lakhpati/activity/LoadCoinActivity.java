package com.lakhpati.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.lakhpati.R;
import com.lakhpati.Services.UserTransactionApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserCoinsModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.checkOut.helper.KhaltiCheckOut;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadCoinActivity extends AppCompatActivity {

    final String khaltiKey = "test_public_key_2456ee2c6deb4d378a50831584fd54dc";
    Config config;

    @BindView(R.id.editText_loadCoin)
    TextInputEditText editText_loadCoin;

    @BindView(R.id.editText_transferTo)
    TextInputEditText editText_transferTo;

    @BindView(R.id.editText_transferCoin)
    TextInputEditText editText_transferCoin;

    @BindView(R.id.khalti_button)
    AppCompatButton khalti_button;

    @BindView(R.id.btn_transfer_coin)
    MaterialButton btn_transfer_coin;

    @BindView(R.id.txt_available_coins)
    TextView txt_available_coins;

    @BindView(R.id.pullToRefresh_loadCoin)
    SwipeRefreshLayout pullToRefresh_loadCoin;

    @BindView(R.id.layout_buySell)
    CoordinatorLayout layout_buySell;

    AlertDialog alertDialog;

    double availableCoins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_coin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Load and Transfer");

        ButterKnife.bind(this);

        alertDialog = Dialogs.getInstance().initLoaderDialog(this);

        loadCoins();
        long amount = 1000;
        config = new Config(khaltiKey, "Product ID", "Product Name", "Product Url", amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Log.i("Payment confirmed", data + "");
                addCoinToUserAccount();
            }

            @Override
            public void onError(String action, String message) {
                Log.i(action, message);
            }
        });
        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        khalti_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*config.setAmount(Long.parseLong(editText_loadCoin.getText().toString()) * 100);
                config.setProductId("4");
                config.setProductName("Nep-La");
                config.setProductUrl("No url");
                khaltiCheckOut.show();*/
                if (validateCoinLoad())
                    addCoinToUserAccount();
            }
        });

        btn_transfer_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCoinTransfer())
                    transferCoin();
            }
        });

        editText_loadCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCoinLoad();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_transferTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCoinTransfer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_transferCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCoinTransfer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pullToRefresh_loadCoin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCoins();
                pullToRefresh_loadCoin.setRefreshing(false);
            }
        });
    }

    private boolean validateCoinLoad() {
        boolean isValid = true;
        if (editText_loadCoin.getText().toString().equals("")) {
            editText_loadCoin.setError("Please enter coin to load.");
            isValid = false;
        }
        return isValid;
    }

    private boolean validateCoinTransfer() {
        boolean isValid = true;
        if (editText_transferTo.getText().toString().equals("")) {
            editText_transferTo.setError("Please enter email id to transfer.");
            isValid = false;
        } else if (editText_transferCoin.getText().toString().equals("")) {
            editText_transferCoin.setError("Please enter coin to transfer.");
            isValid = false;
        }
        if (isValid) {
            isValid = HelperClass.isEmailValid(editText_transferTo.getText().toString());
            if(!isValid){
                editText_transferTo.setError("Please enter valid email address.");
            }
        }
        return isValid;
    }

    private void addCoinToUserAccount() {
        alertDialog.show();

        UserTransactionApiInterface userTransactionApi = RetrofitClientInstance.getRetrofitInstance().create(UserTransactionApiInterface.class);

        UserCoinsModel model = new UserCoinsModel();
        model.setCoins(Long.parseLong(editText_loadCoin.getText().toString()));
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());

        Call<ReturnModel> callValue = userTransactionApi.addCoinToUser(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    Double loadedCoin = Double.parseDouble(editText_loadCoin.getText().toString()) + availableCoins;
                    availableCoins = loadedCoin;
                    txt_available_coins.setText("Available coins : " + loadedCoin.toString());
                    MessageDisplay.getInstance().showSuccessToast("Coin loaded.", getApplication());
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getApplication());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }

    private void transferCoin() {

        if(DrawerActivity.userCommonModel.getEmailId().trim().equals(editText_transferTo.getText().toString().trim())){
            editText_transferTo.setError("You cannot make transfer coin to yourself.");
            return;
        }

        alertDialog.show();

        UserTransactionApiInterface userTransactionApi = RetrofitClientInstance.getRetrofitInstance().create(UserTransactionApiInterface.class);

        UserCoinsModel model = new UserCoinsModel();
        model.setCoins(Long.parseLong(editText_transferCoin.getText().toString()));
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());
        model.setToUser(editText_transferTo.getText().toString());
        model.setFromUser(DrawerActivity.userCommonModel.getEmailId());

        Call<ReturnModel> callValue = userTransactionApi.transferCoinToUser(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();

                if (response.body().isSuccess()) {
                    MessageDisplay.getInstance().showSuccessToast(message, getApplication());
                    Double loadedCoin = availableCoins - Double.parseDouble(editText_transferCoin.getText().toString());
                    availableCoins = loadedCoin;
                    txt_available_coins.setText("Available coins : " + loadedCoin.toString());
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getApplication());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }

    private void loadCoins() {
        alertDialog.show();

        UserTransactionApiInterface userTransactionApi = RetrofitClientInstance.getRetrofitInstance().create(UserTransactionApiInterface.class);

        UserCoinsModel model = new UserCoinsModel();
        model.setUserDetailId(DrawerActivity.userCommonModel.getUserDetailId());

        Call<ReturnModel> callValue = userTransactionApi.getAvailableCoinsByUserId(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                String message = response.body().getMessage();
                String data = response.body().getReturnData();

                if (response.body().isSuccess()) {
                    Double loadedCoin = Double.parseDouble(data.toString());
                    availableCoins = loadedCoin;
                    txt_available_coins.setText("Available coins : " + loadedCoin.toString());
                } else {
                    MessageDisplay.getInstance().showErrorToast(message, getApplication());
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                MessageDisplay.getInstance().showErrorToast(new ReturnModel().getGlobalErrorMessage().getMessage(), getApplication());
                alertDialog.cancel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
