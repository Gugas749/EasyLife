package com.alexandreconrado.easylife.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Locale;

@Entity(tableName = "User_Infos")
public class UserInfosEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "firebaseID")
    public String firebaseID;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "password")
    public String Password;
    @ColumnInfo(name = "theme")
    public String Theme;
    @ColumnInfo(name = "language")
    public Locale Language;

    public UserInfosEntity(String Password, String Theme, Locale Language) {
        this.Password = Password;
        this.Theme = Theme;
        this.Language = Language;
    }

    public void setInfos(String firebaseID, String email){
        this.firebaseID = firebaseID;
        this.email = email;
    }
}