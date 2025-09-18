package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.subaccount;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.TypedValue;
import android.view.KeyEvent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogColorPickerFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag {

    private FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding binding;
    private MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment THIS;
    private boolean isInEditMode = false, changed = false;
    private String selectedPercentageOnLongClick = "";
    private SubSpendingAccountsEntity subAccount, oldSubAccount;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;

    private ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails exitListenner;
    public interface ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails{
        void onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(SubSpendingAccountsEntity subAccount, SubSpendingAccountsEntity oldSubAccount, boolean deleted, boolean changed);
    }
    public void setExitListenner(ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails exitListenner){
        this.exitListenner = exitListenner;
    }

    public MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment() {

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
        disableBackPressed();
        setupChangeParentColorCardView();
        binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(subAccount.getColorInParent()));

        return binding.getRoot();
    }
    private void disableBackPressed(){
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.performClick();
                    return true;
                }
                return false;
            }
        });
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
                if(changed){
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setCancelListenner(THIS);
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Title), getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Text), customAlertDialogFragment, customAlertDialogFragment, "3");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_Exit");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }else{
                    exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(subAccount, oldSubAccount, false, false);
                }
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
                    customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_Delete");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }
            }
        });
    }
    private void setupChangeParentColorCardView(){
        binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogColorPickerFragment fragment = new AlertDialogColorPickerFragment();
                fragment.setInfos(customAlertDialogFragment, customAlertDialogFragment, 0, "FragMainACOverviewViewSpendingAccountDetailsAddSubAccountFrom_SecretMenssage");
                fragment.setJustGetColor(true);
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                int color = typedValue.data;
                customAlertDialogFragment.setBackgroundColor(color);
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setConfirmColorPickerListenner(THIS);
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
    }
    private void setEditMode(boolean editMode){
        if(editMode){
            changed = true;

            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = typedValue.data;
            float defaultElevation = getResources().getDimension(androidx.cardview.R.dimen.cardview_default_elevation);

            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(defaultElevation);
            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setEnabled(true);
            binding.buttonDeleteSubAccountFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);

            binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);

            View root = binding.getRoot();
            List<String> listNamesAux = subAccount.getPercentagesNamesList();
            for (int i = 0; i < listNamesAux.size(); i++) {
                if(listNamesAux.get(i).equals("+")){
                    listNamesAux.remove(i);
                    i--;
                }
            }
            int num = listNamesAux.size();
            if(num < 4){
                num++;
            }
            for (int i = 1; i <= num; i++) {
                int cardViewId = getResources().getIdentifier("cardView_textView_subAccount_" + i + "_Title_Holder_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());

                CardView cardView = root.findViewById(cardViewId);
                cardView.setCardBackgroundColor(color);
                cardView.setCardElevation(defaultElevation);
                cardView.setVisibility(View.VISIBLE);
            }
        }else{
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardElevation(0);
            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setEnabled(false);
            binding.buttonDeleteSubAccountFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);

            binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.GONE);

            View root = binding.getRoot();
            for (int i = 1; i <= 4; i++) {
                int cardViewId = getResources().getIdentifier("cardView_textView_subAccount_" + i + "_Title_Holder_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());

                CardView cardView = root.findViewById(cardViewId);
                cardView.setCardBackgroundColor(Color.TRANSPARENT);
                cardView.setCardElevation(0);
                cardView.setVisibility(View.GONE);
            }
        }
    }
     private void setupClickListennerTextViewsAux(){
        View root = binding.getRoot();
        for (int i = 1; i <= 4; i++) {
            int textViewId = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());
            TextView textView = root.findViewById(textViewId);
            textView.setTag(i);
            textView.setOnClickListener(commonOnClickListener);
            textView.setOnLongClickListener(categorysOnLongClickListener);
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
    private final View.OnLongClickListener categorysOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if(isInEditMode){
                int index = Integer.parseInt(String.valueOf(view.getTag()));
                index--;
                if(subAccount.getPercentagesNamesList().size() > index){
                    selectedPercentageOnLongClick = subAccount.getPercentagesNamesList().get(index);
                    if(!selectedPercentageOnLongClick.equals("+")){
                        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                        customAlertDialogFragment.setCancelListenner(THIS);
                        customAlertDialogFragment.setConfirmListenner(THIS);
                        AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_Button_DeleteCategory_Text), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_DeleteCategory_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                        customAlertDialogFragment.setCustomFragment(fragment);
                        customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_DeleteCategory");
                        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                    }
                }
            }
            return false;
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
            float percentagesAmount = 100.0f / size;
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
                    binding.textViewPercentageText11FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(0)+"%");
                    break;
                case 1:
                    entries.add(new PieEntry(percetagesValuesList.get(1)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(1));
                    binding.linerLayoutPieChartPercentage2FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage2FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(1)));
                    binding.textViewPercentageText21FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(1)+"%");
                    break;
                case 2:
                    entries.add(new PieEntry(percetagesValuesList.get(2)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(2));
                    binding.linerLayoutPieChartPercentage3FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage3FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(2)));
                    binding.textViewPercentageText31FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(2)+"%");
                    break;
                case 3:
                    entries.add(new PieEntry(percetagesValuesList.get(3)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(3));
                    binding.linerLayoutPieChartPercentage4FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage4FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(3)));
                    binding.textViewPercentageText41FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setText(percetagesValuesList.get(3)+"%");
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
    @Override
    public void onConfirmButtonClicked(int color, int position, String name, boolean justGetColor) {
        if(justGetColor){
            binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.setCardBackgroundColor(color);
        }else{
            subAccount.getPercentagesNamesList().remove(position);
            subAccount.getPercentagesNamesList().add(position, name);
            subAccount.getPercentagesColorList().remove(position);
            subAccount.getPercentagesColorList().add(position, String.valueOf(color));

            init(isInEditMode);
        }
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_Delete":
                exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(subAccount, oldSubAccount, true, true);
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_Exit":
                String name = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.getText().toString().trim();
                subAccount.setAccountTitle(name);
                ColorStateList colorStateList = binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails.getCardBackgroundColor();
                int color = colorStateList.getDefaultColor();
                subAccount.setColorInParent(String.valueOf(color));
                exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(subAccount, oldSubAccount, false, true);
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_DeleteCategory":
                for (int i = 0; i < subAccount.getPercentagesNamesList().size(); i++) {
                    if(subAccount.getPercentagesNamesList().get(i).equals(selectedPercentageOnLongClick)){
                        subAccount.getPercentagesNamesList().remove(i);
                        subAccount.getPercentagesColorList().remove(i);
                        break;
                    }
                }

                View root = binding.getRoot();
                for (int i = 1; i <= 4; i++) {
                    int textViewId = getResources().getIdentifier("textView_PercentageText" + i + "_2_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());
                    int textViewId2 = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails", "id", requireActivity().getPackageName());
                    TextView textView = root.findViewById(textViewId);
                    TextView textView2 = root.findViewById(textViewId2);
                    textView.setText("+");
                    textView2.setText("+");
                }
                init(false);
                break;
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {
        switch (Tag){
            case "FragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails_Exit":
                exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(subAccount, oldSubAccount, false, false);
                break;
        }
    }
}