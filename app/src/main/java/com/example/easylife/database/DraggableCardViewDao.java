package com.example.easylife.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easylife.scripts.mainvieweditlayout_things.DraggableCardView;

import java.util.List;

@Dao
public interface DraggableCardViewDao {
    @Insert
    void insert(DraggableCardViewEntity object);

    @Insert
    void insertList(List<DraggableCardViewEntity> objects);

    @Update
    void update(DraggableCardViewEntity object);

    @Query("SELECT * FROM DraggableCardView_Objects")
    List<DraggableCardViewEntity> getObjects();

    @Query("DELETE FROM DraggableCardView_Objects")
    void clearAllEntries();
}

