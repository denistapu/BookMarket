package com.example.denis.loginui;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences("Login", 0);
        editor = pref.edit();
    }
    public void createSession(User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putBoolean("isLogged", true);
        editor.putString("User", json);
        editor.apply();
    }
    public boolean isLoggedIn(){
        return pref.getBoolean("isLogged",false);
    }
    public User getUser(){
        Gson gson = new Gson();
        return gson.fromJson(pref.getString("User",null),User.class);
    }
    public void updateUser(User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.remove("User");
        editor.putString("User", json);
        editor.apply();
    }
    public void logout(){
        editor.clear();
        editor.apply();
    }

}
