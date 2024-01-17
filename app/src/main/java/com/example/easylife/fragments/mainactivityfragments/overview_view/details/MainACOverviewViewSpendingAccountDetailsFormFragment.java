package com.example.easylife.fragments.mainactivityfragments.overview_view.details;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.easylife.R;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.SpendsEntity;
import com.example.easylife.database.entities.SubSpendingAccountsEntity;
import com.example.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsFormBinding;
import com.example.easylife.fragments.mainactivityfragments.mainview.editlayouthowtofrags.MainACMainViewEditLayoutHowToHomeFragment;
import com.example.easylife.fragments.mainactivityfragments.mainview.mainviewpiecharts.RectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.example.easylife.fragments.mainactivityfragments.mainview.mainviewpiecharts.RectangleWithPieChartInTheRightAndTextInTheLeftFragment;
import com.example.easylife.fragments.mainactivityfragments.overview_view.details.RectangleWithPieChartExampleAddSubAccountFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainACOverviewViewSpendingAccountDetailsFormFragment extends Fragment implements
        RectangleWithPieChartExampleAddSubAccountFragment.ConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount,
        MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment.ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm {
    private FragmentMainACOverviewViewSpendingAccountDetailsFormBinding binding;
    private SpendingAccountsEntity account;
    private MainACOverviewViewSpendingAccountDetailsFormFragment THIS;
    private boolean isInEditMode = false;
    //---------------EXIT LISTENNER--------------------
    private ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm listenner;

    public interface ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm{
        void onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(SpendingAccountsEntity account);
    }
    public void setExitButtonClickFragMainACOverviewViewSpendingAccountDetailsFormListenner(ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm listenner){
        this.listenner = listenner;
    }
    //-----------------------------------------------
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

        init(isInEditMode);
        setupExitButton();
        setupEditModeButton();

        return binding.getRoot();
    }
    private void init(boolean editModeAux){
        THIS = this;
        binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm .setText(account.getAccountTitle());

        setEditMode(editModeAux);
        loadTextViews();
        loadSubAccountsCardViews(editModeAux);
        loadChart();

        if(editModeAux){
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = typedValue.data;
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(color);
            float defaultElevation = getResources().getDimension(androidx.cardview.R.dimen.cardview_default_elevation);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardElevation(defaultElevation);
        }else{
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardElevation(0);
        }
    }
    private void setEditMode(boolean editMode){
        if(editMode){
            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);

            binding.textViewSubAccount1TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount2TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount3TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount4TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount5TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount6TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount7TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.textViewSubAccount8TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);

            binding.frameLayoutSubAccount1FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount2FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount3FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount4FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount5FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount6FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount7FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount8FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
        }else{
            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);

            binding.textViewSubAccount1TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount2TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount3TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount4TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount5TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount6TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount7TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.textViewSubAccount8TitleFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);

            binding.frameLayoutSubAccount1FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount2FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount3FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount4FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount5FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount6FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount7FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount8FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        }
    }
    private void setupEditModeButton(){
        binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
                if(binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.isEnabled()){
                    isInEditMode = false;
                    init(false);
                }else{
                    isInEditMode = true;
                    init(true);
                }
                fadeInAnimation(binding.relativeLayoutParentFragMainACOverviewViewSpendingAccountDetailsForm);
                binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);
            }
        });
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(account);
            }
        });
    }
    private void loadTextViews(){
        List<String> percentagesNamesList = account.getPercentagesNamesList();
        View root = binding.getRoot();
        for (int i = 1; i <= percentagesNamesList.size(); i++) {
            int textViewId = getResources().getIdentifier("textView_PercentageText" + i + "_2_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
            int textViewId2 = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
            TextView textView = root.findViewById(textViewId);
            TextView textView2 = root.findViewById(textViewId2);
            textView.setText(percentagesNamesList.get(i-1).trim());
            textView2.setText(percentagesNamesList.get(i-1).trim());
        }
    }
    private void loadSubAccountsCardViews(boolean loadWithExample){
        View root = binding.getRoot();
        List<SubSpendingAccountsEntity> subAccountsList = account.getSubAccountsList();
        for (int i = 1; i <= 8; i++) {
            boolean isInList = false;
            SubSpendingAccountsEntity subAccount = null;
            int viewID = getResources().getIdentifier("frameLayout_subAccount_" + i + "_fragmentHolder_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
            int textViewID = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());

            if(subAccountsList != null){
                for (int j = 0; j < subAccountsList.size(); j++) {
                    SubSpendingAccountsEntity selected = subAccountsList.get(j);
                    if(selected.getPositionInTheList() == i){
                        isInList = true;
                        subAccount = selected;
                        break;
                    }
                }
            }
            if(isInList){
                FrameLayout frameLayout = root.findViewById(viewID);
                TextView textView = root.findViewById(textViewID);
                frameLayout.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);

                Random random = new Random();
                int style = random.nextInt(3 - 1) + 1;
                Fragment fragment = null;

                List<Float> valuesList = calculateSpendPercentages(subAccount.getSpendsList(), subAccount.getPercentagesNamesList());
                boolean everythingIs0 = true;

                for (int j = 0; j < valuesList.size(); j++) {
                    if(valuesList.get(j) > 0.0){
                        everythingIs0 = false;
                        break;
                    }
                }

                if(everythingIs0){
                    float size = valuesList.size();
                    float percentagesAmount = 1.0f / size;
                    for (int j = 0; j < size; j++) {
                        valuesList.add(j, percentagesAmount);
                    }
                }
                List<String> percentagesNames = subAccount.getPercentagesNamesList();
                List<String> percentagesColors = subAccount.getPercentagesColorList();

                switch (style){
                    case 1:
                        RectangleWithPieChartInTheLeftAndTextInTheRightFragment fragStyle1 = new RectangleWithPieChartInTheLeftAndTextInTheRightFragment();
                        fragStyle1.setInfos(Integer.parseInt(percentagesColors.get(0)), Integer.parseInt(percentagesColors.get(1)),
                                Integer.parseInt(percentagesColors.get(2)), Integer.parseInt(percentagesColors.get(3)),
                                subAccount.getAccountTitle(),
                                valuesList.get(0),valuesList.get(1),
                                valuesList.get(2), valuesList.get(3),
                                percentagesNames.get(0), percentagesNames.get(1),
                                percentagesNames.get(2), percentagesNames.get(3));
                        fragStyle1.setDisableFunctions(true);
                        fragment = fragStyle1;
                        break;
                    case 2:
                        RectangleWithPieChartInTheRightAndTextInTheLeftFragment fragStyle2 = new RectangleWithPieChartInTheRightAndTextInTheLeftFragment();
                        fragStyle2.setInfos(Integer.parseInt(percentagesColors.get(0)), Integer.parseInt(percentagesColors.get(1)),
                                Integer.parseInt(percentagesColors.get(2)), Integer.parseInt(percentagesColors.get(3)),
                                subAccount.getAccountTitle(),
                                valuesList.get(0),valuesList.get(1),
                                valuesList.get(2), valuesList.get(3),
                                percentagesNames.get(0), percentagesNames.get(1),
                                percentagesNames.get(2), percentagesNames.get(3));
                        fragStyle2.setDisableFunctions(true);
                        fragment = fragStyle2;
                        break;
                }

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(viewID, fragment)
                        .addToBackStack(null)
                        .commit();
            }else if(loadWithExample){
                RectangleWithPieChartExampleAddSubAccountFragment fragment = new RectangleWithPieChartExampleAddSubAccountFragment(i);
                fragment.setConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccountListenner(this);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(viewID, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
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
        pieChart.animateY(900);

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
    private void enableDisableEverything(boolean enable){
        if(enable){
            binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);

            binding.frameLayoutSubAccount1FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount2FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount3FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount4FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount5FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount6FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount7FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
            binding.frameLayoutSubAccount8FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
        }else{
            binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);

            binding.frameLayoutSubAccount1FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount2FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount3FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount4FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount5FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount6FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount7FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
            binding.frameLayoutSubAccount8FragmentHolderFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        }
    }
    private void runSwipeDownAnimation(int index) {
        ViewGroup container1 = binding.relativeLayoutParentFragMainACOverviewViewSpendingAccountDetailsForm;
        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerFragMainACOverviewViewSpendingAccountDetailsForm;
        container2.setVisibility(View.VISIBLE);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(container1, "translationY", 0, container1.getHeight());
        translateYAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container1.setVisibility(View.GONE);
                fadeInAnimation(container2);
                List<String> percentagesNamesList = account.getPercentagesNamesList();
                String name = percentagesNamesList.get(index-1);
                MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment fragment = new MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment(name, (index-1), account);
                fragment.setExitListenner(THIS);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, fragment, "MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        translateYAnimator.start();
    }
    private void runSwipeUpAnimation() {
        ViewGroup container1 = binding.relativeLayoutParentFragMainACOverviewViewSpendingAccountDetailsForm;
        container1.setVisibility(View.VISIBLE);
        container1.setTranslationY(0);
        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerFragMainACOverviewViewSpendingAccountDetailsForm;
        int containerHeight = container1.getHeight();
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(container1, "translationY", containerHeight, 0);
        translateYAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container2.setVisibility(View.GONE);
                container2.setEnabled(false);
                enableDisableEverything(true);
            }
        });

        translateYAnimator.start();
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
    @Override
    public void onConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount(int index) {
        enableDisableEverything(false);
        runSwipeDownAnimation(index);
    }
    @Override
    public void onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(boolean save, SpendingAccountsEntity account) {
        if(save){
            this.account = account;
            init(isInEditMode);
        }
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragmentB = fragmentManager.findFragmentByTag("MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment");
        if (fragmentB != null && fragmentB.isAdded()) {
            fragmentManager.beginTransaction()
                    .remove(fragmentB)
                    .commit();
        }
        runSwipeUpAnimation();
    }
}