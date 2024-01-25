package com.alexandreconrado.easylife.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Spends")
public class SpendsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "amount")
    private float amount;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "mainAccountID")
    private String mainAccountID;
    @ColumnInfo(name = "subAccountID")
    private String subAccountID;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "isPartOfSubAccount")
    private boolean isPartOfSubAccount;
    @ColumnInfo(name = "isPartOf")
    private String isPartOf;

    public SpendsEntity() {

    }

    public void setInfos(float amount, Date date, String mainAccountID, String subAccountID, String category, boolean isPartOfSubAccount, String isPartOf){
        this.amount = amount;
        this.date = date;
        this.mainAccountID = mainAccountID;
        this.subAccountID = subAccountID;
        this.category = category;
        this.isPartOfSubAccount = isPartOfSubAccount;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMainAccountID() {
        return mainAccountID;
    }

    public void setMainAccountID(String mainAccountID) {
        this.mainAccountID = mainAccountID;
    }

    public String getSubAccountID() {
        return subAccountID;
    }

    public void setSubAccountID(String subAccountID) {
        this.subAccountID = subAccountID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPartOfSubAccount() {
        return isPartOfSubAccount;
    }

    public void setPartOfSubAccount(boolean partOfSubAccount) {
        isPartOfSubAccount = partOfSubAccount;
    }

    public String getIsPartOf() {
        return isPartOf;
    }

    public void setIsPartOf(String isPartOf) {
        this.isPartOf = isPartOf;
    }
}
