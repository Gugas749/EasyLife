package com.example.easylife.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easylife.database.entities.UserInfosEntity;

import java.util.List;

@Dao
public interface UserInfosDao {
    @Insert
    void insert(UserInfosEntity user);

    @Update
    void update(UserInfosEntity user);

    @Query("SELECT * FROM User_Infos")
    List<UserInfosEntity> getUserInfos();
}

