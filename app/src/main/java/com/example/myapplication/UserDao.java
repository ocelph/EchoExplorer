package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    // Insert a new user
    @Insert
    void insert(User user);

    // Insert multiple users (varargs or list)
    @Insert
    void insertAll(User... users);

    // Update an existing user
    @Update
    void update(User user);

    // Delete a user
    @Delete
    void delete(User user);

    // Delete multiple users
    @Delete
    void deleteUsers(User... users);

    // Query a user by ID
    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> getUserById(String id);

    // Query all users
    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();

    // Query user by email
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    LiveData<User> getUserByEmail(String email);

    // Delete a user by ID
    @Query("DELETE FROM user WHERE id = :id")
    void deleteUserById(String id);

    // Update the email of a user by ID
    @Query("UPDATE user SET email = :email WHERE id = :id")
    void updateUserEmail(String id, String email);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    LiveData<User> findByEmail(String email);
}
