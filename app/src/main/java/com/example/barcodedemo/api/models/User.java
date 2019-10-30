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
    private Integer userId;

    public User(String username, String password, Integer userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userID) {
        this.userId = userID;
    }
}
