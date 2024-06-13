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
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogDateHourPickerFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainACMainViewSearchFragment extends Fragment implements OnChartValueSelectedListener, CustomAlertDialogFragment.ExitAlertDialogDateHourPicker_CustomAlertDialogFrag {

    private FragmentMainACMainViewSearchBinding binding;
    private List<SpendingAccountsEntity> spendingAccountsEntityList = new ArrayList<>();
    private List<SubSpendingAccountsEntity> subAcountsList = new ArrayList<>();
    private List<SpendsEntity> filteredSpendsEntityList = new ArrayList<>(), notTimeStampedFilteredSpendsList = new ArrayList<>();
    private String selectedSubSpendID = "", textViewText = "", auxString = "1", selectedAccountID = "";
    private Date selectedDate1, selectedDate2;
    private boolean toShowSubAccount = false;
    private MainActivity parent;
    private MainACMainViewSearchFragment THIS;
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
        THIS = this;
        textViewText = getString(R.string.mainAc_FragMainViewSearch_DatePicker_Title_1_Text);

        binding.textViewFromDateFragMainACMainViewSearch.setText(getString(R.string.mainAc_FragMainViewSearch_Dates_Container_1_Text) + " " + "dd-MM-yyyy");
        binding.textViewToDateFragMainACMainViewSearch.setText(getString(R.string.mainAc_FragMainViewSearch_Dates_Container_2_Text) + " " + "dd-MM-yyyy");

        loadSpinnerMainAccounts();

        setupOnItemSelectedSpinnerAccounts();
        setupOnItemSelectedSpinnerSubAccounts();
        setupButtonDateToDate();
        setupChangeButton();
        setupSearchButton();

        return binding.getRoot();
    }

    private void setupSearchButton(){
        binding.buttonSearchFragMainACMainViewSearch .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedAccountID.equals(" ") || !selectedAccountID.equals("") || selectedAccountID != null){
                    boolean goodToGo = true;
                    if(toShowSubAccount){
                        if(selectedSubSpendID.equals(" ") || selectedSubSpendID.equals("") || selectedSubSpendID == null){
                            goodToGo = false;
                        }
                    }
                    if(goodToGo){
                        loadChart();
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
        binding.imageViewChangeToDefaultFragMainACMainViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinnerSpendigsAccountsFragMainACMainViewSearch.dismiss();
                binding.spinnerSpendigsSubAccountsFragMainACMainViewSearch.dismiss();
                changeToDefaultButtonClick.ChangeToDefaultButtonClick();
            }
        });
    }
    private void setupButtonDateToDate(){
        binding.buttonDateToDateFragMainACMainViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogDateHourPickerFragment fragment = new AlertDialogDateHourPickerFragment();
                fragment.setExitAlertDialogDateHourPickerListenner(customAlertDialogFragment);
                fragment.setMode2(textViewText, auxString);
                customAlertDialogFragment.setCustomFragment(fragment);
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                int color = typedValue.data;
                customAlertDialogFragment.setBackgroundColor(color);
                customAlertDialogFragment.setExitAlertDialogDateHourPickerListenner_CustomAlertDialogFrag(THIS);
                customAlertDialogFragment.setTag("Exit");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void changeInformationCardInfos(String categoryName, String percentage){
        binding.textViewNameFragMainACMainViewSearch.setText(categoryName);
        binding.textViewPercentageFragMainACMainViewSearch.setText(percentage+"%");
        double totalSpend = 0.0;
        if(selectedDate1 != null && selectedDate2 != null){
            for (int i = 0; i < filteredSpendsEntityList.size(); i++) {
                if(filteredSpendsEntityList.get(i).getCategory().equals(categoryName)){
                    totalSpend += filteredSpendsEntityList.get(i).getAmount();
                }
            }
        }else{
            for (int i = 0; i < notTimeStampedFilteredSpendsList.size(); i++) {
                if(notTimeStampedFilteredSpendsList.get(i).getCategory().equals(categoryName)){
                    totalSpend += notTimeStampedFilteredSpendsList.get(i).getAmount();
                }
            }
        }
        binding.textViewTotalSpentFragMainACMainViewSearch.setText(roundToTwoDecimalPlaces(totalSpend) +"€");
    }
    private void setupOnItemSelectedSpinnerAccounts(){
        binding.spinnerSpendigsAccountsFragMainACMainViewSearch.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                toShowSubAccount = false;
                selectedAccountID = Integer.toString(i1);
                binding.spinnerSpendigsSubAccountsFragMainACMainViewSearch.clearSelectedItem();
                loadSpinnerSubAccounts(i1);
            }
        });
    }
    private void setupOnItemSelectedSpinnerSubAccounts(){
        binding.spinnerSpendigsSubAccountsFragMainACMainViewSearch.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                toShowSubAccount = true;
                selectedSubSpendID = Integer.toString(i1);
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
    private void loadDefaultInfoCard(){
        binding.textViewNameFragMainACMainViewSearch.setText(getString(R.string.mainAc_FragMainViewSearch_InformationCard_Name_Text));
        binding.textViewPercentageFragMainACMainViewSearch.setText("00%");
        binding.textViewTotalSpentFragMainACMainViewSearch.setText("000,00€");
    }
    private void loadChart(){
        binding.pieChartFragMainACMainViewSearch.highlightValues(null);
        loadDefaultInfoCard();

        notTimeStampedFilteredSpendsList = new ArrayList<>();
        SpendingAccountsEntity account = new SpendingAccountsEntity(spendingAccountsEntityList.get(Integer.parseInt(selectedAccountID)));

        if(toShowSubAccount){
            SubSpendingAccountsEntity selected = new SubSpendingAccountsEntity(account.getSubAccountsList().get(Integer.parseInt(selectedSubSpendID)));
            account.setInfos(account.getIdUserFirebase(), account.getEmailUser(), selected.getAccountTitle(),
                    new ArrayList<>(selected.getSpendsList()), new ArrayList<>(selected.getPercentagesNamesList()),
                    new ArrayList<>(selected.getPercentagesColorList()));
        }

        PieChart pieChart = binding.pieChartFragMainACMainViewSearch;
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        List<PieEntry> entries = new ArrayList<>();
        List<String> percentagesNamesList = new ArrayList<>(account.getPercentagesNamesList());

        for (int i = 0; i < percentagesNamesList.size(); i++) {
            if(percentagesNamesList.get(i).equals("+")){
                percentagesNamesList.remove(percentagesNamesList.get(i));
                i--;
            }
        }

        List<String> percentagesColorsList = new ArrayList<>(account.getPercentagesColorList());

        List<Float> percetagesValuesList = new ArrayList<>();
        notTimeStampedFilteredSpendsList.addAll(account.getSpendsList());

        if(!toShowSubAccount){
            for (SubSpendingAccountsEntity subAccount : account.getSubAccountsList()) {
                SubSpendingAccountsEntity selectedSubAccount = new SubSpendingAccountsEntity(subAccount);
                for (SpendsEntity spendsEntity : selectedSubAccount.getSpendsList()) {
                    SpendsEntity spendsCopy = new SpendsEntity(spendsEntity);
                    spendsCopy.setIsPartOf(selectedSubAccount.getAccountTitle());
                    spendsCopy.setCategory(selectedSubAccount.getAccountTitle());
                    notTimeStampedFilteredSpendsList.add(spendsCopy);
                }
            }
        }

        if(selectedDate1 != null && selectedDate2 != null){
            Date startDate = null;
            Date endDate = null;
            if(selectedDate1.before(selectedDate2)){
                startDate = selectedDate1;
                endDate = selectedDate2;
            }else{
                startDate = selectedDate2;
                endDate = selectedDate1;
            }

            for (int i = 0; i < notTimeStampedFilteredSpendsList.size(); i++) {
                if(!notTimeStampedFilteredSpendsList.get(i).getDate().before(startDate) && !notTimeStampedFilteredSpendsList.get(i).getDate().after(endDate)){
                    filteredSpendsEntityList.add(notTimeStampedFilteredSpendsList.get(i));
                }
            }

            percetagesValuesList = calculateSpendPercentages(filteredSpendsEntityList, percentagesNamesList);
        }else{
            percetagesValuesList = calculateSpendPercentages(notTimeStampedFilteredSpendsList, percentagesNamesList);
        }

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
    private void loadTextViewDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(selectedDate1 != null){
            String formattedDate = dateFormat.format(selectedDate1);
            binding.textViewFromDateFragMainACMainViewSearch.setText(getString(R.string.mainAc_FragMainViewSearch_Dates_Container_1_Text) + " " + formattedDate);
        }
        if(selectedDate2 != null){
            String formattedDate = dateFormat.format(selectedDate2);
            binding.textViewToDateFragMainACMainViewSearch.setText(getString(R.string.mainAc_FragMainViewSearch_Dates_Container_2_Text) + " " + formattedDate);
        }
    }
    @Override
    public void onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(boolean save, Date date, String aux) {
        if(save){
            if(aux.equals("1")){
                selectedDate1 = date;
                loadTextViewDate();
                auxString = "2";
                textViewText = getString(R.string.mainAc_FragMainViewSearch_DatePicker_Title_2_Text);
                binding.buttonDateToDateFragMainACMainViewSearch.performClick();
            }else if(aux.equals("2")){
                selectedDate2 = date;
                loadTextViewDate();
                auxString = "1";
                textViewText = getString(R.string.mainAc_FragMainViewSearch_DatePicker_Title_1_Text);
            }
        }
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