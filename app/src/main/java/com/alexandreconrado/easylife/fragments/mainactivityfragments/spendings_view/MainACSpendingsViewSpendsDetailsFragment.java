package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACSpendingsViewSpendsDetailsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogDateHourPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.add.MainACSpendingsViewAddSpendingsFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainACSpendingsViewSpendsDetailsFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.ExitAlertDialogDateHourPicker_CustomAlertDialogFrag {

    private FragmentMainACSpendingsViewSpendsDetailsBinding binding;
    private SpendsEntity spendSelected;
    private SpendingAccountsEntity selectedAccount;
    private MainACSpendingsViewSpendsDetailsFragment THIS;
    private Date spendDate;
    private List<SpendingAccountsEntity> spendingAccountsEntityList = new ArrayList<>();
    private List<SubSpendingAccountsEntity> subAcountsList = new ArrayList<>();
    private boolean changed = false, disableAll = false;
    private String selectedSubSpend, subAccountCategorySelected;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;

    private ExitFragMainACSpendingsViewSpendsDetails exitListenner;

    public interface ExitFragMainACSpendingsViewSpendsDetails{
        void onExitFragMainACSpendingsViewSpendsDetails(boolean changed, SpendingAccountsEntity account);
    }

    public MainACSpendingsViewSpendsDetailsFragment() {
        // Required empty public constructor
    }
    public MainACSpendingsViewSpendsDetailsFragment(ExitFragMainACSpendingsViewSpendsDetails exitListenner) {
        this.exitListenner = exitListenner;
    }
    public MainACSpendingsViewSpendsDetailsFragment(ExitFragMainACSpendingsViewSpendsDetails exitListenner, SpendsEntity spendSelected) {
        this.exitListenner = exitListenner;
        this.spendSelected = spendSelected;
    }
    public MainACSpendingsViewSpendsDetailsFragment(ExitFragMainACSpendingsViewSpendsDetails exitListenner, SpendsEntity spendSelected, List<SpendingAccountsEntity> spendingAccountsEntityList) {
        this.exitListenner = exitListenner;
        this.spendSelected = spendSelected;
        this.spendingAccountsEntityList = spendingAccountsEntityList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACSpendingsViewSpendsDetailsBinding.inflate(inflater);

        init();
        disableBackPressed();
        setupExitButton();
        setupOnItemSelectedSpinnerAccounts();
        setupOnItemSelectedSpinnerSubAccounts();
        setupOnItemSelectedSpinnerSubAccountsCategorys();
        setupDeleteButton();
        setupLocalDataBase();
        setupButtonChangeHours();
        setupEditTextAmount();

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
    private void init(){
        THIS = this;
        loadSpinnerMainAccounts();
        loadInfosSpinners();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        spendDate = spendSelected.getDate();
        String formattedDate = sdf.format(spendDate);
        binding.textViewDateFragMainACSpendingsViewSpendsDetails.setText(formattedDate);
        String amount = roundToTwoDecimalPlaces(spendSelected.getAmount());
        binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.setText(amount+"€");
    }
    private void disableEnableAll(boolean enable){
        binding.imageViewButtonExitFragMainACSpendingsViewSpendsDetails.setEnabled(enable);
        binding.imageViewButtonHowToUseFragMainACSpendingsViewSpendsDetails.setEnabled(enable);

        binding.buttonChangeHoursAlertDialogDateHourPicker.setEnabled(enable);
        binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.setEnabled(enable);
        binding.spinnerSpendigsAccountsFragMainACSpendingsViewSpendsDetails.setEnabled(enable);
        binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewSpendsDetails.setEnabled(enable);
        binding.spinnerSpendigsSubAccountsSpendsFragMainACSpendingsViewSpendsDetails.setEnabled(enable);

        binding.buttonDeleteAccountFragMainACSpendingsViewSpendsDetails.setEnabled(enable);
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
    }
    private void loadInfosSpinners(){
        for (int i = 0; i < spendingAccountsEntityList.size(); i++) {
            SpendingAccountsEntity selectedObject = spendingAccountsEntityList.get(i);
            if(spendSelected.getMainAccountID().equals(String.valueOf(selectedObject.getId()))){
                selectedAccount = selectedObject;
                binding.spinnerSpendigsAccountsFragMainACSpendingsViewSpendsDetails.selectItemByIndex(i);
                loadSpinnerSubAccounts(i);
                break;
            }
        }
        if(selectedAccount != null){
            List<String> percentagesNamesList = selectedAccount.getPercentagesNamesList();
            for (int i = 0; i < percentagesNamesList.size(); i++) {
                if(percentagesNamesList.get(i).equals(spendSelected.getCategory()) || percentagesNamesList.get(i).equals(spendSelected.getSubAccountID()) || percentagesNamesList.get(i).equals(spendSelected.getIsPartOf())){
                    binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewSpendsDetails.selectItemByIndex(i);
                    selectedSubSpend = percentagesNamesList.get(i);
                    break;
                }
            }

            if(!spendSelected.getSubAccountID().equals("")){
                for (int i = 0; i < selectedAccount.getSubAccountsList().size(); i++) {
                    SubSpendingAccountsEntity selectedSub = selectedAccount.getSubAccountsList().get(i);
                    if(selectedSub.getAccountTitle().equals(spendSelected.getSubAccountID())){
                        loadSpinnerSubAccountsSpends(selectedSub);
                        hideShowSubAccountSpendCategory(true);
                        for (int j = 0; j < selectedSub.getPercentagesNamesList().size(); j++) {
                            if(selectedSub.getPercentagesNamesList().get(i).equals(spendSelected.getCategory())){
                                binding.spinnerSpendigsSubAccountsSpendsFragMainACSpendingsViewSpendsDetails.selectItemByIndex(i);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACSpendingsViewSpendsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!disableAll){
                    if(changed){
                        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                        customAlertDialogFragment.setCancelListenner(THIS);
                        customAlertDialogFragment.setConfirmListenner(THIS);
                        AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Title), getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                        customAlertDialogFragment.setCustomFragment(fragment);
                        customAlertDialogFragment.setTag("FragMainACSpendingsViewSpendsDetails_Exit");
                        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                    }else{
                        disableAll = true;
                        disableEnableAll(false);
                        exitListenner.onExitFragMainACSpendingsViewSpendsDetails(false, selectedAccount);
                    }
                }
            }
        });
    }
    private void setupDeleteButton(){
        binding.buttonDeleteAccountFragMainACSpendingsViewSpendsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_Delete), getString(R.string.mainAc_FragSpendingsViewAddSpendings_AlertDialog_Question_Delete_Title), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACSpendingsViewSpendsDetails_Delete");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupButtonChangeHours(){
        binding.buttonChangeHoursAlertDialogDateHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogDateHourPickerFragment fragment = new AlertDialogDateHourPickerFragment();
                fragment.setExitAlertDialogDateHourPickerListenner(customAlertDialogFragment);
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
    private void setupOnItemSelectedSpinnerAccounts(){
        binding.spinnerSpendigsAccountsFragMainACSpendingsViewSpendsDetails.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                changed = true;
                selectedAccount = spendingAccountsEntityList.get(i1);
                loadSpinnerSubAccounts(i1);
            }
        });
    }
    private void setupOnItemSelectedSpinnerSubAccounts(){
        binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewSpendsDetails.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                changed = true;
                selectedSubSpend = selectedAccount.getPercentagesNamesList().get(i1);
                List<SubSpendingAccountsEntity> auxList = selectedAccount.getSubAccountsList();
                if(binding.textViewSpinnerSpendigsSubAccountsSpendsExplainFragMainACSpendingsViewSpendsDetails.getVisibility() != View.GONE){
                    hideShowSubAccountSpendCategory(false);
                }
                if(auxList != null && auxList.size() > 0){
                    for (int j = 0; j < auxList.size(); j++) {
                        SubSpendingAccountsEntity selected = auxList.get(j);
                        if(selectedSubSpend.equals(selected.getAccountTitle())){
                            hideShowSubAccountSpendCategory(true);
                            loadSpinnerSubAccountsSpends(selected);
                            break;
                        }
                    }
                }
            }
        });
    }
    private void setupOnItemSelectedSpinnerSubAccountsCategorys(){
        binding.spinnerSpendigsSubAccountsSpendsFragMainACSpendingsViewSpendsDetails.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                changed = true;
                SubSpendingAccountsEntity selected = null;
                List<SubSpendingAccountsEntity> auxList = selectedAccount.getSubAccountsList();
                if(auxList != null && auxList.size() > 0){
                    for (int j = 0; j < auxList.size(); j++) {
                        selected = auxList.get(j);
                        if(selectedSubSpend.equals(selected.getAccountTitle())){
                            break;
                        }
                    }
                }
                if(selected != null){
                    subAccountCategorySelected = selected.getPercentagesNamesList().get(i1);
                }
            }
        });
    }
    private void setupEditTextAmount(){
        binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.getWindowToken(), 0);
                    binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.clearFocus();
                    return true;
                }
                return false;
            }
        });
        binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changed = true;
                if(hasFocus){
                    insertDeleteToEditTextAmount(false);
                }else{
                    String amount = binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.getText().toString().trim();
                    amount = amount.replace("€", "");
                    if(!amount.equals("")){
                        double num = Double.parseDouble(amount);
                        String finalText = roundToTwoDecimalPlaces(num);
                        binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.setText(finalText);
                    }
                    insertDeleteToEditTextAmount(true);
                }
            }
        });
    }
    private static String roundToTwoDecimalPlaces(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(number);
    }
    private void loadSpinnerMainAccounts(){
        List<String> items = new ArrayList<>();
        for (int i = 0; i < spendingAccountsEntityList.size(); i++) {
            SpendingAccountsEntity selectedObject = spendingAccountsEntityList.get(i);
            if(!selectedObject.getAccountTitle().equals("+")){
                items.add(selectedObject.getAccountTitle());
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACSpendingsViewSpendsDetails;
        powerSpinner.setItems(items);
    }
    private void insertDeleteToEditTextAmount(boolean insert){
        EditText et = binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails;
        if(insert){
            String text = et.getText().toString().trim();
            text += "€";
            et.setText(text);
        }else{
            String text = et.getText().toString().trim();
            text = text.replace("€", "");
            et.setText(text);
        }
    }
    private void loadSpinnerSubAccounts(int SelectedAccount){
        subAcountsList = spendingAccountsEntityList.get(SelectedAccount).getSubAccountsList();
        List<String> namesPercenteages = spendingAccountsEntityList.get(SelectedAccount).getPercentagesNamesList();
        List<String> items = new ArrayList<>(namesPercenteages);
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).equals("+")){
                items.remove(i);
                i--;
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewSpendsDetails;
        powerSpinner.setItems(items);
    }
    private void loadSpinnerSubAccountsSpends(SubSpendingAccountsEntity subAccount){
        List<String> namesPercenteages = subAccount.getPercentagesNamesList();
        List<String> items = new ArrayList<>(namesPercenteages);
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).equals("+")){
                items.remove(i);
                i--;
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsSpendsFragMainACSpendingsViewSpendsDetails;
        powerSpinner.setItems(items);
    }
    private void hideShowSubAccountSpendCategory(boolean show){
        fadeInAnimation(binding.buttonDeleteAccountFragMainACSpendingsViewSpendsDetails);
        if(show){
            fadeInAnimation(binding.textViewSpinnerSpendigsSubAccountsSpendsExplainFragMainACSpendingsViewSpendsDetails);
            fadeInAnimation(binding.cardViewSpinnerSpendigsSubAccountsSpendsHolderFragMainACSpendingsViewSpendsDetails);
        }else{
            fadeOutAnimation(binding.textViewSpinnerSpendigsSubAccountsSpendsExplainFragMainACSpendingsViewSpendsDetails);
            fadeOutAnimation(binding.cardViewSpinnerSpendigsSubAccountsSpendsHolderFragMainACSpendingsViewSpendsDetails);
        }
    }
    private void loadTextViewDate(Date currentDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        binding.textViewDateFragMainACSpendingsViewSpendsDetails.setText(formattedDate);
    }
    private void fadeInAnimation(View view){
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
    private void fadeOutAnimation(View view){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        view.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private boolean isValidInput(String input) {
        boolean aux = true;
        if(input.contains(".")){
            if(input.length() >= 3){
                for (int i = 0; i < input.length(); i++) {
                    if(input.substring(i).equals(".")){
                        break;
                    }
                }
            }else{
                aux = false;
            }
        }

        return aux;
    }
    private void getInfosAndSave(){
        selectedAccount.getSpendsList().remove(spendSelected);

        boolean aux = false;
        String amount = binding.editTextAmountSpendFragMainACSpendingsViewSpendsDetails.getText().toString().trim();
        amount = amount.replace("€", "");
        double doubleValue = Double.parseDouble(amount);
        if(doubleValue < 10_000_000_000.00){
            if(!amount.equals("")){
                if(selectedAccount != null){
                    if(selectedSubSpend != null && !selectedSubSpend.equals("")){
                        if(isValidInput(amount)){
                            boolean isSubAccount = false;
                            String subAccountID = "";
                            String category = selectedSubSpend;
                            SubSpendingAccountsEntity subAccountSelected = null;
                            List<SubSpendingAccountsEntity> auxList = selectedAccount.getSubAccountsList();
                            if(auxList != null && auxList.size() > 0){
                                for (int j = 0; j < auxList.size(); j++) {
                                    SubSpendingAccountsEntity selected = auxList.get(j);
                                    if(selectedSubSpend.equals(selected.getAccountTitle())){
                                        subAccountID = selected.getAccountTitle();
                                        isSubAccount = true;
                                        category = subAccountCategorySelected;
                                        subAccountSelected = selected;
                                        break;
                                    }
                                }
                            }
                            boolean canGo = false;
                            String nameCategory = "";
                            if(isSubAccount){
                                if(!subAccountCategorySelected.equals("")){
                                    canGo = true;
                                }else{
                                    aux = true;
                                }
                            }else{
                                canGo = true;
                            }

                            if(canGo){
                                SpendsEntity spend = new SpendsEntity();
                                double num = Double.parseDouble(amount);
                                String stringNum = roundToTwoDecimalPlaces(num);
                                double numFinal = Double.parseDouble(stringNum);
                                spend.setInfos(numFinal, spendDate, String.valueOf(selectedAccount.getId()), subAccountID, category, isSubAccount, category);

                                if(isSubAccount){
                                    for (int i = 0; i < selectedAccount.getSubAccountsList().size(); i++) {
                                        if(selectedAccount.getSubAccountsList().get(i).equals(subAccountSelected)){
                                            selectedAccount.getSubAccountsList().remove(subAccountSelected);

                                            subAccountSelected.getSpendsList().add(spend);
                                            selectedAccount.getSubAccountsList().add(i, subAccountSelected);
                                            break;
                                        }
                                    }
                                }else{
                                    selectedAccount.getSpendsList().add(spend);
                                }

                                new LocalDatabaseUpdateTask().execute();
                            }
                        }else{
                            aux = true;
                        }
                    }else{
                        aux = true;
                    }
                }else{
                    aux = true;
                }
            }else{
                aux = true;
            }
            if(aux){
                Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MissingInputs_Text), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MaxValue_Text), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragMainACSpendingsViewSpendsDetails_Delete":
                selectedAccount.getSpendsList().remove(spendSelected);
                new LocalDatabaseUpdateTask().execute();
                break;
            case "FragMainACSpendingsViewSpendsDetails_Exit":
                getInfosAndSave();
                break;
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {

    }
    @Override
    public void onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(boolean save, Date date) {
        if(save){
            spendDate = date;
            loadTextViewDate(date);
        }
    }

    private class LocalDatabaseUpdateTask extends AsyncTask<Void, Void, SpendingAccountsEntity> {
        @Override
        protected SpendingAccountsEntity doInBackground(Void... voids) {
            spendingsAccountsDao.update(selectedAccount);

            return selectedAccount;
        }

        @Override
        protected void onPostExecute(SpendingAccountsEntity object) {
            disableAll = true;
            disableEnableAll(false);
            exitListenner.onExitFragMainACSpendingsViewSpendsDetails(true, selectedAccount);
        }
    }
}