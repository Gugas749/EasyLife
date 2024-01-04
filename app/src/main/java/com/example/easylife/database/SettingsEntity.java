package com.example.easylife.database;

import androidx.fragment.app.Fragment;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.List;

@Entity(tableName = "User_Settings")
public class SettingsEntity {
    @ColumnInfo(name = "userID")
    public int userID;
    @ColumnInfo(name = "linesSplitted")
    private Boolean linesSplitted;
    @ColumnInfo(name = "linesTogether")
    private Boolean linesTogether;
    @ColumnInfo(name = "quantLinesSplitted")
    private int quantLinesSplitted;
    @ColumnInfo(name = "quantLinesTogether")
    private int quantLinesTogether;
    @ColumnInfo(name = "idsLinesSplitted")
    private int[] idsLinesSplitted;
    @ColumnInfo(name = "idsLinesTogether")
    private int[] idsLinesTogether;
    @ColumnInfo(name = "listFragmentLinesSplitted")
    private List<Fragment> listFragmentLinesSplitted;
    @ColumnInfo(name = "listFragmentLinesTogether")
    private List<Fragment> listFragmentLinesTogether;

    public SettingsEntity(int userID, Boolean linesSplitted, Boolean linesTogether,
                           int quantLinesSplitted, int quantLinesTogether,
                           int[] idsLinesSplitted, int[] idsLinesTogether,
                           List<Fragment> listFragmentLinesSplitted, List<Fragment> listFragmentLinesTogether) {
        this.userID = userID;
        this.linesSplitted = linesSplitted;
        this.linesTogether = linesTogether;
        this.quantLinesSplitted = quantLinesSplitted;
        this.quantLinesTogether = quantLinesTogether;
        this.idsLinesSplitted = idsLinesSplitted;
        this.idsLinesTogether = idsLinesTogether;
        this.listFragmentLinesSplitted = listFragmentLinesSplitted;
        this.listFragmentLinesTogether = listFragmentLinesTogether;
    }
}
