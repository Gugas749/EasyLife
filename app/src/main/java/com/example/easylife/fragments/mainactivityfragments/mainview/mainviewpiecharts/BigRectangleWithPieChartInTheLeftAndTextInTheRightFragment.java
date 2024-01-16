package com.example.easylife.fragments.mainactivityfragments.mainview.mainviewpiecharts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.entities.DraggableCardViewEntity;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.databinding.FragmentBigRectangleWithPieChartInTheLeftAndTextInTheRightBinding;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment extends Fragment {

    private LongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight Listenner;
    public interface LongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight{
        void onLongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList);
    }
    public void setListenner(LongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight Listenner){
        this.Listenner = Listenner;
    }
    private FragmentBigRectangleWithPieChartInTheLeftAndTextInTheRightBinding binding;
    private int color1, color2, color3, color4, color5, color6, color7, color8;
    private String title, valueText1, valueText2, valueText3, valueText4, valueText5, valueText6, valueText7, valueText8;
    private float value1, value2, value3, value4, value5, value6, value7, value8;
    private DraggableCardViewEntity thisObject;
    private List<SpendingAccountsEntity> spendingAccountsEntityList;
    public void setAccountsList(List<SpendingAccountsEntity> spendingAccountsEntityList){
        this.spendingAccountsEntityList = spendingAccountsEntityList;
    }
    public BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setInfos(int color1, int color2, int color3, int color4, int color5, int color6, int color7, int color8,
                         String title,
                         float value1, float value2, float value3, float value4, float value5, float value6, float value7, float value8,
                         String valueText1, String valueText2, String valueText3, String valueText4 ,String valueText5, String valueText6, String valueText7, String valueText8){
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
        this.color6 = color6;
        this.color7 = color7;
        this.color8 = color8;
        this.title = title;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.valueText1 = valueText1;
        this.valueText2 = valueText2;
        this.valueText3 = valueText3;
        this.valueText4 = valueText4;
        this.valueText5 = valueText5;
        this.valueText6 = valueText6;
        this.valueText7 = valueText7;
        this.valueText8 = valueText8;
    }
    public void setObject(DraggableCardViewEntity thisObject){
        this.thisObject = thisObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBigRectangleWithPieChartInTheLeftAndTextInTheRightBinding.inflate(inflater);

        init();
        loadChart();
        setupLongPress();

        return binding.getRoot();
    }
    private void setupLongPress(){
        binding.cardViewContainerBigRectangleStyle1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Listenner.onLongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight(thisObject, spendingAccountsEntityList);
                return false;
            }
        });
    }
    private void init(){
        binding.linerLayoutPieChartPercentage1FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage2FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage3FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage4FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage5FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage6FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage7FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);
        binding.linerLayoutPieChartPercentage8FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.INVISIBLE);

        binding.textViewNameOfChartFragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(title.trim());

        binding.textViewPercentageText1FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText1.trim());
        binding.textViewPercentageText2FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText2.trim());
        binding.textViewPercentageText3FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText3.trim());
        binding.textViewPercentageText4FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText4.trim());
        binding.textViewPercentageText5FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText5.trim());
        binding.textViewPercentageText6FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText6.trim());
        binding.textViewPercentageText7FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText7.trim());
        binding.textViewPercentageText8FragBigRectangleWithChartInTheLeftAndTextInTheRight.setText(valueText8.trim());
    }
    public void loadChart(){
        PieChart pieChart = binding.pieChartFragBigRectangleWithChartInTheLeftAndTextInTheRight;
        pieChart.setHighlightPerTapEnabled(false);
        int numValues = 0;
        List<PieEntry> entries = new ArrayList<>();

        if(value1 != 0){
            numValues++;
            entries.add(new PieEntry(value1));
            binding.linerLayoutPieChartPercentage1FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage1FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color1);
        }
        if(value2 != 0){
            numValues++;
            entries.add(new PieEntry(value2));
            binding.linerLayoutPieChartPercentage2FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage2FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color2);
        }
        if(value3 != 0){
            numValues++;
            entries.add(new PieEntry(value3));
            binding.linerLayoutPieChartPercentage3FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage3FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color3);
        }
        if(value4 != 0){
            numValues++;
            entries.add(new PieEntry(value4));
            binding.linerLayoutPieChartPercentage4FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage4FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color4);
        }
        if(value5 != 0){
            numValues++;
            entries.add(new PieEntry(value5));
            binding.linerLayoutPieChartPercentage5FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage5FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color5);
        }
        if(value6 != 0){
            numValues++;
            entries.add(new PieEntry(value6));
            binding.linerLayoutPieChartPercentage6FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage6FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color6);
        }
        if(value7 != 0){
            numValues++;
            entries.add(new PieEntry(value7));
            binding.linerLayoutPieChartPercentage7FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage7FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color7);
        }
        if(value8 != 0){
            numValues++;
            entries.add(new PieEntry(value8));
            binding.linerLayoutPieChartPercentage8FragBigRectangleWithChartInTheLeftAndTextInTheRight.setVisibility(View.VISIBLE);
            binding.cardViewColorIndicatorPercentage8FragBigRectangleWithChartInTheLeftAndTextInTheRight.setCardBackgroundColor(color8);
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
            case 5:
                colors = new int[]{color1, color2, color3, color4, color5};
                break;
            case 6:
                colors = new int[]{color1, color2, color3, color4, color5, color6};
                break;
            case 7:
                colors = new int[]{color1, color2, color3, color4, color5, color6, color7};
                break;
            case 8:
                colors = new int[]{color1, color2, color3, color4, color5, color6, color7, color8};
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