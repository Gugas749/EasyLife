package com.alexandreconrado.easylife.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;

import java.util.List;

@Dao
public interface UserInfosDao {
    @Insert
    void insert(UserInfosEntity user);

    @Update
    void update(UserInfosEntity user);
    @Insert
    void insertList(List<UserInfosEntity> users);

    @Query("SELECT * FROM User_Infos")
    List<UserInfosEntity> getUserInfos();

    @Query("DELETE FROM User_Infos")
    void clearAllEntries();
}

