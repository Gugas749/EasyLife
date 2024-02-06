package com.alexandreconrado.easylife.database;

import androidx.room.TypeConverter;

import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

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
    public static List<SubSpendingAccountsEntity> fromSubAccountsString(String value) {
        Type listType = new TypeToken<List<SubSpendingAccountsEntity>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toSubAccountsString(List<SubSpendingAccountsEntity> list) {
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

    @TypeConverter
    public static Locale fromLocaleString(String value) {
        String[] parts = value.split("_");
        if (parts.length == 1) {
            return new Locale(parts[0]);
        } else if (parts.length == 2) {
            return new Locale(parts[0], parts[1]);
        } else if (parts.length == 3) {
            return new Locale(parts[0], parts[1], parts[2]);
        } else {
            throw new IllegalArgumentException("Invalid locale string: " + value);
        }
    }

    @TypeConverter
    public static String toLocaleString(Locale locale) {
        return locale.toString();
    }

    @TypeConverter
    public static Timestamp fromTimestamp(Long value) {
        return value == null ? null : new Timestamp(value, 0);
    }

    @TypeConverter
    public static Long toTimestamp(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.getSeconds();
    }
}