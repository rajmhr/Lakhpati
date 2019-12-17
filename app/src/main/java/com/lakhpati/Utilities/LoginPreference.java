package com.lakhpati.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.lakhpati.models.LoginModel;
import com.lakhpati.models.ReturnModel;

import retrofit2.Callback;

public class LoginPreference {

    private Context activity;
    private SharedPreferences sp;
    private final String login_preferences="lakhpati_login";

    public LoginPreference(Context activity) {
        this.activity = activity;
    }

    public void setLoginPreference(String displayName, String emailId, int userDetailId) {
        sp = activity.getSharedPreferences(login_preferences, 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("uName", displayName);
        Ed.putInt("uDetailId", userDetailId);
        Ed.putString("eId", emailId);
        Ed.commit();
    }

    public LoginModel getLoginPreferences() {
        sp = activity.getSharedPreferences(login_preferences, 0);
        String displayName = sp.getString("uName", null);
        String emailId = sp.getString("eId", null);
        int userDetailId = sp.getInt("uDetailId", 0);

        LoginModel model = new LoginModel();
        model.setEmail(emailId);
        model.setUserDetailId(userDetailId);
        model.setDisplayName(displayName);
        return  model;
    }
    public void clearLoginPreferences(){
        sp = activity.getSharedPreferences(login_preferences, 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.commit();
    }
}
