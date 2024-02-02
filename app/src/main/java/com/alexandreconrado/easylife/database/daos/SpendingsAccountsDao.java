package com.alexandreconrado.easylife.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;

import java.util.List;

@Dao
public interface SpendingsAccountsDao {
    @Insert
    void insert(SpendingAccountsEntity account);

    @Update
    void update(SpendingAccountsEntity account);

    @Delete
    void delete(SpendingAccountsEntity account);
    @Insert
    void insertList(List<SpendingAccountsEntity> accounts);

    @Query("SELECT * FROM SpendingAccounts")
    List<SpendingAccountsEntity> getSpendingsAccounts();

    @Query("DELETE FROM DraggableCardView_Objects")
    void clearAllEntries();
}

