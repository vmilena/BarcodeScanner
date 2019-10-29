package com.example.barcodedemo.api.models;

import com.orm.SugarRecord;

public class User extends SugarRecord<User> {
    private String username;
    private String password;
    private String userID;

    public User(String usernme, String password, String userID) {
        this.username = usernme;
        this.password = password;
        this.userID = userID;
    }

    public User(String username, String password) {
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsernme(String usernme) {
        this.username = usernme;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
