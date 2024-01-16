package com.example.easylife.database;

import androidx.room.TypeConverter;

import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.SpendsEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<SpendsEntity> fromSpendsString(String value) {
        Type listType = new TypeToken<List<SpendsEntity>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toSpendsString(List<SpendsEntity> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<SpendingAccountsEntity> fromSpendingAccountsString(String value) {
        Type listType = new TypeToken<List<SpendingAccountsEntity>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toSpendingAccountsString(List<SpendingAccountsEntity> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<String> fromStringList(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toStringList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}