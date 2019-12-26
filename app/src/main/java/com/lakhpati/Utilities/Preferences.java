package com.lakhpati.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.lakhpati.models.LoginModel;

public class Preferences {

    private Context activity;
    private SharedPreferences sp;
    private final String login_preferences = "lakhpati_login";

    public Preferences(Context activity) {
        this.activity = activity;
    }

    public void setLoginPreference(String displayName, String emailId, int userDetailId) {
        sp = activity.getSharedPreferences(login_preferences, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("uName", displayName);
        editor.putInt("uDetailId", userDetailId);
        editor.putString("eId", emailId);
        editor.apply();
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
        return model;
    }

    public void clearLoginPreferences() {
        sp = activity.getSharedPreferences(login_preferences, 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.apply();
    }
}
