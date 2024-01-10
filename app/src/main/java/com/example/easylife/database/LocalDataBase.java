package com.example.easylife.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.easylife.database.daos.DraggableCardViewDao;
import com.example.easylife.database.daos.SettingsDao;
import com.example.easylife.database.daos.SpendingsAccountsDao;
import com.example.easylife.database.daos.UserInfosDao;
import com.example.easylife.database.entities.DraggableCardViewEntity;
import com.example.easylife.database.entities.SettingsEntity;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.UserInfosEntity;

@Database(entities = {UserInfosEntity.class, SettingsEntity.class, DraggableCardViewEntity.class, SpendingAccountsEntity.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class LocalDataBase extends RoomDatabase {
    public abstract UserInfosDao userInfosDao();
    public abstract SettingsDao settingsDao();
    public abstract SpendingsAccountsDao spendingsAccountsDao();
    public abstract DraggableCardViewDao draggableCardViewDao();
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
