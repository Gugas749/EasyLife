package com.example.easylife.fragments.mainviewpiecharts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentRectangleWithPieChartBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class RectangleWithPieChartFragment extends Fragment {

    private FragmentRectangleWithPieChartBinding binding;
    private int color1, color2, color3, color4;
    private String title;
    private float value1, value2, value3, value4;

    public RectangleWithPieChartFragment(int color1, int color2, int color3, int color4, String title, float value1, float value2, float value3, float value4) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.title = title;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRectangleWithPieChartBinding.inflate(inflater);

        init();
        loadChart();

        return binding.getRoot();
    }

    private void init(){
        binding.textViewNameOfChartFragRectangleWithPieChart.setText(title.trim());
    }
    public void loadChart(){
        PieChart pieChart = binding.pieChartFragRectangleWithPieChart;

        int numValues = 0;
        List<PieEntry> entries = new ArrayList<>();

        if(value1 != 0){
            numValues++;
            entries.add(new PieEntry(value1));
        }
        if(value2 != 0){
            numValues++;
            entries.add(new PieEntry(value2));
        }
        if(value3 != 0){
            numValues++;
            entries.add(new PieEntry(value3));
        }
        if(value4 != 0){
            numValues++;
            entries.add(new PieEntry(value4));
        }

        int[] colors = new int[0];

        switch (numValues){
            case 1:
                colors = new int[]{color1};
                break;
            case 2:
                colors = new int[]{color1, color2};
                break;
            case 3:
                colors = new int[]{color1, color2, color3};
                break;
            case 4:
                colors = new int[]{color1, color2, color3, color4};
                break;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(colors);
        dataSet.setDrawValues(false); // Hide labels and percentages

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Configure PieChart
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1400);

        // Invalidate the chart to refresh
        pieChart.invalidate();
    }
}