package com.example.denis.loginui;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class User {
    private int ID;
    private String username;
    private String name;
    private String surname;
    private String hashedPassword;
    private String email;
    private String phone;
    private byte[] picture;
    private String city;
    private String gender;

    private Date bdate;
    private String auth;


    protected boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    private boolean setup;

    public User(int id, String username, String name, String surname, String hashedPassword, String email, byte[] picture, String city, String auth){
        this.ID = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.picture = picture;
        this.city = city;
        this.auth = auth;
    }
    public User(JSONObject json){
        try {
            this.ID = json.getInt("ID");
            this.username = json.getString("Username");
            this.auth = json.getString("Hash");
            this.name = json.getString("Name");
            this.email =json.getString("Email");

            this.surname = json.getString("LastName");
            this.setup = (json.getInt("Setup")==1);

            Log.d("gx8", json.getString("Hash"));
            this.city = json.getString("Citta");
            this.gender = json.getString("sesso");
            try {
                this.bdate = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).parse(json.getString("DataNascita").replace("-", "/"));
                Log.d("gx9",this.bdate.toString());
            } catch (ParseException e) {
                Log.d("gx9", json.getString("DataNascita"));
            }




            //this.picture = json.getString("Picture").getBytes();
        } catch (JSONException  e) {

        }
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
    public void updateUser(HashMap<String,String> params){
        Log.d("gx8", "ZZ");
        this.username = params.get("username");
        this.email = params.get("email");
        this.name = params.get("nome");
        this.surname = params.get("cognome");
        try {
            this.bdate = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).parse(params.get("DataNascita").replace("-", "/"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.gender = params.get("Sesso");
        this.city = params.get("Citta");

    }
   /* public void JSONtoUser(JSONObject json){
        try {
            this.ID = Integer.parseInt(json.getString("id"));
            this.username = json.getString("Username");
            this.name = json.getString("Name");
            this.email =json.getString("Email");
            this.surname = json.getString("LastName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

}

