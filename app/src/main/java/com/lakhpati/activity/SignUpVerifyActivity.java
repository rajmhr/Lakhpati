package com.lakhpati.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.lakhpati.R;
import com.lakhpati.Services.UserApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.LoginPreference;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.models.LoginModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserDetailViewModel;
import com.lakhpati.models.UserRegisterModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpVerifyActivity extends AppCompatActivity {

    @BindView(R.id.edittxt_verifyCode)
    TextInputEditText edittxt_verifyCode;

    @BindView(R.id.btn_verifyUser)
    MaterialButton btn_verifyUser;

    @BindView(R.id.btn_RequestCode)
    MaterialButton btn_RequestCode;


    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verify);
        ButterKnife.bind(this);

        String userName = getIntent().getExtras().getString("userName", "");
        String password = getIntent().getExtras().getString("password", "");

        alertDialog = Dialogs.getInstance().initLoaderDialog(this);
        edittxt_verifyCode.addTextChangedListener(new VerifyCodeTextWatcher(edittxt_verifyCode));

        btn_verifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edittxt_verifyCode.getText().toString();

                if (validateCode()) {
                    loginToServer(userName, password, code);
                }
            }
        });
        btn_RequestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode(userName);
            }
        });
    }

    private void getVerificationCode(String email) {
            alertDialog.show();

            UserApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiInterface.class);

            UserRegisterModel model = new UserRegisterModel();
            model.setEmail(email);

            Call<ReturnModel> callValue = userApiService.getVerificationCode(model);
            callValue.enqueue(new Callback<ReturnModel>() {
                @Override
                public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                    if (response.body().isSuccess()) {
                        MessageDisplay.getInstance().showSuccessToast("Code has been sent to your email id. Please enter your code.", getApplication());
                    } else {
                        MessageDisplay.getInstance().showSuccessToast(response.body().getMessage(), getApplication());
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

    private void loginToServer(String email, String password, String code) {
        alertDialog.show();

        UserApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiInterface.class);

        LoginModel model = new LoginModel();
        model.setEmail(email);
        model.setPassword(password);
        model.setCode(code);

        Call<ReturnModel> callValue = userApiService.loginWithCode(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    UserDetailViewModel detailViewModel = HelperClass.getSingleModelFromJson(UserDetailViewModel.class, returnData);

                    startApplication(detailViewModel);
                    new LoginPreference(getApplicationContext()).setLoginPreference(detailViewModel.getDisplayName(), detailViewModel.getEmailId(),
                            detailViewModel.getUserDetailId());

                }
                else {
                    MessageDisplay.getInstance().showErrorToast(response.body().getMessage(), getApplication());
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

    private class VerifyCodeTextWatcher implements TextWatcher {

        private View view;

        private VerifyCodeTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edittxt_verifyCode:
                    validateCode();
                    break;
            }
        }
    }

    private boolean validateCode() {
        if (edittxt_verifyCode.getText().toString().equals("") || edittxt_verifyCode.getText().toString().length() != 4) {
            edittxt_verifyCode.setError("Please enter 4 digit code.");
            return false;
        }
        return true;
    }

    private void startApplication(UserDetailViewModel model) {
        Intent drawerIntent = new Intent(SignUpVerifyActivity.this, DrawerActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("userModel", model);
        drawerIntent.putExtras(b);
        SignUpVerifyActivity.this.startActivity(drawerIntent);
    }
}
