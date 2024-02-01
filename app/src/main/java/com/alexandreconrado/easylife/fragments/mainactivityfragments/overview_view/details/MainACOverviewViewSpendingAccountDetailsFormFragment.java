package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsFormBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts.RectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts.RectangleWithPieChartInTheRightAndTextInTheLeftFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.howto.MainACOverviewViewSpendingsAccountDetailsHowToAddCategoryFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.howto.MainACOverviewViewSpendingsAccountDetailsHowToAddSubAccountFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.howto.MainACOverviewViewSpendingsAccountDetailsHowToEditModeFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.howto.MainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.howto.MainACOverviewViewSpendingsAccountDetailsHowToEditTitleFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.subaccount.MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.subaccount.MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainACOverviewViewSpendingAccountDetailsFormFragment extends Fragment implements
        RectangleWithPieChartExampleAddSubAccountFragment.ConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount,
        MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment.ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogColorPickerFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        RectangleWithPieChartInTheLeftAndTextInTheRightFragment.LongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight,
        RectangleWithPieChartInTheRightAndTextInTheLeftFragment.LongPressFragRectangleWithPieChartInTheRightAndTextInTheLeft,
        MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment.ExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails {
    private FragmentMainACOverviewViewSpendingAccountDetailsFormBinding binding;
    private SpendingAccountsEntity account;
    private MainACOverviewViewSpendingAccountDetailsFormFragment THIS;
    private boolean isInEditMode = false, isDisable = false, changed = false, showTutorials = true;
    private String selectedPercentageOnLongClick = "";
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;
    private DraggableCardViewEntity objectSelectInLongPress;
    //---------------EXIT LISTENNER--------------------
    private ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm listenner;

    public interface ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm{
        void onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(SpendingAccountsEntity account, boolean deleted, boolean changed);
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
        setupHowToButton();
        setupExitButton();
        setupEditModeButton();
        setupClickListennerTextViewsAux();
        setupLocalDataBase();
        setupDeleteAccountButton();
        disableBackPressed();

        return binding.getRoot();
    }
    private void disableBackPressed(){
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
    }
    private void init(boolean editModeAux){
        THIS = this;
        binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm .setText(account.getAccountTitle());
        hideAll();
        setEditMode(editModeAux);
        loadTextViews();
        loadSubAccountsCardViews(editModeAux);
        loadChart();
    }

    //----------------------SETUPS---------------------
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

            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(color);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardElevation(defaultElevation);
            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);
            binding.buttonDeleteAccountFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);

            View root = binding.getRoot();
            List<String> listNamesAux = account.getPercentagesNamesList();
            for (int i = 0; i < listNamesAux.size(); i++) {
                if(listNamesAux.get(i).equals("+")){
                    listNamesAux.remove(i);
                    i--;
                }
            }
            int num = listNamesAux.size();
            if(num < 8){
                num++;
            }
            for (int i = 1; i <= num; i++) {
                int cardViewId = getResources().getIdentifier("cardView_textView_subAccount_" + i + "_Title_Holder_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
                int frameLayoutId = getResources().getIdentifier("frameLayout_subAccount_" + i + "_fragmentHolder_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());

                CardView cardView = root.findViewById(cardViewId);
                cardView.setCardBackgroundColor(color);
                cardView.setCardElevation(defaultElevation);
                cardView.setVisibility(View.VISIBLE);

                FrameLayout frameLayout = root.findViewById(frameLayoutId);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }else{
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Color.TRANSPARENT);
            binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm.setCardElevation(0);
            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.buttonDeleteAccountFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);

            View root = binding.getRoot();
            for (int i = 1; i <= 8; i++) {
                int cardViewId = getResources().getIdentifier("cardView_textView_subAccount_" + i + "_Title_Holder_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
                int frameLayoutId = getResources().getIdentifier("frameLayout_subAccount_" + i + "_fragmentHolder_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());

                CardView cardView = root.findViewById(cardViewId);
                cardView.setCardBackgroundColor(Color.TRANSPARENT);
                cardView.setCardElevation(0);
                cardView.setVisibility(View.GONE);

                FrameLayout frameLayout = root.findViewById(frameLayoutId);
                frameLayout.setVisibility(View.GONE);
            }
        }
    }
    private void setupDeleteAccountButton(){
        binding.buttonDeleteAccountFragMainACOverviewViewSpendingAccountDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInEditMode){
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setCancelListenner(THIS);
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_Button_DeleteAccount_Text), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_DeleteAccount_Text), customAlertDialogFragment, customAlertDialogFragment, "1");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsForm_DeleteAccount");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }
            }
        });
    }
    private void setupHowToButton(){
        SharedPreferences prefs = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        showTutorials = prefs.getBoolean("hideTutorials", true);
        if(showTutorials){
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
        }
        binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSwipeDownAnimationHowTo("HowTo");
            }
        });
    }
    private void setupEditModeButton(){
        binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
                String name = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.getText().toString().trim();
                account.setAccountTitle(name);
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
                if(changed){
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setCancelListenner(THIS);
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Title), getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Text), customAlertDialogFragment, customAlertDialogFragment, "3");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsForm_Exit");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }else{
                    enableDisableEverything(false);
                    listenner.onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(account, false, false);
                }
            }
        });
    }
    private void setupClickListennerTextViewsAux(){
        View root = binding.getRoot();
        for (int i = 1; i <= 8; i++) {
            int textViewId = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
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
                List<String> namesList = account.getPercentagesNamesList();
                while (namesList.size() < 8){
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
                if(account.getPercentagesNamesList().size() > index){
                    selectedPercentageOnLongClick = account.getPercentagesNamesList().get(index);
                    if(!selectedPercentageOnLongClick.equals("+")){
                        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                        customAlertDialogFragment.setCancelListenner(THIS);
                        customAlertDialogFragment.setConfirmListenner(THIS);
                        AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_Button_DeleteCategory_Text), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_DeleteCategory_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                        customAlertDialogFragment.setCustomFragment(fragment);
                        customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsForm_DeleteCategory");
                        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                    }
                }
            }
            return false;
        }
    };
    //---------------------------------------------------

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
            int cardViewID = getResources().getIdentifier("cardView_textView_subAccount_" + i + "_Title_Holder_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
            int textViewID2 = getResources().getIdentifier("textView_PercentageText" + i + "_2_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
            int textViewID = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
            String parentTag = "FragMainACOverviewViewSpendingAccountDetailsForm";
            int auxPositionOnList = 0;
            if(subAccountsList != null){
                for (int j = 0; j < subAccountsList.size(); j++) {
                    SubSpendingAccountsEntity selected = subAccountsList.get(j);
                    if(selected.getPositionInTheList() == i){
                        isInList = true;
                        subAccount = selected;
                        auxPositionOnList = j;
                        break;
                    }
                }
            }

            if(isInList){
                FrameLayout frameLayout = root.findViewById(viewID);
                CardView cardView = root.findViewById(cardViewID);
                TextView textView = root.findViewById(textViewID);
                TextView textView2 = root.findViewById(textViewID2);
                frameLayout.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(subAccount.getAccountTitle());
                textView2.setText(subAccount.getAccountTitle());

                if(isInEditMode){
                    cardView.setCardBackgroundColor(Color.TRANSPARENT);
                    cardView.setCardElevation(0);
                    textView.setEnabled(false);
                }

                Random random = new Random();
                int style = random.nextInt(3 - 1) + 1;
                Fragment fragment = null;

                List<String> Names = subAccount.getPercentagesNamesList();

                for (int j = 0; j < Names.size(); j++) {
                    if(Names.get(j).equals("+")){
                        Names.remove(j);
                        j--;
                    }
                }

                List<Float> valuesList = calculateSpendPercentages(subAccount.getSpendsList(), Names);
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

                DraggableCardViewEntity object = new DraggableCardViewEntity(0,"",subAccount.getAccountTitle(),"");
                object.setSubAccountID(String.valueOf(auxPositionOnList));

                while (percentagesNames.size() < 4){
                    percentagesNames.add("+");
                }

                while (percentagesColors.size() < 4){
                    percentagesColors.add("-1");
                }

                while (valuesList.size() < 4){
                    valuesList.add(0.0f);
                }

                switch (style){
                    case 1:
                        RectangleWithPieChartInTheLeftAndTextInTheRightFragment fragStyle1 = new RectangleWithPieChartInTheLeftAndTextInTheRightFragment();
                        fragStyle1.setInfos(Integer.parseInt(percentagesColors.get(0)), Integer.parseInt(percentagesColors.get(1)),
                                Integer.parseInt(percentagesColors.get(2)), Integer.parseInt(percentagesColors.get(3)),
                                subAccount.getAccountTitle(),
                                valuesList.get(0),valuesList.get(1),
                                valuesList.get(2), valuesList.get(3),
                                percentagesNames.get(0), percentagesNames.get(1),
                                percentagesNames.get(2), percentagesNames.get(3), parentTag);
                        fragStyle1.setListenner(THIS);
                        fragStyle1.setObject(object);
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
                                percentagesNames.get(2), percentagesNames.get(3), parentTag);
                        fragStyle2.setListenner(THIS);
                        fragStyle2.setObject(object);
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
    private void hideAll(){
        binding.linerLayoutPieChartPercentage1FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage2FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage3FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage4FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage5FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage6FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage7FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
        binding.linerLayoutPieChartPercentage8FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.GONE);
    }
    private void loadChart(){
        PieChart pieChart = binding.pieChartFragMainACOverviewViewSpendingAccountDetailsForm;
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
                    entries.add(new PieEntry(percetagesValuesList.get(0)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(0));
                    binding.linerLayoutPieChartPercentage1FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage1FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(0)));
                    binding.textViewPercentageText11FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(0)+"%");
                    break;
                case 1:
                    entries.add(new PieEntry(percetagesValuesList.get(1)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(1));
                    binding.linerLayoutPieChartPercentage2FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage2FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(1)));
                    binding.textViewPercentageText21FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(1)+"%");
                    break;
                case 2:
                    entries.add(new PieEntry(percetagesValuesList.get(2)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(2));
                    binding.linerLayoutPieChartPercentage3FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage3FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(2)));
                    binding.textViewPercentageText31FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(2)+"%");
                    break;
                case 3:
                    entries.add(new PieEntry(percetagesValuesList.get(3)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(3));
                    binding.linerLayoutPieChartPercentage4FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage4FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(3)));
                    binding.textViewPercentageText41FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(3)+"%");
                    break;
                case 4:
                    entries.add(new PieEntry(percetagesValuesList.get(4)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(4));
                    binding.linerLayoutPieChartPercentage5FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage5FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(4)));
                    binding.textViewPercentageText51FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(4)+"%");
                    break;
                case 5:
                    entries.add(new PieEntry(percetagesValuesList.get(5)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(5));
                    binding.linerLayoutPieChartPercentage6FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage6FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(5)));
                    binding.textViewPercentageText61FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(5)+"%");
                    break;
                case 6:
                    entries.add(new PieEntry(percetagesValuesList.get(6)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(6));
                    binding.linerLayoutPieChartPercentage7FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage7FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(6)));
                    binding.textViewPercentageText71FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(6)+"%");
                    break;
                case 7:
                    entries.add(new PieEntry(percetagesValuesList.get(7)));
                    colors[i] = Integer.parseInt(percentagesColorsList.get(7));
                    binding.linerLayoutPieChartPercentage8FragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
                    binding.cardViewColorIndicatorPercentage8FragMainACOverviewViewSpendingAccountDetailsForm.setCardBackgroundColor(Integer.parseInt(percentagesColorsList.get(7)));
                    binding.textViewPercentageText81FragMainACOverviewViewSpendingAccountDetailsForm.setText(percetagesValuesList.get(7)+"%");
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
    private void enableDisableEverything(boolean enable){
        if(enable){
            isDisable = false;
            binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);
            binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(true);
        }else{
            isDisable = true;
            binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);

            binding.textViewSubAccount1TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount2TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount3TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount4TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount5TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount6TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount7TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
            binding.textViewSubAccount8TitleFragMainACOverviewViewSpendingAccountDetailsForm.setEnabled(false);
        }
    }
    private void onObjectLongPress(){
        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        customAlertDialogFragment.setCancelListenner(THIS);
        customAlertDialogFragment.setConfirmListenner(THIS);
        AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_EditSubAccount_Title), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_EditSubAccount_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
        customAlertDialogFragment.setCustomFragment(fragment);
        customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsForm_EditSubAccount");
        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
    }

    //----------------------ANIMATIONS---------------------
    private void runSwipeDownAnimation(int index, String fragmentName, SubSpendingAccountsEntity selected) {
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
                switch (fragmentName){
                    case "MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment":
                        List<String> percentagesNamesList = account.getPercentagesNamesList();
                        while (percentagesNamesList.size() < 8){
                            percentagesNamesList.add("+");
                        }
                        String name = percentagesNamesList.get(index-1);
                        MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment fragment = new MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment(name, (index-1), account, percentagesNamesList);
                        fragment.setExitListenner(THIS);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, fragment, "MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment":
                        MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment fragment2 = new MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment(selected);
                        fragment2.setExitListenner(THIS);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, fragment2, "MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment")
                                .addToBackStack(null)
                                .commit();
                        break;
                }
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
                loadSubAccountsCardViews(isInEditMode);
                isDisable = false;
                init(isInEditMode);
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
    //---------------------------------------------------

    //----------------------LISTENNERS---------------------
    @Override
    public void onConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount(int index) {
        enableDisableEverything(false);
        SubSpendingAccountsEntity selected = null;
        runSwipeDownAnimation(index, "MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment", selected);
    }
    @Override
    public void onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(boolean save, SpendingAccountsEntity account) {
        if(save){
            this.account = account;
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
    @Override
    public void onConfirmButtonClicked(int color, int position, String name, boolean justGetColor) {
        account.getPercentagesNamesList().remove(position);
        account.getPercentagesNamesList().add(position, name);
        account.getPercentagesColorList().remove(position);
        account.getPercentagesColorList().add(position, String.valueOf(color));

        init(isInEditMode);
        loadSubAccountsCardViews(isInEditMode);
    }
    @Override
    public void onLongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList, String parentTag) {
        if(parentTag.equals("FragMainACOverviewViewSpendingAccountDetailsForm") && !isDisable){
            objectSelectInLongPress = object;
            onObjectLongPress();
        }
    }
    @Override
    public void onLongPressFragRectangleWithPieChartInTheRightAndTextInTheLeft(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList, String parentTag) {
        if(parentTag.equals("FragMainACOverviewViewSpendingAccountDetailsForm") && !isDisable){
            objectSelectInLongPress = object;
            onObjectLongPress();
        }
    }
    @Override
    public void onExitFragMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetails(SubSpendingAccountsEntity subAccount, SubSpendingAccountsEntity oldSubAccount, boolean deleted, boolean changedFromSubAccounts) {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragmentB = fragmentManager.findFragmentByTag("MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment");
        if (fragmentB != null && fragmentB.isAdded()) {
            fragmentManager.beginTransaction()
                    .remove(fragmentB)
                    .commit();
        }
        runSwipeUpAnimation();
        if(changedFromSubAccounts){
            this.changed = true;
            for (int i = 0; i < account.getSubAccountsList().size(); i++) {
                SubSpendingAccountsEntity selected = account.getSubAccountsList().get(i);
                if(selected.getAccountTitle().equals(oldSubAccount.getAccountTitle())){
                    account.getSubAccountsList().remove(selected);
                    List<String> percentagesNames = account.getPercentagesNamesList();
                    List<String> percentagesColors = account.getPercentagesColorList();
                    for (int j = 0; j < percentagesNames.size(); j++) {
                        if(percentagesNames.get(j).equals(selected.getAccountTitle())){
                            percentagesNames.remove(j);
                            percentagesColors.remove(j);
                            percentagesNames.add(j, subAccount.getAccountTitle());
                            percentagesColors.add(j, subAccount.getColorInParent());
                            break;
                        }
                    }
                    account.setPercentagesNamesList(percentagesNames);
                    account.setPercentagesColorList(percentagesColors);
                    if(!deleted){
                        while (subAccount.getPercentagesNamesList().size() < 4){
                            subAccount.getPercentagesNamesList().add("+");
                            subAccount.getPercentagesColorList().add("-1");
                        }
                        account.getSubAccountsList().add(i, subAccount);
                    }

                    init(isInEditMode);
                    loadSubAccountsCardViews(isInEditMode);
                    break;
                }
            }
        }
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragMainACOverviewViewSpendingAccountDetailsForm_EditSubAccount":
                for (int i = 0; i < account.getSubAccountsList().size(); i++) {
                    SubSpendingAccountsEntity selected = account.getSubAccountsList().get(i);
                    if(selected.getAccountTitle().equals(objectSelectInLongPress.getChartName())){
                        enableDisableEverything(false);
                        runSwipeDownAnimation(0, "MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment", selected);
                        break;
                    }
                }
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsForm_DeleteAccount":
                listenner.onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(account, true, true);
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsForm_Exit":
                String name = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsForm.getText().toString().trim();
                account.setAccountTitle(name);
                new LocalDatabaseUpdateAccountTask(true).execute();
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsForm_DeleteCategory":
                for (int i = 0; i < account.getPercentagesNamesList().size(); i++) {
                    if(account.getPercentagesNamesList().get(i).equals(selectedPercentageOnLongClick)){
                        account.getPercentagesNamesList().remove(i);
                        account.getPercentagesColorList().remove(i);
                        break;
                    }
                }

                View root = binding.getRoot();
                for (int i = 1; i <= 8; i++) {
                    int textViewId = getResources().getIdentifier("textView_PercentageText" + i + "_2_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
                    int textViewId2 = getResources().getIdentifier("textView_subAccount_" + i + "_Title_FragMainACOverviewViewSpendingAccountDetailsForm", "id", requireActivity().getPackageName());
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
            case "FragMainACOverviewViewSpendingAccountDetailsForm_Exit":
                enableDisableEverything(false);
                listenner.onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(account, false, false);
                break;
        }
    }
    //---------------------------------------------------

    private class LocalDatabaseUpdateAccountTask extends AsyncTask<Void, Void, SpendingAccountsEntity> {
        private boolean dontSaveHere = false;
        public LocalDatabaseUpdateAccountTask(boolean dontSaveHere){
            this.dontSaveHere = dontSaveHere;
        }
        @Override
        protected SpendingAccountsEntity doInBackground(Void... voids) {
            spendingsAccountsDao.update(account);
            return account;
        }

        @Override
        protected void onPostExecute(SpendingAccountsEntity object) {
            if(!dontSaveHere){
                init(isInEditMode);
                loadSubAccountsCardViews(isInEditMode);
            }else{
                enableDisableEverything(false);
                listenner.onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(account, false, true);
            }
        }
    }

    public void changeHowToFragment(String Tag, Boolean fromNext){
        switch (Tag){
            case "OverviewViewSpendingAccountDetails_HowTo_goToEditMode":
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, new MainACOverviewViewSpendingsAccountDetailsHowToEditModeFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "OverviewViewSpendingAccountDetails_HowTo_goToEditTitle":
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, new MainACOverviewViewSpendingsAccountDetailsHowToEditTitleFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "OverviewViewSpendingAccountDetails_HowTo_goToAddCategory":
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, new MainACOverviewViewSpendingsAccountDetailsHowToAddCategoryFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "OverviewViewSpendingAccountDetails_HowTo_goToAddSubAccount":
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, new MainACOverviewViewSpendingsAccountDetailsHowToAddSubAccountFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "OverviewViewSpendingAccountDetails_HowTo_goToEditSubAccount":
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, new MainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "HowTo_finish":
                runSwipeUpAnimationHowTo();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                int containerId = binding.frameLayoutFullScreenFragmentContainerFragMainACOverviewViewSpendingAccountDetailsForm.getId();

                for (Fragment fragment : fragmentManager.getFragments()) {
                    if (fragment != null && fragment.getId() == containerId) {
                        fragmentManager.beginTransaction().remove(fragment).commit();
                    }
                }
                break;
        }
    }
    private void runSwipeDownAnimationHowTo(String howToID) {
        enableDisableEverything(false);
        ViewGroup container1 = binding.relativeLayoutParentFragMainACOverviewViewSpendingAccountDetailsForm;
        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerFragMainACOverviewViewSpendingAccountDetailsForm;
        container1.setVisibility(View.VISIBLE);
        container2.setVisibility(View.VISIBLE);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(container1, "translationY", 0, container1.getHeight());
        translateYAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inFragment();
                container1.setVisibility(View.GONE);
                fadeInAnimation(container2);

                switch (howToID){
                    case "HowTo":
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_FragMainACOverviewViewSpendingAccountDetailsForm, new MainACOverviewViewSpendingsAccountDetailsHowToEditModeFragment(THIS, false))
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }
        });

        translateYAnimator.start();
    }
    private void runSwipeUpAnimationHowTo() {
        outFragment();
        ViewGroup container1 = binding.relativeLayoutParentFragMainACOverviewViewSpendingAccountDetailsForm;
        container1.setVisibility(View.VISIBLE);
        container1.setTranslationY(0);
        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerFragMainACOverviewViewSpendingAccountDetailsForm;
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(container1, "translationY", container1.getHeight(), 0);
        translateYAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container2.setVisibility(View.GONE);
                container2.setEnabled(false);
            }
        });

        translateYAnimator.start();
    }
    private void inFragment(){
        enableDisableEverything(false);

        binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.INVISIBLE);
        binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.INVISIBLE);
        binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.INVISIBLE);
    }
    private void outFragment(){
        enableDisableEverything(true);
        loadSubAccountsCardViews(isInEditMode);

        binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
        binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
        binding.imageViewButtonActivateEditionFragMainACOverviewViewSpendingAccountDetailsForm.setVisibility(View.VISIBLE);
    }
}