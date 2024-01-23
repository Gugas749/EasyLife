package com.alexandreconrado.easylife.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.alexandreconrado.easylife.database.Converters;

import java.util.List;

@Entity(tableName = "SpendingAccounts")
@TypeConverters(Converters.class)
public class SubSpendingAccountsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "parentID")
    private long parentID;
    @ColumnInfo(name = "positionInTheList")
    private long positionInTheList;
    @ColumnInfo(name = "accountTitle")
    private String accountTitle;
    @ColumnInfo(name = "spendsList")
    private List<SpendsEntity> spendsList;
    @ColumnInfo(name = "percentagesNamesList")
    private List<String> percentagesNamesList;
    @ColumnInfo(name = "percentagesColorList")
    private List<String> percentagesColorList;
    public SubSpendingAccountsEntity() {

    }

    public void setInfos(long parentID, long positionInTheList, String accountTitle, List<SpendsEntity> spendsList, List<String> percentagesNamesList, List<String> percentagesColorList) {
        this.parentID = parentID;
        this.positionInTheList = positionInTheList;
        this.accountTitle = accountTitle;
        this.spendsList = spendsList;
        this.percentagesNamesList = percentagesNamesList;
        this.percentagesColorList = percentagesColorList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public List<SpendsEntity> getSpendsList() {
        return spendsList;
    }

    public void setSpendsList(List<SpendsEntity> spendsList) {
        this.spendsList = spendsList;
    }

    public List<String> getPercentagesNamesList() {
        return percentagesNamesList;
    }

    public void setPercentagesNamesList(List<String> percentagesNamesList) {
        this.percentagesNamesList = percentagesNamesList;
    }

    public List<String> getPercentagesColorList() {
        return percentagesColorList;
    }

    public void setPercentagesColorList(List<String> percentagesColorList) {
        this.percentagesColorList = percentagesColorList;
    }

    public long getPositionInTheList() {
        return positionInTheList;
    }

    public void setPositionInTheList(long positionInTheList) {
        this.positionInTheList = positionInTheList;
    }
}
