package com.example.easylife.database.entities;

import android.view.View;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.easylife.database.Converters;

@Entity(tableName = "DraggableCardView_Objects")
@TypeConverters(Converters.class)
public class DraggableCardViewEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "parentView")
    private String parentView;
    @ColumnInfo(name = "position")
    private int position;
    @ColumnInfo(name = "accountID")
    private String accountID;
    @ColumnInfo(name = "subAccountID")
    private String subAccountID;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "chart_name")
    private String chartName;
    @ColumnInfo(name = "style")
    private String style;
    @ColumnInfo(name = "value1_percentage")
    private float value1Percentage;
    @ColumnInfo(name = "value2_percentage")
    private float value2Percentage;
    @ColumnInfo(name = "value3_percentage")
    private float value3Percentage;
    @ColumnInfo(name = "value4_percentage")
    private float value4Percentage;
    @ColumnInfo(name = "value5_percentage")
    private float value5Percentage;
    @ColumnInfo(name = "value6_percentage")
    private float value6Percentage;
    @ColumnInfo(name = "value7_percentage")
    private float value7Percentage;
    @ColumnInfo(name = "value8_percentage")
    private float value8Percentage;
    @ColumnInfo(name = "value1_text")
    private String value1Text;
    @ColumnInfo(name = "value2_text")
    private String value2Text;
    @ColumnInfo(name = "value3_text")
    private String value3Text;
    @ColumnInfo(name = "value4_text")
    private String value4Text;
    @ColumnInfo(name = "value5_text")
    private String value5Text;
    @ColumnInfo(name = "value6_text")
    private String value6Text;
    @ColumnInfo(name = "value7_text")
    private String value7Text;
    @ColumnInfo(name = "value8_text")
    private String value8Text;
    @ColumnInfo(name = "value1_color")
    private int value1Color;
    @ColumnInfo(name = "value2_color")
    private int value2Color;
    @ColumnInfo(name = "value3_color")
    private int value3Color;
    @ColumnInfo(name = "value4_color")
    private int value4Color;
    @ColumnInfo(name = "value5_color")
    private int value5Color;
    @ColumnInfo(name = "value6_color")
    private int value6Color;
    @ColumnInfo(name = "value7_color")
    private int value7Color;
    @ColumnInfo(name = "value8_color")
    private int value8Color;
    public DraggableCardViewEntity(int position, String type, String chartName, String style) {
        this.position = position;
        this.type = type;
        this.chartName = chartName;
        this.style = style;
    }

    public void setInfos(String accountID, String subAccountID, float value1Percentage, float value2Percentage, float value3Percentage, float value4Percentage, String value1Text, String value2Text, String value3Text, String value4Text, int value1Color, int value2Color, int value3Color, int value4Color){
        this.accountID = accountID;
        this.subAccountID = subAccountID;
        this.value1Percentage = value1Percentage;
        this.value2Percentage = value2Percentage;
        this.value3Percentage = value3Percentage;
        this.value4Percentage = value4Percentage;
        this.value1Text = value1Text;
        this.value2Text = value2Text;
        this.value3Text = value3Text;
        this.value4Text = value4Text;
        this.value1Color = value1Color;
        this.value2Color = value2Color;
        this.value3Color = value3Color;
        this.value4Color = value4Color;
    }

    public void setInfosType3(float value1Percentage, float value2Percentage, float value3Percentage, float value4Percentage, String value1Text, String value2Text, String value3Text, String value4Text, int value1Color, int value2Color, int value3Color, int value4Color){
        this.value5Percentage = value1Percentage;
        this.value6Percentage = value2Percentage;
        this.value7Percentage = value3Percentage;
        this.value8Percentage = value4Percentage;
        this.value5Text = value1Text;
        this.value6Text = value2Text;
        this.value7Text = value3Text;
        this.value8Text = value4Text;
        this.value5Color = value1Color;
        this.value6Color = value2Color;
        this.value7Color = value3Color;
        this.value8Color = value4Color;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public float getValue1Percentage() {
        return value1Percentage;
    }

    public void setValue1Percentage(float value1Percentage) {
        this.value1Percentage = value1Percentage;
    }

    public float getValue2Percentage() {
        return value2Percentage;
    }

    public void setValue2Percentage(float value2Percentage) {
        this.value2Percentage = value2Percentage;
    }

    public float getValue3Percentage() {
        return value3Percentage;
    }

    public void setValue3Percentage(float value3Percentage) {
        this.value3Percentage = value3Percentage;
    }

    public float getValue4Percentage() {
        return value4Percentage;
    }

    public void setValue4Percentage(float value4Percentage) {
        this.value4Percentage = value4Percentage;
    }

    public String getValue1Text() {
        return value1Text;
    }

    public void setValue1Text(String value1Text) {
        this.value1Text = value1Text;
    }

    public String getValue2Text() {
        return value2Text;
    }

    public void setValue2Text(String value2Text) {
        this.value2Text = value2Text;
    }

    public String getValue3Text() {
        return value3Text;
    }

    public void setValue3Text(String value3Text) {
        this.value3Text = value3Text;
    }

    public String getValue4Text() {
        return value4Text;
    }

    public void setValue4Text(String value4Text) {
        this.value4Text = value4Text;
    }

    public int getValue1Color() {
        return value1Color;
    }

    public void setValue1Color(int value1Color) {
        this.value1Color = value1Color;
    }

    public int getValue2Color() {
        return value2Color;
    }

    public void setValue2Color(int value2Color) {
        this.value2Color = value2Color;
    }

    public int getValue3Color() {
        return value3Color;
    }

    public void setValue3Color(int value3Color) {
        this.value3Color = value3Color;
    }

    public int getValue4Color() {
        return value4Color;
    }

    public void setValue4Color(int value4Color) {
        this.value4Color = value4Color;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getSubAccountID() {
        return subAccountID;
    }

    public void setSubAccountID(String subAccountID) {
        this.subAccountID = subAccountID;
    }

    public float getValue5Percentage() {
        return value5Percentage;
    }

    public void setValue5Percentage(float value5Percentage) {
        this.value5Percentage = value5Percentage;
    }

    public float getValue6Percentage() {
        return value6Percentage;
    }

    public void setValue6Percentage(float value6Percentage) {
        this.value6Percentage = value6Percentage;
    }

    public float getValue7Percentage() {
        return value7Percentage;
    }

    public void setValue7Percentage(float value7Percentage) {
        this.value7Percentage = value7Percentage;
    }

    public float getValue8Percentage() {
        return value8Percentage;
    }

    public void setValue8Percentage(float value8Percentage) {
        this.value8Percentage = value8Percentage;
    }

    public String getValue5Text() {
        return value5Text;
    }

    public void setValue5Text(String value5Text) {
        this.value5Text = value5Text;
    }

    public String getValue6Text() {
        return value6Text;
    }

    public void setValue6Text(String value6Text) {
        this.value6Text = value6Text;
    }

    public String getValue7Text() {
        return value7Text;
    }

    public void setValue7Text(String value7Text) {
        this.value7Text = value7Text;
    }

    public String getValue8Text() {
        return value8Text;
    }

    public void setValue8Text(String value8Text) {
        this.value8Text = value8Text;
    }

    public int getValue5Color() {
        return value5Color;
    }

    public void setValue5Color(int value5Color) {
        this.value5Color = value5Color;
    }

    public int getValue6Color() {
        return value6Color;
    }

    public void setValue6Color(int value6Color) {
        this.value6Color = value6Color;
    }

    public int getValue7Color() {
        return value7Color;
    }

    public void setValue7Color(int value7Color) {
        this.value7Color = value7Color;
    }

    public int getValue8Color() {
        return value8Color;
    }

    public void setValue8Color(int value8Color) {
        this.value8Color = value8Color;
    }

    public String getParentView() {
        return parentView;
    }

    public void setParentView(String parentView) {
        this.parentView = parentView;
    }
}
