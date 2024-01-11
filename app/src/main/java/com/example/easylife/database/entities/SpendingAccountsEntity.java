package com.example.easylife.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.easylife.database.Converters;

import java.util.List;

@Entity(tableName = "SpendingAccounts")
@TypeConverters(Converters.class)
public class SpendingAccountsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "idUserFirebase")
    private String idUserFirebase;
    @ColumnInfo(name = "emailUser")
    private String emailUser;
    @ColumnInfo(name = "accountTitle")
    private String accountTitle;
    @ColumnInfo(name = "spendsList")
    private List<SpendsEntity> spendsList;
    @ColumnInfo(name = "percentagesNamesList")
    private List<String> percentagesNamesList;
    @ColumnInfo(name = "percentagesColorList")
    private List<String> percentagesColorList;
    public SpendingAccountsEntity() {

    }

    public void setInfos(String idUserFirebase, String emailUser, String accountTitle, List<SpendsEntity> spendsList, List<String> percentagesNamesList, List<String> percentagesColorList) {
        this.idUserFirebase = idUserFirebase;
        this.emailUser = emailUser;
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

    public String getIdUserFirebase() {
        return idUserFirebase;
    }

    public void setIdUserFirebase(String idUserFirebase) {
        this.idUserFirebase = idUserFirebase;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
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
}
