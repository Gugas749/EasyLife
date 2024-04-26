package com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.search;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACMainViewSearchBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainACMainViewSearchFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentMainACMainViewSearchBinding binding;
    private List<SpendingAccountsEntity> spendingAccountsEntityList = new ArrayList<>();
    private List<SubSpendingAccountsEntity> subAcountsList = new ArrayList<>();
    private SpendingAccountsEntity selectedAccount;
    private String selectedSubSpend = "";
    private boolean toShowSubAccount = false;
    private MainActivity parent;

    private ChangeToDefaultButtonClick changeToDefaultButtonClick;
    public interface ChangeToDefaultButtonClick {
        void ChangeToDefaultButtonClick();
    }
    public void setChangeToDefaultButtonClick(ChangeToDefaultButtonClick changeToDefaultButtonClick){
        this.changeToDefaultButtonClick = changeToDefaultButtonClick;
    }

    public MainACMainViewSearchFragment() {
        // Required empty public constructor
    }
    public MainACMainViewSearchFragment(MainActivity parent) {
        this.parent = parent;
    }
    public MainACMainViewSearchFragment(MainActivity parent, List<SpendingAccountsEntity> spendingAccountsEntityList) {
        this.parent = parent;
        this.spendingAccountsEntityList = spendingAccountsEntityList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewSearchBinding.inflate(inflater);

        loadSpinnerMainAccounts();

        setupOnItemSelectedSpinnerAccounts();
        setupOnItemSelectedSpinnerSubAccounts();
        setupChangeButton();
        setupSearchButton();

        return binding.getRoot();
    }

    private void setupSearchButton(){
        binding.buttonSearchFragMainACMainViewSearch .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedAccount != null){
                    boolean goodToGo = true;
                    if(toShowSubAccount){
                        if(selectedSubSpend.equals(" ") || selectedSubSpend.equals("") || selectedSubSpend == null){
                            goodToGo = false;
                        }
                    }
                    if(goodToGo){
                        loadChart(selectedAccount, selectedSubSpend, toShowSubAccount);
                    }else{
                        Toast.makeText(getContext(), getString(R.string.mainAc_FragMainViewSearch_Toast_SubAccountNotSelectedError_Text), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.mainAc_FragMainViewSearch_Toast_AccountNotSelected_Text), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setupChangeButton(){
        binding.imageViewChangeToDefaultFragMainACMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToDefaultButtonClick.ChangeToDefaultButtonClick();
            }
        });
    }
    private void changeInformationCardInfos(String categoryName, String percentage){
        binding.textViewNameFragMainACMainViewSearch.setText(categoryName);
        binding.textViewPercentageFragMainACMainViewSearch.setText(percentage+"%");
        double totalSpend = 0.0;
        for (int i = 0; i < selectedAccount.getSpendsList().size(); i++) {
            if(selectedAccount.getSpendsList().get(i).getCategory().equals(categoryName)){
                totalSpend += selectedAccount.getSpendsList().get(i).getAmount();
            }
        }
        binding.textViewTotalSpentFragMainACMainViewSearch.setText(roundToTwoDecimalPlaces(totalSpend) +"€");
    }

    private void setupOnItemSelectedSpinnerAccounts(){
        binding.spinnerSpendigsAccountsFragMainACMainViewSearch.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                toShowSubAccount = false;
                selectedAccount = spendingAccountsEntityList.get(i1);
                loadSpinnerSubAccounts(i1);
            }
        });
    }
    private void setupOnItemSelectedSpinnerSubAccounts(){
        binding.spinnerSpendigsSubAccountsFragMainACMainViewSearch.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                selectedSubSpend = selectedAccount.getPercentagesNamesList().get(i1);
                List<SubSpendingAccountsEntity> auxList = selectedAccount.getSubAccountsList();

                if(auxList != null && auxList.size() > 0){
                    for (int j = 0; j < auxList.size(); j++) {
                        SubSpendingAccountsEntity selected = auxList.get(j);
                        if(selectedSubSpend.equals(selected.getAccountTitle())){
                            toShowSubAccount = true;
                            break;
                        }
                    }
                }
            }
        });
    }
    private void loadSpinnerMainAccounts(){
        List<String> items = new ArrayList<>();
        for (int i = 0; i < spendingAccountsEntityList.size(); i++) {
            SpendingAccountsEntity selectedObject = spendingAccountsEntityList.get(i);
            if(!selectedObject.getAccountTitle().equals("+")){
                items.add(selectedObject.getAccountTitle());
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACMainViewSearch;
        powerSpinner.setItems(items);
    }
    private void loadSpinnerSubAccounts(int SelectedAccount){
        subAcountsList = spendingAccountsEntityList.get(SelectedAccount).getSubAccountsList();
        List<String> namesPercentages = new ArrayList<>();
        for (int i = 0; i < subAcountsList.size(); i++) {
            namesPercentages.add(subAcountsList.get(i).getAccountTitle());
        }
        List<String> items = new ArrayList<>(namesPercentages);
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).equals("+")){
                items.remove(i);
                i--;
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsFragMainACMainViewSearch;
        powerSpinner.setItems(items);
    }

    private void loadChart(SpendingAccountsEntity selectedAccount, String selectedSubSpend, boolean toShowSubAccount){
        SpendingAccountsEntity account = new SpendingAccountsEntity();
        if(toShowSubAccount){
            List<SubSpendingAccountsEntity> auxList = selectedAccount.getSubAccountsList();

            if(auxList != null && !auxList.isEmpty()){
                for (int j = 0; j < auxList.size(); j++) {
                    SubSpendingAccountsEntity selected = auxList.get(j);
                    if(selectedSubSpend.equals(selected.getAccountTitle())){
                        account.setInfos(selectedAccount.getIdUserFirebase(), selectedAccount.getEmailUser(), selected.getAccountTitle(), selected.getSpendsList(), selected.getPercentagesNamesList(), selected.getPercentagesColorList());
                        break;
                    }
                }
            }
        }else{
            account = selectedAccount;
        }

        PieChart pieChart = binding.pieChartFragMainACMainViewSearch;
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        List<PieEntry> entries = new ArrayList<>();
        List<String> percentagesNamesList = account.getPercentagesNamesList();

        for (int i = 0; i < percentagesNamesList.size(); i++) {
            if(percentagesNamesList.get(i).equals("+")){
                percentagesNamesList.remove(percentagesNamesList.get(i));
                i--;
            }
        }

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
            float percentagesAmount = 100.0f / size;
            for (int i = 0; i < size; i++) {
                percetagesValuesList.add(i, percentagesAmount);
            }
        }

        for (int i = 0; i < percentagesNamesList.size(); i++) {
            switch (i){
                case 0:
                    entries.add(new PieEntry(percetagesValuesList.get(0), percentagesNamesList.get(0)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(0));
                    break;
                case 1:
                    entries.add(new PieEntry(percetagesValuesList.get(1), percentagesNamesList.get(1)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(1));
                    break;
                case 2:
                    entries.add(new PieEntry(percetagesValuesList.get(2), percentagesNamesList.get(2)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(2));
                    break;
                case 3:
                    entries.add(new PieEntry(percetagesValuesList.get(3), percentagesNamesList.get(3)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(3));
                    break;
                case 4:
                    entries.add(new PieEntry(percetagesValuesList.get(4), percentagesNamesList.get(4)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(4));
                    break;
                case 5:
                    entries.add(new PieEntry(percetagesValuesList.get(5), percentagesNamesList.get(5)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(5));
                    break;
                case 6:
                    entries.add(new PieEntry(percetagesValuesList.get(6), percentagesNamesList.get(6)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(6));
                    break;
                case 7:
                    entries.add(new PieEntry(percetagesValuesList.get(7), percentagesNamesList.get(7)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(7));
                    break;
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(colors);
        dataSet.setDrawValues(false); // Hide labels and percentages

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setHoleRadius(20);
        pieChart.setHoleColor(color);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setEntryLabelColor(Color.BLACK);

// Add animation
        pieChart.animateY(1000, Easing.EaseInOutCubic);

// Customize legend
        pieChart.setOnChartValueSelectedListener(this);

// Hide description
        pieChart.getDescription().setEnabled(false);

        pieChart.invalidate(); // refresh
    }
    private static float calculatePercentage(float part, float whole) {
        if (whole != 0) {
            return (part / whole) *100.0f;
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
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String percentageFormated = decimalFormat.format(percentageAmount);
            percentageFormated = percentageFormated.replace(",", ".");
            percentageAmount = Float.parseFloat(percentageFormated);
            percentagesList.add(percentageAmount);
        }

        return percentagesList;
    }
    private static String roundToTwoDecimalPlaces(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(number);
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        // Get the label and value of the selected entry
        String label = ((PieEntry) e).getLabel();
        float value = e.getY();

        // Display the selected information (you can customize this based on your application's logic)
        changeInformationCardInfos(label, String.valueOf(value));
    }
    @Override
    public void onNothingSelected() {
        // Handle when nothing is selected (optional)
    }

}