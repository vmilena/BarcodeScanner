package com.example.barcodedemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.barcodedemo.api.models.User;
import com.google.gson.Gson;

public class UserManager {
    private static UserManager instance;
    private SharedPreferences sharedPreferences;

    private UserManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
        }
        return instance;
    }
    public void logout(){
        sharedPreferences.edit()
                .clear()
                .apply();
    }
    public void saveUser(User user) {
        sharedPreferences.edit().putString(Constants.USER, new Gson().toJson(user)).apply();
    }

    public User getCurrentUser(){
        String userJson = sharedPreferences.getString(Constants.USER, null);
        if(userJson != null){
            return new Gson().fromJson(userJson, User.class);
        }
        return null;
    }

}
