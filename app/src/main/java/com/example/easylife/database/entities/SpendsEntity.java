package com.example.easylife.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Spends")
public class SpendsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "amount")
    private float amount;
    @ColumnInfo(name = "where")
    private String where;
    @ColumnInfo(name = "when")
    private String when;

    @ColumnInfo(name = "isPartOf")
    private String isPartOf;
    public SpendsEntity() {

    }

    public void setInfos(float amount, String where, String when, String isPartOf){
        this.amount = amount;
        this.where = where;
        this.when = when;
        this.isPartOf = isPartOf;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }
    public String getWhen() {
        return when;
    }
    public void setWhen(String when) {
        this.when = when;
    }

    public String getIsPartOf() {
        return isPartOf;
    }

    public void setIsPartOf(String isPartOf) {
        this.isPartOf = isPartOf;
    }
}
