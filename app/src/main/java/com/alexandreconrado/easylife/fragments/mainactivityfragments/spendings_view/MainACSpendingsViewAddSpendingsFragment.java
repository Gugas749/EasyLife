package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
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
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACSpendingsViewAddSpendingsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogDateHourPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainACSpendingsViewAddSpendingsFragment extends Fragment implements
        CustomAlertDialogFragment.ExitAlertDialogDateHourPicker_CustomAlertDialogFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag{

    private FragmentMainACSpendingsViewAddSpendingsBinding binding;
    private List<SpendingAccountsEntity> spendingAccountsEntityList = new ArrayList<>();
    private List<SubSpendingAccountsEntity> subAcountsList = new ArrayList<>();
    private SpendingAccountsEntity selectedAccount;
    private String selectedSubSpend, subAccountCategorySelected;
    private Date spendDate;
    private UserInfosEntity userInfos;
    private MainACSpendingsViewAddSpendingsFragment THIS;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;

    private ExitMainACSpendingsViewAddSpendingsFrag listenner;
    public interface ExitMainACSpendingsViewAddSpendingsFrag{
        void onExitMainACSpendingsViewAddSpendingsFrag(boolean save, SpendingAccountsEntity object);
    }
    public void setExitMainACSpendingsViewAddSpendingsFragListenner(ExitMainACSpendingsViewAddSpendingsFrag listenner){
        this.listenner = listenner;
    }

    public MainACSpendingsViewAddSpendingsFragment(List<SpendingAccountsEntity> spendingAccountsEntityList, UserInfosEntity userInfos) {
        this.spendingAccountsEntityList = spendingAccountsEntityList;
        this.userInfos = userInfos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACSpendingsViewAddSpendingsBinding.inflate(inflater);

        spendDate = new Date();
        loadTextViewDate(spendDate);

        init();
        setupLocalDataBase();
        setupButtonSave();
        setupButtonExit();
        setupButtonChangeHours();
        setupEditTextAmount();
        setupOnItemSelectedSpinnerAccounts();
        setupOnItemSelectedSpinnerSubAccounts();
        setupOnItemSelectedSpinnerSubAccountsCategorys();

        return binding.getRoot();
    }
    private void init(){
        THIS = this;
        hideShowSubAccountSpendCategory(false);
        loadSpinnerWheres();
        loadSpinnerMainAccounts();
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
    }
    private void setupButtonSave(){
        binding.imageViewButtonConfirmFragMainACSpendingsViewAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Save_Title), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_AlertDialog_Question_Save_Text), customAlertDialogFragment, customAlertDialogFragment, "1");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACSpendingsViewAddSpendings_Save");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupButtonExit(){
        binding.imageViewButtonExitFragMainACSpendingsViewAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Save_Title), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_AlertDialog_Question_Save_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACSpendingsViewAddSpendings_Exit");
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
    private void setupEditTextAmount(){
        binding.editTextAmountSpendFragMainACSpendingsViewAddSpendings.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.editTextAmountSpendFragMainACSpendingsViewAddSpendings.getWindowToken(), 0);
                    binding.editTextAmountSpendFragMainACSpendingsViewAddSpendings.clearFocus();
                    return true;
                }
                return false;
            }
        });
        binding.editTextAmountSpendFragMainACSpendingsViewAddSpendings.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    insertDeleteToEditTextAmount(false);
                }else{
                    insertDeleteToEditTextAmount(true);
                }
            }
        });
    }
    private void setupOnItemSelectedSpinnerAccounts(){
        binding.spinnerSpendigsAccountsFragMainACSpendingsViewAddSpendings.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                selectedAccount = spendingAccountsEntityList.get(i1);
                loadSpinnerSubAccounts(i1);
            }
        });
    }
    private void setupOnItemSelectedSpinnerSubAccounts(){
        binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewAddSpendings.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                selectedSubSpend = selectedAccount.getPercentagesNamesList().get(i1);
                List<SubSpendingAccountsEntity> auxList = selectedAccount.getSubAccountsList();

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
        binding.spinnerSpendigsSubAccountsSpendsFragMainACSpendingsViewAddSpendings.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
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

    private void loadTextViewDate(Date currentDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        binding.textViewDateFragMainACSpendingsViewAddSpendings.setText(formattedDate);
    }
    private void hideShowSubAccountSpendCategory(boolean show){
        if(show){
            fadeInAnimation(binding.textViewSpinnerSpendigsSubAccountsSpendsExplainFragMainACSpendingsViewAddSpendings);
            fadeInAnimation(binding.cardViewSpinnerSpendigsSubAccountsSpendsHolderFragMainACSpendingsViewAddSpendings);
        }else{
            fadeOutAnimation(binding.textViewSpinnerSpendigsSubAccountsSpendsExplainFragMainACSpendingsViewAddSpendings);
            fadeOutAnimation(binding.cardViewSpinnerSpendigsSubAccountsSpendsHolderFragMainACSpendingsViewAddSpendings);
        }
    }
    private void insertDeleteToEditTextAmount(boolean insert){
        EditText et = binding.editTextAmountSpendFragMainACSpendingsViewAddSpendings;
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
    private void loadSpinnerWheres(){
        List<String> wheres = userInfos.addSpedingsWheres;
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACSpendingsViewAddSpendings;
        if(wheres != null){
            if(wheres.size() > 0){
                powerSpinner.setItems(wheres);
            }
        }
    }
    private void loadSpinnerMainAccounts(){
        List<String> items = new ArrayList<>();
        for (int i = 0; i < spendingAccountsEntityList.size(); i++) {
            SpendingAccountsEntity selectedObject = spendingAccountsEntityList.get(i);
            if(!selectedObject.getAccountTitle().equals("+")){
                items.add(selectedObject.getAccountTitle());
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACSpendingsViewAddSpendings;
        powerSpinner.setItems(items);
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
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewAddSpendings;
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
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsSpendsFragMainACSpendingsViewAddSpendings;
        powerSpinner.setItems(items);
    }
    private void getInfosAndSave(){
        boolean aux = false;
        String amount = binding.editTextAmountSpendFragMainACSpendingsViewAddSpendings.getText().toString().trim();
        amount = amount.replace("€", "");
        if(!amount.equals("")){
            if(selectedAccount != null){
                if(selectedSubSpend != null && !selectedSubSpend.equals("")){
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
                        float amountFloat = Float.parseFloat(amount);
                        spend.setInfos(amountFloat, spendDate, String.valueOf(selectedAccount.getId()), subAccountID, category, isSubAccount, category);

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
        if(aux){
            Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MissingInputs_Text), Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(boolean save, Date date) {
        if(save){
            spendDate = date;
            loadTextViewDate(date);
        }
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        getInfosAndSave();
    }
    @Override
    public void onCancelButtonClicked(String Tag) {
        switch (Tag){
            case "FragMainACSpendingsViewAddSpendings_Exit":
                SpendingAccountsEntity object = null;
                listenner.onExitMainACSpendingsViewAddSpendingsFrag(false, object);
                break;
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
            listenner.onExitMainACSpendingsViewAddSpendingsFrag(true, object);
        }
    }
}