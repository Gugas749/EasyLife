package com.alexandreconrado.easylife.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.alexandreconrado.easylife.database.Converters;

import java.util.ArrayList;
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
    @ColumnInfo(name = "subAccountsList")
    private List<SubSpendingAccountsEntity> subAccountsList;
    @ColumnInfo(name = "spendsList")
    private List<SpendsEntity> spendsList;
    @ColumnInfo(name = "percentagesNamesList")
    private List<String> percentagesNamesList;
    @ColumnInfo(name = "percentagesColorList")
    private List<String> percentagesColorList;
    public SpendingAccountsEntity() {

    }
    public SpendingAccountsEntity(SpendingAccountsEntity spendingAccountsEntity) {
        this.idUserFirebase = spendingAccountsEntity.getIdUserFirebase();
        this.emailUser = spendingAccountsEntity.getEmailUser();
        this.accountTitle = spendingAccountsEntity.getAccountTitle();
        this.spendsList = new ArrayList<>();
        for (SpendsEntity spend : spendingAccountsEntity.getSpendsList()) {
            this.spendsList.add(new SpendsEntity(spend));
        }
        this.percentagesNamesList = new ArrayList<>(spendingAccountsEntity.getPercentagesNamesList());
        this.percentagesColorList = new ArrayList<>(spendingAccountsEntity.getPercentagesColorList());
        this.subAccountsList = new ArrayList<>();
        for (SubSpendingAccountsEntity subAccount : spendingAccountsEntity.getSubAccountsList()) {
            this.subAccountsList.add(new SubSpendingAccountsEntity(subAccount));
        }
    }

    public void setInfos(String idUserFirebase, String emailUser, String accountTitle, List<SpendsEntity> spendsList, List<String> percentagesNamesList, List<String> percentagesColorList) {
        this.idUserFirebase = idUserFirebase;
        this.emailUser = emailUser;
        this.accountTitle = accountTitle;
        this.spendsList = spendsList;
        this.percentagesNamesList = percentagesNamesList;
        this.percentagesColorList = percentagesColorList;
    }
    public void setInfos2(String idUserFirebase, String emailUser, String accountTitle, List<String> percentagesNamesList, List<String> percentagesColorList) {
        this.idUserFirebase = idUserFirebase;
        this.emailUser = emailUser;
        this.accountTitle = accountTitle;
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

    public List<SubSpendingAccountsEntity> getSubAccountsList() {
        return subAccountsList;
    }

    public void setSubAccountsList(List<SubSpendingAccountsEntity> subAccountsList) {
        this.subAccountsList = subAccountsList;
    }

    public void setAddAllSubAccountsList(List<SubSpendingAccountsEntity> subAccountsList) {
        this.subAccountsList = new ArrayList<>();
        this.subAccountsList.addAll(subAccountsList);
    }
}
