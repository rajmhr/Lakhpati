package com.lakhpati.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.lakhpati.models.LoginModel;
import com.lakhpati.models.LotteryGroupModel;

import java.util.ArrayList;

public class MyGroupPreference {
    private Context activity;
    private SharedPreferences sp;
    private final String mygroup_preferences="lakhpati_mygroup";

    public MyGroupPreference(Context activity) {
        this.activity = activity;
    }

    public void setMyGroupPreference(String groupListJson) {
        sp = activity.getSharedPreferences(mygroup_preferences, 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("groupList", groupListJson);
        Ed.commit();
    }

    public String getMyGroupPreferences() {
        sp = activity.getSharedPreferences(mygroup_preferences, 0);
        String groupList = sp.getString("groupList", null);
        return  groupList;
    }
    public void clearLoginPreferences(){
        sp = activity.getSharedPreferences(mygroup_preferences, 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.commit();
    }
}
