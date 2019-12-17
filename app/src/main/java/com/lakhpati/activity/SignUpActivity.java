package com.lakhpati.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    TextInputEditText _nameText;
    @BindView(R.id.input_email)
    TextInputEditText _emailText;
    @BindView(R.id.input_password)
    TextInputEditText _passwordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        alertDialog = Dialogs.getInstance().initLoaderDialog(this);
        _nameText.addTextChangedListener(new SignUpTextWatcher(_nameText));
        _emailText.addTextChangedListener(new SignUpTextWatcher(_emailText));
        _passwordText.addTextChangedListener(new SignUpTextWatcher(_passwordText));

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displyaName = _nameText.getText().toString();
                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                if (!validate())
                    return;
                createAccount(email, password, displyaName);
                //signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }


    private void createAccount(String email, String password, String displayName) {
        alertDialog.show();

        UserApiInterface userApiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiInterface.class);

        UserRegisterModel model = new UserRegisterModel();
        model.setEmail(email);
        model.setPassword(password);
        model.setDisplayName(displayName);

        Call<ReturnModel> callValue = userApiService.registerUserPublic(model);
        callValue.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {

                if (response.body().isSuccess()) {
                    String returnData = response.body().getReturnData();
                    MessageDisplay.getInstance().showSuccessToast("Your account has created. Please verify your account.", getApplication());
                    startVerifyScreen(email,password);

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

    private void startVerifyScreen(String email, String password) {
        Intent signUpVerify = new Intent(SignUpActivity.this, SignUpVerifyActivity.class);
        signUpVerify.putExtra("userName", email);
        signUpVerify.putExtra("password", password);
        SignUpActivity.this.startActivity(signUpVerify);
    }

    public boolean validate() {
        boolean isValid = true;

        if (!validateName()){
            isValid = false;
        }
        if (!validateEmail()){
            isValid = false;
        }
        if (!validatePassword()){
            isValid = false;
        }
        return isValid;
    }

    private boolean validateName(){
        boolean isValid = true;
        String name = _nameText.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            isValid =  false;
        } else {
            _nameText.setError(null);
        }
        return isValid;
    }

    private boolean validateEmail(){
        boolean isValid = true;
        String email = _emailText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            isValid = false;
        } else {
            _emailText.setError(null);
        }
        return  isValid;
    }
    private  boolean validatePassword(){
        boolean isValid = true;
        String password = _passwordText.getText().toString();
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            isValid =false;
        } else {
            _passwordText.setError(null);
        }
        return isValid;
    }
    private class SignUpTextWatcher implements TextWatcher {

        private View view;

        private SignUpTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

}