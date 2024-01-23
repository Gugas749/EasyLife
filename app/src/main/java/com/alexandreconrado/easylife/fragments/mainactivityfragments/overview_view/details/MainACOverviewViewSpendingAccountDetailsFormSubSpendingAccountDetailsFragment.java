package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogColorPickerFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag {

    private FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding binding;
    private MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment THIS;
    private boolean isInEditMode = false;
    private SubSpendingAccountsEntity subAccount, oldSubAccount;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;

    private ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails exitListenner;
    public interface ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails{
        void onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(SubSpendingAccountsEntity subAccount, SubSpendingAccountsEntity oldSubAccount, boolean deleted);
    }
    public void setExitListenner(ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails exitListenner){
        this.exitListenner = exitListenner;
    }

    public MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment(SubSpendingAccountsEntity subAccount) {
        this.subAccount = subAccount;
        oldSubAccount = subAccount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding.inflate(inflater);

        init(isInEditMode);
        setupEditModeButton();
        setupExitButton();
        setupClickListennerTextViewsAux();
        setupLocalDataBase();
        setupDeleteAccountButton();

        return binding.getRoot();
    }
    private void init(boolean editModeAux){
        THIS = this;
        binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(subAccount.getAccountTitle());
        hideAll();
        setEditMode(editModeAux);
        loadTextViews();
        loadChart();
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.getText().toString().trim();
                subAccount.setAccountTitle(name);
                exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(subAccount, oldSubAccount, false);
            }
        });
    }
    private void setupEditModeButton(){
        binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setEnabled(false);
                String name = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.getText().toString().trim();
                subAccount.setAccountTitle(name);
                if(binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.isEnabled()){
                    isInEditMode = false;
                    init(false);
                }else{
                    isInEditMode = true;
                    init(true);
                }
                fadeInAnimation(binding.relativeLayoutParentFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails);
                binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setEnabled(true);
            }
        });
    }
    private void setupDeleteAccountButton(){
        binding.buttonDeleteSubAccountFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInEditMode){
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setCancelListenner(THIS);
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_Button_DeleteSubAccount_Text), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_DeleteSubAccount_Text), customAlertDialogFragment, customAlertDialogFragment, "1");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsForm_DeleteAccount");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }
            }
        });
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
    }
    private void setEditMode(boolean editMode){
        if(editMode){
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = typedValue.data;
            float defaultElevation = getResources().getDimension(androidx.cardview.R.dimen.cardview_default_elevation);

            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(defaultElevation);

            binding.cardViewTextViewSubAccount1TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
            binding.cardViewTextViewSubAccount1TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(defaultElevation);
            binding.cardViewTextViewSubAccount2TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
            binding.cardViewTextViewSubAccount2TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(defaultElevation);
            binding.cardViewTextViewSubAccount3TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
            binding.cardViewTextViewSubAccount3TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(defaultElevation);
            binding.cardViewTextViewSubAccount4TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
            binding.cardViewTextViewSubAccount4TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(defaultElevation);

            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setEnabled(true);

            binding.cardViewTextViewSubAccount1TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
            binding.cardViewTextViewSubAccount2TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
            binding.cardViewTextViewSubAccount3TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
            binding.cardViewTextViewSubAccount4TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);

            binding.buttonDeleteSubAccountFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
        }else{
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(0);

            binding.cardViewTextViewSubAccount1TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewTextViewSubAccount1TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(0);
            binding.cardViewTextViewSubAccount2TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewTextViewSubAccount2TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(0);
            binding.cardViewTextViewSubAccount3TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewTextViewSubAccount3TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(0);
            binding.cardViewTextViewSubAccount4TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewTextViewSubAccount4TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(0);

            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setEnabled(false);

            binding.cardViewTextViewSubAccount1TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
            binding.cardViewTextViewSubAccount2TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
            binding.cardViewTextViewSubAccount3TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
            binding.cardViewTextViewSubAccount4TitleHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);

            binding.buttonDeleteSubAccountFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
        }
    }
    private void setupClickListennerTextViewsAux(){
        View root = binding.getRoot();
        for (int i = 1; i <= 4; i++) {
            int textViewId = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());
            TextView textView = root.findViewById(textViewId);
            textView.setTag(i);
            textView.setOnClickListener(commonOnClickListener);
        }
    }
    private final View.OnClickListener commonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isInEditMode){
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogColorPickerFragment fragment = new AlertDialogColorPickerFragment();
                int index = (int) view.getTag();
                index--;
                List<String> namesList = subAccount.getPercentagesNamesList();
                while (namesList.size() < 4){
                    namesList.add("+");
                }
                String name = namesList.get(index);
                fragment.setInfos(customAlertDialogFragment, customAlertDialogFragment, index, name);
                fragment.setPercentagesNames(namesList);
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                int color = typedValue.data;
                customAlertDialogFragment.setBackgroundColor(color);
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setConfirmColorPickerListenner(THIS);
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        }
    };
    private void loadTextViews() {
        List<String> percentagesNamesList = subAccount.getPercentagesNamesList();
        View root = binding.getRoot();
        for (int i = 1; i <= percentagesNamesList.size(); i++) {
            int textViewId = getResources().getIdentifier("textView_PercentageText" + i + "_2_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());
            int textViewId2 = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());
            TextView textView = root.findViewById(textViewId);
            TextView textView2 = root.findViewById(textViewId2);
            textView.setText(percentagesNamesList.get(i - 1).trim());
            textView2.setText(percentagesNamesList.get(i - 1).trim());
        }
    }
    private void loadChart(){
        PieChart pieChart = binding.pieChartFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails;
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        List<PieEntry> entries = new ArrayList<>();
        List<String> percentagesNamesList = subAccount.getPercentagesNamesList();

        for (int i = 0; i < percentagesNamesList.size(); i++) {
            if(percentagesNamesList.get(i).equals("+")){
                percentagesNamesList.remove(percentagesNamesList.get(i));
                i--;
            }
        }

        List<String> percentagesColorsList = subAccount.getPercentagesColorList();
        List<Float> percetagesValuesList = calculateSpendPercentages(subAccount.getSpendsList(), percentagesNamesList);
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

        for (int i = 0; i < percentagesNamesList.size(); i++) {
            switch (i){
                case 0:
                    entries.add(new PieEntry(percetagesValuesList.get(0)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(0));
                    binding.linerLayoutPieChartPercentage1FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage1FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(0)));
                    binding.textViewPercentageText11FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(0) * 100+"%");
                    break;
                case 1:
                    entries.add(new PieEntry(percetagesValuesList.get(1)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(1));
                    binding.linerLayoutPieChartPercentage2FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage2FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(1)));
                    binding.textViewPercentageText21FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(1) * 100+"%");
                    break;
                case 2:
                    entries.add(new PieEntry(percetagesValuesList.get(2)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(2));
                    binding.linerLayoutPieChartPercentage3FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage3FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(2)));
                    binding.textViewPercentageText31FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(2) * 100+"%");
                    break;
                case 3:
                    entries.add(new PieEntry(percetagesValuesList.get(3)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(3));
                    binding.linerLayoutPieChartPercentage4FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage4FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(3)));
                    binding.textViewPercentageText41FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(3) * 100+"%");
                    break;
            }
        }

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
        pieChart.animateY(900);

        // Invalidate the chart to refresh
        pieChart.invalidate();
    }
    private void hideAll(){
        binding.linerLayoutPieChartPercentage1FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage2FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage3FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage4FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);
    }
    private void fadeInAnimation(ViewGroup view){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        view.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
    @Override
    public void onConfirmButtonClicked(int color, int position, String name, boolean justGetColor) {
        subAccount.getPercentagesNamesList().remove(position);
        subAccount.getPercentagesNamesList().add(position, name);
        subAccount.getPercentagesColorList().remove(position);
        subAccount.getPercentagesColorList().add(position, String.valueOf(color));

        init(isInEditMode);
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(subAccount, oldSubAccount, true);
    }
    @Override
    public void onCancelButtonClicked(String Tag) {

    }
}