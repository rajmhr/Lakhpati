package com.lakhpati.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lakhpati.R;
import com.lakhpati.Services.UserApiInterface;
import com.lakhpati.Utilities.Dialogs;
import com.lakhpati.Utilities.EnumCollection;
import com.lakhpati.Utilities.HelperAsyncClass;
import com.lakhpati.Utilities.HelperClass;
import com.lakhpati.Utilities.LoginPreference;
import com.lakhpati.Utilities.MessageDisplay;
import com.lakhpati.customControl.ProgressLoader;
import com.lakhpati.internalService.SignalRChatService;
import com.lakhpati.models.LoginModel;
import com.lakhpati.models.RelatedLotteryGroupModel;
import com.lakhpati.models.ReturnModel;
import com.lakhpati.models.UserDetailViewModel;
import com.lakhpati.retrofit.RetrofitClientInstance;

import java.lang.reflect.Modifier;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    //region Fields and Declaration
    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_email)
    TextInputEditText _emailText;

    @BindView(R.id.input_password)
    TextInputEditText _passwordText;

    @BindView(R.id.btn_login)
    MaterialButton btn_login;

    @BindView(R.id.link_signup)
    MaterialButton link_signup;

    private AlertDialog alertDialog;

    //endregion

    //region Native methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        alertDialog = Dialogs.getInstance().initLoaderDialog(this);

        tryLoginWithPreferences();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(signUpIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    //endregion

    //region Private Methods
    private void tryLoginWithPreferences() {
        LoginPreference preference = new LoginPreference(this);
        LoginModel model = preference.getLoginPreferences();
        if (model != null && model.getEmail() != null) {
            UserDetailViewModel detailViewModel = new UserDetailViewModel();
            detailViewModel.setUserDetailId(model.getUserDetailId());
            detailViewModel.setEmailId(model.getEmail());
            detailViewModel.setDisplayName(model.getDisplayName());
            startApplication(detailViewModel);
        }
    }

    private void startApplication(UserDetailViewModel model) {
        Intent drawerIntent = new Intent(LoginActivity.this, DrawerActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("userModel", model);
        drawerIntent.putExtras(b);
        LoginActivity.this.startActivity(drawerIntent);
    }

    private void login() {
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!validateCredential(email, password))
            return;
        loginToServer(email, password);
    }

    private boolean validateCredential(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void loginToServer(String email, String password) {
        alertDialog.show();
        btn_login.setEnabled(false);
        UserApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiInterface.class);

        LoginModel model = new LoginModel();
        model.setEmail(email);
        model.setPassword(password);

        Call<ReturnModel> callValue = userApiService.login(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                btn_login.setEnabled(true);
                if(response == null){
                    MessageDisplay.getInstance().showErrorToast(new ReturnModel().getServerErrorMessage().getMessage(), getApplication());
                    return;
                }
                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    UserDetailViewModel detailViewModel = HelperClass.getSingleModelFromJson(UserDetailViewModel.class, returnData);

                    if (detailViewModel.getUserStatus().equals(EnumCollection.UserStatus.NotActive.toString())) {
                        startActivationScreen(detailViewModel);
                    } else {
                        finish();
                        startApplication(detailViewModel);
                        new LoginPreference(getApplicationContext()).setLoginPreference(detailViewModel.getDisplayName(), detailViewModel.getEmailId(),
                                detailViewModel.getUserDetailId());
                    }

                }
                else{
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

    private void startActivationScreen(UserDetailViewModel detailViewModel) {
        Intent signUpVerify = new Intent(LoginActivity.this, SignUpVerifyActivity.class);
        signUpVerify.putExtra("userName", detailViewModel.getEmailId());
        signUpVerify.putExtra("password", detailViewModel.getPassword());
        LoginActivity.this.startActivity(signUpVerify);
    }
    //endregion
}
