package com.example.easylife.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User_Infos")
public class UserInfosEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "password")
    public String Password;
    @ColumnInfo(name = "tema")
    public String Tema;
    @ColumnInfo(name = "language")
    public String Language;

    public UserInfosEntity(int id, String Password, String Tema, String Language) {
        this.id = id;
        this.Password = Password;
        this.Tema = Tema;
        this.Language = Language;
    }
}
