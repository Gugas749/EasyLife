package com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.databinding.FragmentRectangleWithPieChartInTheLeftAndTextInTheRightBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class RectangleWithPieChartInTheLeftAndTextInTheRightFragment extends Fragment {
    private LongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight Listenner;
    public interface LongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight{
        void onLongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList, String parentTag);
    }
    public void setListenner(LongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight Listenner){
        this.Listenner = Listenner;
    }
    private FragmentRectangleWithPieChartInTheLeftAndTextInTheRightBinding binding;
    private int color1, color2, color3, color4;
    private String title, valueText1, valueText2, valueText3, valueText4, parentTag;
    private float value1, value2, value3, value4;
    private DraggableCardViewEntity thisObject;
    private boolean disableFunctions = false;
    public void setDisableFunctions(boolean disableFunctions){
        this.disableFunctions = disableFunctions;
    }
    public void setObject(DraggableCardViewEntity thisObject){
        this.thisObject = thisObject;
    }
    private List<SpendingAccountsEntity> spendingAccountsEntityList;
    public void setAccountsList(List<SpendingAccountsEntity> spendingAccountsEntityList){
        this.spendingAccountsEntityList = spendingAccountsEntityList;
    }
    public RectangleWithPieChartInTheLeftAndTextInTheRightFragment() {

    }

    public void setInfos(int color1, int color2, int color3, int color4,
                         String title,
                         float value1, float value2, float value3, float value4,
                         String valueText1, String valueText2, String valueText3, String valueText4, String parentTag){
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.title = title;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.valueText1 = valueText1;
        this.valueText2 = valueText2;
        this.valueText3 = valueText3;
        this.valueText4 = valueText4;
        this.parentTag = parentTag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRectangleWithPieChartInTheLeftAndTextInTheRightBinding.inflate(inflater);

        if(title != null){
            init();
            loadChart();
            setupLongPress();
        }

        return binding.getRoot();
    }
    private void setupLongPress(){
        binding.cardViewContainerRectangleStyle1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!disableFunctions){
                    Listenner.onLongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight(thisObject, spendingAccountsEntityList, parentTag);
                }
                return false;
            }
        });
    }
    private void init(){
        binding.linerLayoutPieChartPercentage1FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage2FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage3FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage4FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.textViewNameOfChartFragRectangleWithChartInTheLeftAndTextInTheRight.setText(title.trim());
        binding.textViewPercentageText1FragRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText1.trim());
        binding.textViewPercentageText2FragRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText2.trim());
        binding.textViewPercentageText3FragRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText3.trim());
        binding.textViewPercentageText4FragRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText4.trim());
    }

    public void loadChart(){
        PieChart pieChart = binding.pieChartFragRectangleWithChartInTheLeftAndTextInTheRight;
        pieChart.setHighlightPerTapEnabled(false);
        int numValues = 0;
        List<PieEntry> entries = new ArrayList<>();

        if(value1 != 0){
            numValues++;
            entries.add(new PieEntry(value1));
            binding.linerLayoutPieChartPercentage1FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage1FragRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color1);
        }
        if(value2 != 0){
            numValues++;
            entries.add(new PieEntry(value2));
            binding.linerLayoutPieChartPercentage2FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage2FragRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color2);
        }
        if(value3 != 0){
            numValues++;
            entries.add(new PieEntry(value3));
            binding.linerLayoutPieChartPercentage3FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage3FragRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color3);
        }
        if(value4 != 0){
            numValues++;
            entries.add(new PieEntry(value4));
            binding.linerLayoutPieChartPercentage4FragRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage4FragRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color4);
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