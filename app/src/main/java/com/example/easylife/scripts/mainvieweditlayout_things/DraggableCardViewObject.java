package com.example.easylife.scripts.mainvieweditlayout_things;

import android.graphics.Point;

public class DraggableCardViewObject {
    int position;
    String type, ChartName, style;
    float value1Percentage, value2Percentage, value3Percentage, value4Percentage;
    String value1Text, value2Text, value3Text, value4Text;
    int value1Color, value2Color, value3Color, value4Color;

    public DraggableCardViewObject(int position, String type, String chartName, String style) {
        this.position = position;
        this.type = type;
        this.ChartName = chartName;
        this.style = style;
    }

    public void setInfos(float value1Percentage, float value2Percentage, float value3Percentage, float value4Percentage, String value1Text, String value2Text, String value3Text, String value4Text, int value1Color, int value2Color, int value3Color, int value4Color){
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
        return ChartName;
    }

    public void setChartName(String chartName) {
        ChartName = chartName;
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
}
