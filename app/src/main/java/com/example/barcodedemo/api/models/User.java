package com.example.barcodedemo.api.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @ColumnInfo
    private String username;
    @ColumnInfo
    private String password;
    @PrimaryKey(autoGenerate = true)
    private int userID;

    public User(String username, String password, int userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }
    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Ignore
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
