package com.example.barcodedemo.orm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barcodedemo.api.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select User.* from user where username like :username")
    List<User> getUserList(String username);

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
