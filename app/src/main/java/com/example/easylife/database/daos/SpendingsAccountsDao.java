package com.example.easylife.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easylife.database.entities.SpendingAccountsEntity;

import java.util.List;

@Dao
public interface SpendingsAccountsDao {
    @Insert
    void insert(SpendingAccountsEntity account);

    @Update
    void update(SpendingAccountsEntity account);

    @Delete
    void delete(SpendingAccountsEntity account);

    @Query("SELECT * FROM SpendingAccounts")
    List<SpendingAccountsEntity> getSpendingsAccounts();
}

