package com.alexandreconrado.easylife.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alexandreconrado.easylife.database.entities.SettingsEntity;

import java.util.List;

@Dao
public interface SettingsDao {
    @Insert
    void insert(SettingsEntity settings);

    @Update
    void update(SettingsEntity settings);

    @Query("SELECT * FROM User_Settings")
    List<SettingsEntity> getSettings();
}

