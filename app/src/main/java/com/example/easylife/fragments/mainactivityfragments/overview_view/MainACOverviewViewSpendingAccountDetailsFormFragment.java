package com.example.easylife.fragments.mainactivityfragments.overview_view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.easylife.R;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.SpendsEntity;
import com.example.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsFormBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainACOverviewViewSpendingAccountDetailsFormFragment extends Fragment {
    private FragmentMainACOverviewViewSpendingAccountDetailsFormBinding binding;
    private SpendingAccountsEntity account;
    private ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm listenner;
    public interface ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm{
        void onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(SpendingAccountsEntity account);
    }
    public void setExitButtonClickFragMainACOverviewViewSpendingAccountDetailsFormListenner(ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm listenner){
        this.listenner = listenner;
    }
    public MainACOverviewViewSpendingAccountDetailsFormFragment(SpendingAccountsEntity account) {
        this.account = account;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingAccountDetailsFormBinding.inflate(inflater);

        loadInfos();
        setupExitButton();

        return binding.getRoot();
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(account);
            }
        });
    }
    private void loadInfos(){
        binding.textViewAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.setText(account.getAccountTitle());
        List<String> percentagesNamesList = account.getPercentagesNamesList();
        binding.textViewPercentageText12FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(0).trim());
        binding.textViewPercentageText22FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(1).trim());
        binding.textViewPercentageText32FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(2).trim());
        binding.textViewPercentageText42FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(3).trim());
        binding.textViewPercentageText52FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(4).trim());
        binding.textViewPercentageText62FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(5).trim());
        binding.textViewPercentageText72FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(6).trim());
        binding.textViewPercentageText82FragMainACOverviewViewSpendingAccountDetailsForm.setText(percentagesNamesList.get(7).trim());
        loadChart();
    }
    private void loadChart(){
        PieChart pieChart = binding.pieChartFragMainACOverviewViewSpendingAccountDetailsForm;
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        List<PieEntry> entries = new ArrayList<>();
        List<String> percentagesNamesList = account.getPercentagesNamesList();
        List<String> percentagesColorsList = account.getPercentagesColorList();
        List<Float> percetagesValuesList = calculateSpendPercentages(account.getSpendsList(), percentagesNamesList);
        int[] colors = new int[percentagesColorsList.size()];
        boolean everythingIs0 = true;

        for (int i = 0; i < percetagesValuesList.size(); i++) {
            if(percetagesValuesList.get(i) > 0.0){
                everythingIs0 = false;
                break;
            }
        }

        if(everythingIs0){
            float size = percetagesValuesList.size();
            float percentagesAmount = 1.0f / size;
            for (int i = 0; i < size; i++) {
                percetagesValuesList.add(i, percentagesAmount);
            }
        }

        for (int i = 0; i <= percentagesNamesList.size(); i++) {
            switch (i){
                case 0:
                    entries.add(new PieEntry(percetagesValuesList.get(0)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(0));
                    binding.linerLayoutPieChartPercentage1FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage1FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(0)));
                    break;
                case 1:
                    entries.add(new PieEntry(percetagesValuesList.get(1)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(1));
                    binding.linerLayoutPieChartPercentage2FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage2FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(1)));
                    break;
                case 2:
                    entries.add(new PieEntry(percetagesValuesList.get(2)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(2));
                    binding.linerLayoutPieChartPercentage3FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage3FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(2)));
                    break;
                case 3:
                    entries.add(new PieEntry(percetagesValuesList.get(3)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(3));
                    binding.linerLayoutPieChartPercentage4FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage4FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(3)));
                    break;
                case 4:
                    entries.add(new PieEntry(percetagesValuesList.get(4)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(4));
                    binding.linerLayoutPieChartPercentage5FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage5FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(4)));
                    break;
                case 5:
                    entries.add(new PieEntry(percetagesValuesList.get(5)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(5));
                    binding.linerLayoutPieChartPercentage6FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage6FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(5)));
                    break;
                case 6:
                    entries.add(new PieEntry(percetagesValuesList.get(6)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(6));
                    binding.linerLayoutPieChartPercentage7FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage7FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(6)));
                    break;
                case 7:
                    entries.add(new PieEntry(percetagesValuesList.get(7)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(7));
                    binding.linerLayoutPieChartPercentage8FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage8FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(7)));
                    break;
            }
        }

        binding.textViewPercentageText11FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText21FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText31FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText41FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText51FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText61FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText71FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");
        binding.textViewPercentageText81FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0) * 100+"%");

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(colors);
        dataSet.setDrawValues(false); // Hide labels and percentages

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(color);
        pieChart.setHoleRadius(20);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1400);

        // Invalidate the chart to refresh
        pieChart.invalidate();
    }
    private static float calculatePercentage(float part, float whole) {
        if (whole != 0) {
            return (part / whole) * 100.0f;
        } else {
            return 0.0f;
        }
    }
    private List<Float> calculateSpendPercentages(List<SpendsEntity> objects, List<String> percentagesNameList){
        List<Float> percentagesList = new ArrayList<>();
        float totalAmount = 0;

        for (int i = 0; i < objects.size(); i++) {
            totalAmount += objects.get(i).getAmount();
        }

        for (int i = 0; i < percentagesNameList.size(); i++) {
            String percentage = percentagesNameList.get(i);
            float amount = 0;

            for (int j = 0; j < objects.size(); j++) {
                SpendsEntity spend = objects.get(j);
                if(spend.getIsPartOf().equals(percentage)){
                    amount += spend.getAmount();
                }
            }

            float percentageAmount = calculatePercentage(amount, totalAmount);
            percentagesList.add(percentageAmount);
        }

        return percentagesList;
    }
}