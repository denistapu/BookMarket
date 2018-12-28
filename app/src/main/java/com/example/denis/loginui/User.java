package com.example.denis.loginui;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private int ID;
    private String username;
    private String name;
    private String surname;
    private String hashedPassword;
    private String email;
    private String phone;

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    private boolean setup;

    public User(int id, String username, String name, String surname, String hashedPassword, String email){
        this.ID = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }
    public User(JSONObject json){
        try {
            this.ID = json.getInt("ID");
            this.username = json.getString("Username");
            this.name = json.getString("Name");
            this.email =json.getString("Email");
            this.surname = json.getString("LastName");
            this.setup = (json.getInt("Setup")==1);
        } catch (JSONException e) {
            e.printStackTrace();
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

