package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACSpendingsViewAddSpendingsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogDateHourPickerFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainACSpendingsViewAddSpendingsFragment extends Fragment implements CustomAlertDialogFragment.ExitAlertDialogDateHourPicker_CustomAlertDialogFrag {

    private FragmentMainACSpendingsViewAddSpendingsBinding binding;
    private List<SpendingAccountsEntity> spendingAccountsEntityList;
    private UserInfosEntity userInfos;
    private MainACSpendingsViewAddSpendingsFragment THIS;

    private ExitMainACSpendingsViewAddSpendingsFrag listenner;
    public interface ExitMainACSpendingsViewAddSpendingsFrag{
        void onExitMainACSpendingsViewAddSpendingsFrag(boolean save);
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

        Date currentDate = new Date();
        loadTextViewDate(currentDate);

        init();
        setupButtonSave();
        setupButtonExit();
        setupButtonChangeHours();
        setupEditTextAmount();
        setupOnItemSelectedSpinnerAccounts();

        return binding.getRoot();
    }
    private void init(){
        THIS = this;
        loadSpinnerWheres();
        loadSpinnerMainAccounts();
    }
    private void setupButtonSave(){
        binding.imageViewButtonConfirmFragMainACSpendingsViewAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setupButtonExit(){
        binding.imageViewButtonExitFragMainACSpendingsViewAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onExitMainACSpendingsViewAddSpendingsFrag(false);
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
                loadSpinnerSubAccounts(i1);
            }
        });
    }
    private void loadTextViewDate(Date currentDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        binding.textViewDateFragMainACSpendingsViewAddSpendings.setText(formattedDate);
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
            items.add(selectedObject.getAccountTitle());
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACSpendingsViewAddSpendings;
        powerSpinner.setItems(items);
    }
    private void loadSpinnerSubAccounts(int SelectedAccount){
        List<String> items = new ArrayList<>();
        List<SubSpendingAccountsEntity> subAcountsList = spendingAccountsEntityList.get(SelectedAccount).getSubAccountsList();
        if(subAcountsList != null){
            for (int i = 0; i < subAcountsList.size(); i++) {
                SubSpendingAccountsEntity selectedObject = subAcountsList.get(i);
                items.add(selectedObject.getAccountTitle());
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewAddSpendings;
        powerSpinner.setItems(items);
    }
    @Override
    public void onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(boolean save, Date date) {
        if(save){
            loadTextViewDate(date);
        }
    }
}