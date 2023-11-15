package com.example.easylife.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {UserInfosEntity.class}, version = 2)
public abstract class LocalDataBase extends RoomDatabase {
    public abstract UserInfosDao userInfosDao();
    public static LocalDataBase INSTANCE;
    public static LocalDataBase getInstance(Context context){
        if(INSTANCE==null)
        {
            INSTANCE= Room.databaseBuilder(context, LocalDataBase.class,"EasyLifeLocalDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return INSTANCE;
    }
}
