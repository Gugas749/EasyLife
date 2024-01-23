package com.alexandreconrado.easylife.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;

import java.util.List;

@Dao
public interface DraggableCardViewDao {
    @Insert
    void insert(DraggableCardViewEntity object);

    @Insert
    void insertList(List<DraggableCardViewEntity> objects);

    @Update
    void update(DraggableCardViewEntity object);

    @Delete
    void delete(DraggableCardViewEntity entity);

    @Query("SELECT * FROM DraggableCardView_Objects")
    List<DraggableCardViewEntity> getObjects();

    @Query("DELETE FROM DraggableCardView_Objects")
    void clearAllEntries();
}

